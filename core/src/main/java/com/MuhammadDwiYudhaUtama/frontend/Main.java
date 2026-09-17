package com.MuhammadDwiYudhaUtama.frontend;

import com.MuhammadDwiYudhaUtama.frontend.objects.GameObject;
import com.MuhammadDwiYudhaUtama.frontend.objects.Player;
import com.MuhammadDwiYudhaUtama.frontend.objects.enemies.Boss;
import com.MuhammadDwiYudhaUtama.frontend.objects.enemies.Fairy;
import com.MuhammadDwiYudhaUtama.frontend.objects.items.Item;
import com.MuhammadDwiYudhaUtama.frontend.objects.items.ItemType;
import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.utils.ScreenUtils;

import java.util.ArrayList;
import java.util.List;

public class Main extends ApplicationAdapter {

    private ShapeRenderer shapeRenderer;

    private Player player;
    private Fairy fairy;
    private Boss boss;

    private Item powerItem;
    private Item pointItem;

    private List<GameObject> entities;

    @Override
    public void create() {

        shapeRenderer = new ShapeRenderer();

        entities = new ArrayList<>();

        // ==========================================
        // PLAYER
        // ==========================================

        player = new Player(
            280,
            40,
            "Reimu Hakurei",
            100,
            15,
            3
        );

        // ==========================================
        // FAIRY
        // ==========================================

        fairy = new Fairy(
            150,
            380,
            "Stage 1 Fairy",
            20
        );

        // ==========================================
        // BOSS
        // ==========================================

        boss = new Boss(
            380,
            400,
            "Cirno (Stage 2 Boss)",
            150
        );

        // ==========================================
        // ITEMS
        // ==========================================

        powerItem = new Item(
            200,
            450,
            16,
            16,
            80f,
            ItemType.POWER,
            500L
        );

        pointItem = new Item(
            320,
            480,
            12,
            12,
            120f,
            ItemType.POINT,
            1000L
        );

        // ==========================================
        // POLYMORPHIC LIST
        // ==========================================

        entities.add(player);
        entities.add(fairy);
        entities.add(boss);
        entities.add(powerItem);
        entities.add(pointItem);
    }

    @Override
    public void render() {

        float delta = Gdx.graphics.getDeltaTime();

        // ==========================================
        // POLYMORPHIC UPDATE
        // ==========================================

        for (GameObject entity : entities) {
            entity.update(delta);
        }

        // ==========================================
        // AABB COLLISION DETECTION
        // ==========================================

        for (int i = 0; i < entities.size(); i++) {

            for (int j = i + 1; j < entities.size(); j++) {

                GameObject a = entities.get(i);
                GameObject b = entities.get(j);

                if (a.getCoreHitbox().overlaps(b.getCoreHitbox())) {

                    a.onCollision(b);
                    b.onCollision(a);
                }
            }
        }

        // ==========================================
        // REMOVE COLLECTED ITEMS
        // ==========================================

        entities.removeIf(entity ->
            entity instanceof Item
                && ((Item) entity).isCollected()
        );

        // ==========================================
        // CLEAR SCREEN
        // ==========================================

        ScreenUtils.clear(
            0.1f,
            0.1f,
            0.15f,
            1f
        );

        // ==========================================
        // POLYMORPHIC RENDER
        // ==========================================

        shapeRenderer.begin(
            ShapeRenderer.ShapeType.Filled
        );

        for (GameObject entity : entities) {
            entity.render(shapeRenderer);
        }

        shapeRenderer.end();
    }

    @Override
    public void dispose() {

        if (shapeRenderer != null) {
            shapeRenderer.dispose();
        }
    }
}
