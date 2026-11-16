package com.MuhammadDwiYudhaUtama.frontend;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.ScreenUtils;

import com.MuhammadDwiYudhaUtama.frontend.systems.AssetManager;
import com.MuhammadDwiYudhaUtama.frontend.systems.StageFacade;
import com.MuhammadDwiYudhaUtama.frontend.ui.GameHUD;

public class Main extends ApplicationAdapter {

    private SpriteBatch batch;
    private GameHUD gameHUD;
    private StageFacade stage;

    @Override
    public void create() {

        batch = new SpriteBatch();

        gameHUD = new GameHUD();

        AssetManager.getInstance().init();

        stage = new StageFacade(gameHUD);
    }

    @Override
    public void render() {

        float delta = Gdx.graphics.getDeltaTime();

        if (
            Gdx.input != null
                && Gdx.input.isKeyJustPressed(Input.Keys.R)
                && (stage.isGameOver() || stage.isStageClear())
        ) {
            stage.restart();
            gameHUD.clearScoreboard();
        }

        stage.update(delta);

        gameHUD.setStageClear(stage.isStageClear());

        ScreenUtils.clear(0.1f, 0.1f, 0.15f, 1f);

        batch.begin();

        stage.render(batch);

        gameHUD.render(batch);

        batch.end();

        gameHUD.renderBackground();

        stage.renderOverlay(gameHUD.getShapeRenderer());
    }

    @Override
    public void dispose() {

        if (batch != null) {
            batch.dispose();
        }

        if (gameHUD != null) {
            gameHUD.dispose();
        }

        AssetManager.getInstance().dispose();
    }
}
