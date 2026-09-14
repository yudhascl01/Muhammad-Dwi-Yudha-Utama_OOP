package com.MuhammadDwiYudhaUtama.frontend;

import com.MuhammadDwiYudhaUtama.frontend.objects.GameObject;
import com.MuhammadDwiYudhaUtama.frontend.objects.Player;
import com.MuhammadDwiYudhaUtama.frontend.objects.enemies.Boss;
import com.MuhammadDwiYudhaUtama.frontend.objects.enemies.Fairy;
import com.MuhammadDwiYudhaUtama.frontend.objects.items.Item;
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

    private Item pointItem1;
    private Item pointItem2;
    private Item pointItem3;

    private List<GameObject> gameObjects;

    @Override
    public void create() {

        shapeRenderer = new ShapeRenderer();

        gameObjects = new ArrayList<>();

        // Player
        player = new Player(
            280,
            40,
            "Reimu Hakurei",
            100,
            15,
            3
        );

        // Fairy
        fairy = new Fairy(
            150,
            380,
            "Stage 1 Fairy",
            20
        );

        // Boss
        boss = new Boss(
            380,
            400,
            "Cirno (Stage 2 Boss)",
            150
        );

        // Items
        pointItem1 = new Item(
            100,
            450,
            16,
            16,
            100f,
            "Point Item",
            1000L
        );

        pointItem2 = new Item(
            200,
            500,
            16,
            16,
            120f,
            "Point Item",
            1000L
        );

        pointItem3 = new Item(
            300,
            550,
            16,
            16,
            150f,
            "Point Item",
            1000L
        );

        // Polymorphic list
        gameObjects.add(player);
        gameObjects.add(fairy);
        gameObjects.add(boss);

        gameObjects.add(pointItem1);
        gameObjects.add(pointItem2);
        gameObjects.add(pointItem3);
    }

    @Override
    public void render() {

        float delta = Gdx.graphics.getDeltaTime();

        // Polymorphic update
        for (GameObject obj : gameObjects) {
            obj.update(delta);
        }

        // Clear screen
        ScreenUtils.clear(
            0.1f,
            0.1f,
            0.15f,
            1f
        );

        // Polymorphic render
        shapeRenderer.begin(
            ShapeRenderer.ShapeType.Filled
        );

        for (GameObject obj : gameObjects) {
            obj.render(shapeRenderer);
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
