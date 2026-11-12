
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

import com.MuhammadDwiYudhaUtama.frontend.objects.patterns.entityStrategy.TargetPointMovement;
import com.MuhammadDwiYudhaUtama.frontend.objects.patterns.entityStrategy.SCurveMovement;

import com.MuhammadDwiYudhaUtama.frontend.objects.patterns.shootingStrategy.SpreadShot;
import com.MuhammadDwiYudhaUtama.frontend.objects.patterns.shootingStrategy.AlternatingRingShot;

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

    private float waveTimer = 0f;
    private int wave1SpawnIndex = 0;
    private float enemyShootTimer = 0f;
    private float phaseTimer = 0f;

    private int currentPhase = 0;

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

        // BOSS
        boss = EntityFactory.createBoss(
            200,
            420,
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
        entities.add(boss);
        entities.add(powerItem);
        entities.add(pointItem);

        // SYSTEMS
        bulletManager = new BulletManager();
        bulletManager.setEntities(entities);

        boss.setMovementPattern(
            new TargetPointMovement(
                200f,
                420f,
                150f
            )
        );

        boss.setShootingPattern(
            new SpreadShot(
                220f,
                5,
                25f,
                10,
                player,
                "bullet_danmaku_red"
            )
        );

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

        // Bersihkan seluruh entitas lama.
        entities.clear();
        fairy.clear();

        // Bersihkan seluruh peluru.
        bulletManager.clearEnemyBullets();
        bulletManager.clearPlayerBullets();

        // Reset wave.
        resetFairyWave();

        // Reset timer dan fase.
        enemyShootTimer = 0f;
        phaseTimer = 0f;
        currentPhase = 0;

        // Buat Player baru.
        player = EntityFactory.createPlayer(
            200,
            50,
            "Reimu Hakurei",
            100,
            15,
            3
        );

        // Hubungkan Player baru dengan HUD.
        player.registerObserver(gameHUD);

        // Buat Boss baru.
        boss = EntityFactory.createBoss(
            200,
            420,
            "Rumia",
            150
        );

        // Atur pola pergerakan Boss.
        boss.setMovementPattern(
            new TargetPointMovement(
                200f,
                420f,
                150f
            )
        );

        // Atur pola tembakan Boss.
        boss.setShootingPattern(
            new SpreadShot(
                220f,
                5,
                25f,
                10,
                player,
                "bullet_danmaku_red"
            )
        );

        // Buat ulang item.
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

        // Daftarkan seluruh entitas baru.
        entities.add(player);
        entities.add(boss);
        entities.add(powerItem);
        entities.add(pointItem);

        // Perbarui referensi entitas pada sistem.
        inputHandler.setEntities(entities);
        bulletManager.setEntities(entities);

        System.out.println("[Main] Game restarted successfully!");
    }

    // =====================================
    // RENDER / GAME LOOP
    // =====================================

    @Override
    public void render() {

        float delta = Gdx.graphics.getDeltaTime();

        // =====================================
        // 0. RESTART INPUT
        // =====================================

        if (
            Gdx.input != null
                && Gdx.input.isKeyJustPressed(Input.Keys.R)
        ) {

            restartGame();

            // Hentikan frame saat ini.
            // Gameplay akan dilanjutkan pada frame berikutnya.
            return;
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

        updateEnemyScheduler(delta);

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

        // Render peluru.
        bulletManager.render(batch);

        // Render HUD.
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

    // =====================================
    // FAIRY WAVE
    // =====================================

    private void updateFairyWave(float delta) {

        float[] spawnX = {
            60f,
            68f,
            76f,
            84f,
            92f
        };

        waveTimer += delta;

        if (
            wave1SpawnIndex < spawnX.length
                && waveTimer >= 1.0f + wave1SpawnIndex * 0.267f
        ) {

            float x = spawnX[wave1SpawnIndex++];

            Fairy f = EntityFactory.createFairy(
                x,
                480f,
                "Left S-Curve Fairy",
                8,
                "fairy_idle_blue"
            );

            f.setMovementPattern(
                new SCurveMovement(
                    130f,
                    (float) Math.toRadians(270),
                    false
                )
            );

            f.setShootingPattern(null);

            fairy.add(f);
            entities.add(f);
        }
    }

    private void resetFairyWave() {

        waveTimer = 0f;
        wave1SpawnIndex = 0;
    }

    // =====================================
    // ENEMY SCHEDULER
    // =====================================

    private void updateEnemyScheduler(float delta) {

        // Perbarui wave peri.
        updateFairyWave(delta);

        fairy.removeIf(
            f -> f.isDestroyed() || f.isOffScreen(416, 560)
        );

        // Tembakan berkala Boss.
        enemyShootTimer += delta;

        if (
            enemyShootTimer >= 1.5f
                && boss != null
                && !boss.isDestroyed()
        ) {

            boss.shootBullet(bulletManager);

            enemyShootTimer = 0f;
        }

        // Dynamic Strategy Swapping.
        phaseTimer += delta;

        if (
            phaseTimer >= 4.0f
                && boss != null
                && !boss.isDestroyed()
        ) {

            phaseTimer = 0f;

            currentPhase = (currentPhase + 1) % 2;

            if (currentPhase == 0) {

                // Fase 1: Kiri Atas dan Aimed SpreadShot.
                boss.setMovementPattern(
                    new TargetPointMovement(
                        120f,
                        420f,
                        150f
                    )
                );

                boss.setShootingPattern(
                    new SpreadShot(
                        220f,
                        5,
                        25f,
                        10,
                        player,
                        "bullet_danmaku_red"
                    )
                );

                System.out.println(
                    "[Boss Strategy] Rumia shifted to Upper Left with Aimed SpreadShot!"
                );

            } else {

                // Fase 2: Kanan Atas dan Alternating Ring Shot.
                boss.setMovementPattern(
                    new TargetPointMovement(
                        280f,
                        420f,
                        150f
                    )
                );

                boss.setShootingPattern(
                    new AlternatingRingShot(
                        180f,
                        16,
                        10
                    )
                );

                System.out.println(
                    "[Boss Strategy] Rumia shifted to Upper Right with AlternatingRingShot!"
                );
            }
        }
    }
}
