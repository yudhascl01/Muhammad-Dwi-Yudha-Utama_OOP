package com.MuhammadDwiYudhaUtama.frontend;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.ScreenUtils;

import com.MuhammadDwiYudhaUtama.frontend.commands.Command;
import com.MuhammadDwiYudhaUtama.frontend.commands.InputHandler;

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

import com.MuhammadDwiYudhaUtama.frontend.ui.GameHUD;

public class Main extends ApplicationAdapter {

    private SpriteBatch batch;

    private Player player;
    private Boss boss;

    private Item powerItem;
    private Item pointItem;

    private List<Fairy> fairy;
    private List<GameObject> entities;

    private BulletManager bulletManager;

    private CollisionReferee collisionReferee;

    private InputHandler inputHandler;

    private GameHUD gameHUD;

    // =====================================
    // CREATE
    // =====================================

    @Override
    public void create() {

        batch = new SpriteBatch();

        fairy = new ArrayList<>();
        entities = new ArrayList<>();

        AssetManager.getInstance().init();

        AudioSystem audio = AudioSystem.getInstance();

        audio.init();

        if (audio.isInitialized()) {
            audio.playBgm();
        }

        // PLAYER
        player = EntityFactory.createPlayer(
            200,
            50,
            "Reimu Hakurei",
            100,
            15,
            3
        );

        // RED FAIRY
        fairy.add(
            EntityFactory.createFairy(
                150,
                380,
                "Red Fairy",
                20
            )
        );

        // BLUE FAIRY
        fairy.add(
            EntityFactory.createFairy(
                250,
                380,
                "Blue Fairy",
                20,
                "fairy_idle_blue"
            )
        );

        // BOSS
        boss = EntityFactory.createBoss(
            250,
            400,
            "Rumia",
            150
        );

        // POWER ITEM
        powerItem = EntityFactory.createItem(
            180,
            450,
            ItemType.POWER
        );

        // POINT ITEM
        pointItem = EntityFactory.createItem(
            220,
            480,
            ItemType.POINT
        );

        // REGISTER ENTITIES
        entities.add(player);
        entities.addAll(fairy);
        entities.add(boss);
        entities.add(powerItem);
        entities.add(pointItem);

        // SYSTEMS
        bulletManager = new BulletManager();

        collisionReferee = new CollisionReferee();

        // INPUT HANDLER
        inputHandler = new InputHandler(entities);

        // HUD
        gameHUD = new GameHUD();

        player.registerObserver(gameHUD);
    }

    // =====================================
    // RESTART GAME
    // =====================================

    private void restartGame() {

        System.out.println("[Main] Restarting game...");

        entities.clear();

        bulletManager.clearEnemyBullets();

        bulletManager.clearPlayerBullets();

        player.resetGame();

        entities.add(player);

        fairy.clear();

        fairy.add(
            EntityFactory.createFairy(
                150,
                380,
                "Red Fairy",
                20
            )
        );

        fairy.add(
            EntityFactory.createFairy(
                250,
                380,
                "Blue Fairy",
                20,
                "fairy_idle_blue"
            )
        );

        boss = EntityFactory.createBoss(
            250,
            400,
            "Rumia",
            150
        );

        powerItem = EntityFactory.createItem(
            180,
            450,
            ItemType.POWER
        );

        pointItem = EntityFactory.createItem(
            220,
            480,
            ItemType.POINT
        );

        entities.addAll(fairy);
        entities.add(boss);
        entities.add(powerItem);
        entities.add(pointItem);

        inputHandler.setEntities(entities);
    }

    // =====================================
    // RENDER / GAME LOOP
    // =====================================

    @Override
    public void render() {

        float delta = Gdx.graphics.getDeltaTime();

        // =====================================
        // 0. RESTART GAME
        // =====================================

        if (
            Gdx.input != null
                && Gdx.input.isKeyJustPressed(Input.Keys.R)
                && player.isGameOver()
        ) {
            restartGame();
        }

        // =====================================
        // 1. INPUT HANDLER
        // =====================================

        if (!player.isDestroyed()) {

            List<Command> commands =
                inputHandler.handleInput(delta);

            for (Command command : commands) {

                command.execute(
                    player,
                    bulletManager
                );
            }
        }

        // =====================================
        // 2. UPDATE ENTITIES
        // =====================================

        updateAndClean(
            entities,
            delta,
            416,
            560
        );

        // =====================================
        // 3. BOSS ATTACK
        // =====================================

        if (boss != null && !boss.isDestroyed()) {

            boss.updateAttack(
                delta,
                bulletManager,
                player
            );
        }

        // =====================================
        // 4. UPDATE BULLETS
        // =====================================

        bulletManager.update(
            delta,
            416,
            560
        );

        // =====================================
        // 5. COLLISION
        // =====================================

        collisionReferee.resolveCollisions(
            player,
            entities,
            bulletManager
        );

        // =====================================
        // 6. RECYCLE BULLETS
        // =====================================

        bulletManager.recycleInactiveBullets();

        // =====================================
        // 7. CLEAR SCREEN
        // =====================================

        ScreenUtils.clear(
            0.1f,
            0.1f,
            0.15f,
            1f
        );

        // =====================================
        // 8. RENDER SPRITES
        // =====================================

        batch.begin();

        for (GameObject entity : entities) {

            if (!entity.isDestroyed()) {
                entity.render(batch);
            }
        }

        // Player bullets dan enemy bullets
        bulletManager.render(batch);

        // Sidebar
        gameHUD.render(batch);

        batch.end();

        // =====================================
        // 9. RENDER HUD BACKGROUND
        // =====================================

        gameHUD.renderBackground();

        // =====================================
        // 10. FOCUS INDICATOR
        // =====================================

        player.renderFocusIndicator(
            gameHUD.getShapeRenderer()
        );
    }

    // =====================================
    // UPDATE AND CLEAN
    // =====================================

    private <T extends GameObject> void updateAndClean(
        List<T> objects,
        float delta,
        float screenWidth,
        float screenHeight
    ) {

        Iterator<T> iterator = objects.iterator();

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

    // =====================================
    // DISPOSE
    // =====================================

    @Override
    public void dispose() {

        if (gameHUD != null) {
            gameHUD.dispose();
        }

        if (batch != null) {
            batch.dispose();
        }

        AudioSystem.getInstance().dispose();

        AssetManager.getInstance().dispose();
    }
}
