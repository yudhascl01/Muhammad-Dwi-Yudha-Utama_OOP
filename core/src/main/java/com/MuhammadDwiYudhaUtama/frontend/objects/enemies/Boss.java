package com.MuhammadDwiYudhaUtama.frontend.objects.enemies;

import com.MuhammadDwiYudhaUtama.frontend.objects.Collidable;
import com.MuhammadDwiYudhaUtama.frontend.objects.Player;
import com.badlogic.gdx.graphics.Color;

public class Boss extends Enemy {

    private float collisionCooldown;

    public Boss(String name, int hp) {
        super(
            380,
            400,
            48,
            48,
            Color.BLUE,
            name,
            hp,
            5000L
        );

        this.collisionCooldown = 0f;
    }

    public Boss(float x, float y, String name, int hp) {
        super(
            x,
            y,
            48,
            48,
            Color.BLUE,
            name,
            hp,
            5000L
        );

        this.collisionCooldown = 0f;
    }

    // ==========================================
    // BONUS - COLLISION COOLDOWN
    // ==========================================

    @Override
    public void update(float delta) {
        if (collisionCooldown > 0f) {
            collisionCooldown -= delta;

            if (collisionCooldown < 0f) {
                collisionCooldown = 0f;
            }
        }
    }

    // ==========================================
    // SOAL 4 - COLLISION
    // ==========================================

    @Override
    public void onCollision(Collidable other) {

        if (other instanceof Player) {

            if (collisionCooldown <= 0f) {

                System.out.println(
                    "Player touches boss"
                );

                collisionCooldown = 1.0f;
            }
        }
    }
}
