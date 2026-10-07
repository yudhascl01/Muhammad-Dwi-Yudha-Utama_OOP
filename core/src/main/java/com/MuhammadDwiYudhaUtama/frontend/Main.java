package com.MuhammadDwiYudhaUtama.frontend;

import com.MuhammadDwiYudhaUtama.frontend.objects.GameObject;
import com.MuhammadDwiYudhaUtama.frontend.objects.Player;
import com.MuhammadDwiYudhaUtama.frontend.objects.bullets.Bullet;
import com.MuhammadDwiYudhaUtama.frontend.objects.enemies.Boss;
import com.MuhammadDwiYudhaUtama.frontend.objects.enemies.Fairy;
import com.MuhammadDwiYudhaUtama.frontend.objects.items.Item;
import com.MuhammadDwiYudhaUtama.frontend.objects.items.ItemType;
import com.MuhammadDwiYudhaUtama.frontend.systems.AssetManager;
import com.MuhammadDwiYudhaUtama.frontend.systems.EntityFactory;
import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.ScreenUtils;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class Main extends ApplicationAdapter {

    private SpriteBatch batch;
    private Player player;
    private Boss boss;
    private List<Fairy> fairy;
    private List<GameObject> entities;

    @Override
    public void create() {
        batch = new SpriteBatch();

        fairy = new ArrayList<>();
        entities = new ArrayList<>();

        AssetManager assets =
            AssetManager.getInstance();

        assets.init();

        player = EntityFactory.createPlayer(
            280,
            40,
            "Reimu Hakurei",
            100,
            15,
            3
        );

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

        boss = EntityFactory.createBoss(
            380,
            400,
            "Rumia",
            150
        );

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

        entities.add(player);
        entities.add(redFairy);
        entities.add(blueFairy);
        entities.add(boss);
        entities.add(powerItem);
        entities.add(pointItem);
    }

    @Override
    public void render() {
        float delta =
            Gdx.graphics.getDeltaTime();

        update(delta);

        ScreenUtils.clear(
            0.1f,
            0.1f,
            0.15f,
            1f
        );

        batch.begin();

        for (GameObject entity : entities) {
            if (!entity.isDestroyed()) {
                entity.render(batch);
            }
        }

        batch.end();
    }

    private void update(float delta) {

        for (GameObject entity : entities) {
            if (!entity.isDestroyed()) {
                entity.update(delta);
            }
        }

        if (Gdx.input.isKeyJustPressed(Input.Keys.Z)) {
            Bullet bullet =
                player.shootBullet();

            entities.add(bullet);
        }

        handleCollisions();

        updateAndClean(entities);
    }

    private void handleCollisions() {

        for (GameObject entity : entities) {

            if (entity.isDestroyed()) {
                continue;
            }

            for (GameObject other : entities) {

                if (entity == other
                    || other.isDestroyed()) {
                    continue;
                }

                if (entity.getCoreHitbox()
                    .overlaps(
                        other.getCoreHitbox()
                    )) {

                    entity.onCollision(other);
                }
            }
        }
    }

    private <T extends GameObject>
    void updateAndClean(
        List<T> objects
    ) {

        Iterator<T> iterator =
            objects.iterator();

        while (iterator.hasNext()) {

            T object = iterator.next();

            if (object.isDestroyed()) {
                iterator.remove();
            }
        }
    }

    @Override
    public void dispose() {

        if (batch != null) {
            batch.dispose();
        }

        AssetManager
            .getInstance()
            .dispose();
    }
}
