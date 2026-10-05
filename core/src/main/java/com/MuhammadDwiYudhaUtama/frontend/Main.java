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
    private Fairy fairy;
    private Boss boss;

    private Item powerItem;
    private Item pointItem;

    private List<GameObject> entities;

    @Override
    public void create() {

        batch = new SpriteBatch();

        AssetManager.getInstance().init();

        entities = new ArrayList<>();

        player = EntityFactory.createPlayer(
            280,
            40,
            "Reimu Hakurei",
            100,
            15,
            3
        );

        fairy = new Fairy(
            150,
            380,
            "Stage 1 Fairy",
            20
        );

        fairy.setAnimation(
            AssetManager.getInstance()
                .getAnimation("fairy_idle")
        );

        boss = new Boss(
            380,
            400,
            "Cirno (Stage 2 Boss)",
            150
        );

        boss.setAnimation(
            AssetManager.getInstance()
                .getAnimation("boss_idle")
        );

        powerItem = EntityFactory.createItem(
            200,
            450,
            ItemType.POWER
        );

        pointItem = EntityFactory.createItem(
            320,
            480,
            ItemType.POINT
        );

        entities.add(player);
        entities.add(fairy);
        entities.add(boss);
        entities.add(powerItem);
        entities.add(pointItem);
    }

    public <T extends GameObject> void updateAndClean(
        List<T> list,
        float delta,
        float screenWidth,
        float screenHeight
    ) {

        Iterator<T> iterator =
            list.iterator();

        while (iterator.hasNext()) {

            T entity =
                iterator.next();

            entity.update(delta);

            if (
                entity.isOffScreen(
                    screenWidth,
                    screenHeight
                )
                    || entity.isDestroyed()
            ) {

                System.out.println(
                    "Removed via Generic Iterator: "
                        + entity.getClass().getSimpleName()
                );

                iterator.remove();
            }
        }
    }

    @Override
    public void render() {

        float delta =
            Gdx.graphics.getDeltaTime();

        if (
            Gdx.input.isKeyJustPressed(
                Input.Keys.Z
            )
        ) {

            Bullet bullet =
                player.shootBullet();

            entities.add(bullet);
        }

        updateAndClean(
            entities,
            delta,
            Gdx.graphics.getWidth(),
            Gdx.graphics.getHeight()
        );

        for (int i = 0;
             i < entities.size();
             i++) {

            for (int j = i + 1;
                 j < entities.size();
                 j++) {

                GameObject a =
                    entities.get(i);

                GameObject b =
                    entities.get(j);

                if (
                    a.isDestroyed()
                        || b.isDestroyed()
                ) {
                    continue;
                }

                if (
                    a.getCoreHitbox()
                        .overlaps(
                            b.getCoreHitbox()
                        )
                ) {

                    a.onCollision(b);
                    b.onCollision(a);
                }
            }
        }

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

    @Override
    public void dispose() {

        if (batch != null) {
            batch.dispose();
        }

        AssetManager.getInstance().dispose();
    }
}
