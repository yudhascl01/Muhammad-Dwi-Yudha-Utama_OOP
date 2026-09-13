package com.MuhammadDwiYudhaUtama.frontend;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;

public class Main extends ApplicationAdapter {

    @Override
    public void create() {
        // Program mulai dijalankan di sini.
    }

    @Override
    public void render() {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
    }

    @Override
    public void dispose() {
        // Digunakan untuk membersihkan resource jika diperlukan.
    }
}
