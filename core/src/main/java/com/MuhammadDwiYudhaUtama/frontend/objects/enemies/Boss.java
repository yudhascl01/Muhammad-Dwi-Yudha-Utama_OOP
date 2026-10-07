package com.MuhammadDwiYudhaUtama.frontend.objects.enemies;

import com.badlogic.gdx.graphics.Color;

public class Boss extends Enemy {

    public Boss(
        float x,
        float y,
        String name,
        int hp
    ) {
        super(
            x,
            y,
            64,
            64,
            Color.RED,
            name,
            hp,
            1000L
        );
    }

    public Boss(
        String name,
        int hp
    ) {
        this(
            0,
            0,
            name,
            hp
        );
    }

    @Override
    public void update(float delta) {
        super.update(delta);
    }
}
