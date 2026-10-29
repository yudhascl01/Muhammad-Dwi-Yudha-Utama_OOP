
package com.MuhammadDwiYudhaUtama.frontend;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.MuhammadDwiYudhaUtama.frontend.objects.GameObject;
import com.MuhammadDwiYudhaUtama.frontend.objects.Player;
import com.MuhammadDwiYudhaUtama.frontend.objects.enemies.Boss;
import com.MuhammadDwiYudhaUtama.frontend.objects.enemies.Fairy;
import com.MuhammadDwiYudhaUtama.frontend.objects.items.Item;
import com.MuhammadDwiYudhaUtama.frontend.objects.items.ItemType;
import com.MuhammadDwiYudhaUtama.frontend.systems.AssetManager;
import com.MuhammadDwiYudhaUtama.frontend.systems.AudioSystem;
import com.MuhammadDwiYudhaUtama.frontend.systems.BulletManager;
import com.MuhammadDwiYudhaUtama.frontend.systems.CollisionReferee;
import com.MuhammadDwiYudhaUtama.frontend.systems.EntityFactory;
import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.ScreenUtils;

public class Main extends ApplicationAdapter {

    private SpriteBatch batch;

    private Player player;
    private Boss boss;

    private List<Fairy> fairy;
    private List<GameObject> entities;

    private BulletManager bulletManager;
    private CollisionReferee collisionReferee;

    private float enemyShootTimer = 0f;

    @Override
    public void create() {
        batch = new SpriteBatch();

        fairy = new ArrayList<>();
        entities = new ArrayList<>();

        // Inisialisasi aset
        AssetManager assets =
            AssetManager.getInstance();

        assets.init();

        // Inisialisasi audio
        AudioSystem audio =
            AudioSystem.getInstance();

        audio.init();

        if (audio.isInitialized()) {
            audio.playBgm();
        }

        // Membuat pemain
        player = EntityFactory.createPlayer(
            280,
            40,
            "Reimu Hakurei",
            100,
            15,
            3
        );

        // Membuat fairy
        Fairy redFairy =
            EntityFactory.createFairy(
                150,
                380,
                "Red Fairy",
                20
            );

        Fairy blueFairy =
            EntityFactory.createFairy(
                250,
                380,
                "Blue Fairy",
                20,
                "fairy_idle_blue"
            );

        fairy.add(redFairy);
        fairy.add(blueFairy);

        // Membuat bos Rumia
        boss = EntityFactory.createBoss(
            380,
            400,
            "Rumia",
            150
        );

        boss.setTarget(player);

        // Membuat item awal
        Item powerItem =
            EntityFactory.createItem(
                200,
                450,
                ItemType.POWER
            );

        Item pointItem =
            EntityFactory.createItem(
                320,
                480,
                ItemType.POINT
            );

        // Memasukkan seluruh entitas ke dalam list
        entities.add(player);
        entities.add(redFairy);
        entities.add(blueFairy);
        entities.add(boss);
        entities.add(powerItem);
        entities.add(pointItem);

        // Inisialisasi sistem Modul 7
        bulletManager = new BulletManager();

        collisionReferee = new CollisionReferee();
    }

    /**
     * Bos menembak setiap 1,5 detik.
     */
    private void updateEnemyScheduler(float delta) {
        if (
            boss == null
                || boss.isDestroyed()
                || bulletManager == null
        ) {
            return;
        }

        enemyShootTimer += delta;

        if (enemyShootTimer >= 1.5f) {
            boss.shootBullet(bulletManager);

            enemyShootTimer = 0f;
        }
    }

    @Override
    public void render() {
        float delta =
            Gdx.graphics.getDeltaTime();

        float screenWidth =
            Gdx.graphics.getWidth();

        float screenHeight =
            Gdx.graphics.getHeight();

        // 1. Input pemain untuk menembak
        if (
            Gdx.input.isKeyJustPressed(Input.Keys.Z)
                && player != null
                && !player.isDestroyed()
        ) {
            player.shootBullet(bulletManager);
        }

        // 2. Scheduler tembakan bos
        updateEnemyScheduler(delta);

        // 3. Update dan bersihkan entitas biasa
        updateAndClean(
            entities,
            delta,
            screenWidth,
            screenHeight
        );

        // 4. Update bullet dan daur ulang yang keluar layar
        bulletManager.update(
            delta,
            screenWidth,
            screenHeight
        );

        // 5. Evaluasi tabrakan melalui Mediator
        collisionReferee.resolveCollisions(
            player,
            entities,
            bulletManager
        );

        // 6. Daur ulang bullet yang hancur akibat collision
        bulletManager.recycleInactiveBullets();

        // 7. Bersihkan layar
        ScreenUtils.clear(
            0.1f,
            0.1f,
            0.15f,
            1f
        );

        // 8. Render entitas dan bullet
        batch.begin();

        for (GameObject entity : entities) {
            if (!entity.isDestroyed()) {
                entity.render(batch);
            }
        }

        bulletManager.render(batch);

        batch.end();
    }

    /**
     * Update dan hapus entitas standar secara aman
     * menggunakan Iterator.
     */
    private <T extends GameObject> void updateAndClean(
        List<T> objects,
        float delta,
        float screenWidth,
        float screenHeight
    ) {
        Iterator<T> iterator =
            objects.iterator();

        while (iterator.hasNext()) {
            T object = iterator.next();

            if (!object.isDestroyed()) {
                object.update(delta);
            }

            if (
                object.isDestroyed()
                    || (
                    !(object instanceof Player)
                        && object.isOffScreen(
                        screenWidth,
                        screenHeight
                    )
                )
            ) {
                iterator.remove();
            }
        }
    }

    @Override
    public void dispose() {
        AudioSystem.getInstance().dispose();

        if (batch != null) {
            batch.dispose();
        }

        AssetManager.getInstance().dispose();
    }
}
