
package com.MuhammadDwiYudhaUtama.frontend.systems;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

import com.MuhammadDwiYudhaUtama.frontend.commands.Command;
import com.MuhammadDwiYudhaUtama.frontend.commands.InputHandler;

import com.MuhammadDwiYudhaUtama.frontend.objects.GameObject;
import com.MuhammadDwiYudhaUtama.frontend.objects.Player;

import com.MuhammadDwiYudhaUtama.frontend.observers.GameObserver;

import com.MuhammadDwiYudhaUtama.frontend.states.StageClearState;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class StageFacade {

    private static final float PLAYFIELD_WIDTH = 416f;
    private static final float PLAYFIELD_HEIGHT = 464f;

    private final List<GameObject> entities = new ArrayList<>();

    private final BulletManager bulletManager = new BulletManager();

    private final CollisionReferee collisionReferee =
        new CollisionReferee();

    private final InputHandler inputHandler =
        new InputHandler();

    private final Player player;

    private LevelWaveManager waveManager;

    // ====================================================
    // Constructor
    // ====================================================

    public StageFacade(GameObserver hud) {

        player = EntityFactory.createPlayer(
            200,
            50,
            "Reimu Hakurei",
            100,
            0,
            3
        );

        player.registerObserver(hud);

        entities.add(player);

        bulletManager.setEntities(entities);

        inputHandler.setEntities(entities);

        waveManager = new LevelWaveManager(
            entities,
            bulletManager,
            player
        );
    }

    // ====================================================
    // Core Methods
    // ====================================================

    public void update(float delta) {

        if (!isStageClear() && !isGameOver()) {

            for (Command command : inputHandler.handleInput(delta)) {

                command.execute(player, bulletManager);
            }

            waveManager.update(delta);

            updateAndClean(
                entities,
                delta,
                PLAYFIELD_WIDTH,
                PLAYFIELD_HEIGHT
            );

            bulletManager.update(
                delta,
                PLAYFIELD_WIDTH,
                PLAYFIELD_HEIGHT
            );

            collisionReferee.resolveCollisions(
                player,
                entities,
                bulletManager
            );
        }
    }

    public boolean isStageClear() {

        return waveManager != null
            && waveManager.getCurrentState()
            instanceof StageClearState;
    }

    public void restart() {

        entities.clear();

        bulletManager.clearEnemyBullets();

        bulletManager.clearPlayerBullets();

        entities.add(player);

        player.resetGame();

        waveManager = new LevelWaveManager(
            entities,
            bulletManager,
            player
        );
    }

    // ====================================================
    // Render
    // ====================================================

    public void render(SpriteBatch batch) {

        for (GameObject entity : entities) {

            if (!entity.isDestroyed()) {
                entity.render(batch);
            }
        }

        bulletManager.render(batch);
    }

    public void renderOverlay(ShapeRenderer shapeRenderer) {

        player.renderFocusIndicator(shapeRenderer);
    }

    // ====================================================
    // Query
    // ====================================================

    public boolean isGameOver() {

        return player.isGameOver();
    }

    public long getScore() {

        return player.getScore();
    }

    // ====================================================
    // Helper
    // ====================================================

    public <T extends GameObject> void updateAndClean(
        List<T> list,
        float delta,
        float screenWidth,
        float screenHeight
    ) {

        Iterator<T> iterator = list.iterator();

        while (iterator.hasNext()) {

            T entity = iterator.next();

            entity.update(delta);

            if (
                entity.isOffScreen(screenWidth, screenHeight)
                    || entity.isDestroyed()
            ) {
                iterator.remove();
            }
        }
    }
}
