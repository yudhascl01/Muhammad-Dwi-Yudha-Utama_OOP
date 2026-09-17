package com.MuhammadDwiYudhaUtama.frontend.objects.enemies;

import com.MuhammadDwiYudhaUtama.frontend.objects.Collidable;
import com.MuhammadDwiYudhaUtama.frontend.objects.Player;
import com.badlogic.gdx.graphics.Color;

public class Fairy extends Enemy {

    private float collisionCooldown;

    public Fairy(String name, int hp) {
        super(
            150,
            380,
            24,
            24,
            Color.PINK,
            name,
            hp,
            500L
        );

        this.collisionCooldown = 0f;
    }

    public Fairy(float x, float y, String name, int hp) {
        super(
            x,
            y,
            24,
            24,
            Color.PINK,
            name,
            hp,
            500L
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
    // SOAL 3 - COLLISION
    // ==========================================

    @Override
    public void onCollision(Collidable other) {

        if (other instanceof Player) {

            if (collisionCooldown <= 0f) {

                System.out.println(
                    "Player touches fairy"
                );

                collisionCooldown = 1.0f;
            }
        }
    }
}
