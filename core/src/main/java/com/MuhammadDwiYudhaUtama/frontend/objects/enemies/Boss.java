
package com.MuhammadDwiYudhaUtama.frontend.objects.enemies;

import com.badlogic.gdx.graphics.Color;

import com.MuhammadDwiYudhaUtama.frontend.objects.Player;
import com.MuhammadDwiYudhaUtama.frontend.systems.BulletManager;

public class Boss extends Enemy {

    private float shootTimer = 0f;

    private float shootInterval = 1.25f;

    private float bulletSpeed = 180f;

    private int bulletDamage = 10;

    private float spreadAngle = 25f;

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

    public Boss(String name, int hp) {
        this(0, 0, name, hp);
    }

    @Override
    public void update(float delta) {
        super.update(delta);
    }

    // =====================================
    // BOSS THREE-WAY SHOOTING
    // =====================================

    public void updateAttack(
        float delta,
        BulletManager bulletManager,
        Player player
    ) {

        if (
            isDestroyed()
                || bulletManager == null
                || player == null
                || player.isDestroyed()
        ) {
            return;
        }

        shootTimer += delta;

        if (shootTimer >= shootInterval) {

            shootTimer -= shootInterval;

            shootThreeWay(bulletManager, player);
        }
    }

    private void shootThreeWay(
        BulletManager bulletManager,
        Player player
    ) {

        // Titik awal peluru: tengah bawah Boss
        float originX = getX() + getWidth() / 2f;

        float originY = getY();

        // Titik tengah pemain
        float targetX =
            player.getX() + player.getWidth() / 2f;

        float targetY =
            player.getY() + player.getHeight() / 2f;

        // Sudut arah menuju pemain
        float centerAngle = (float) Math.toDegrees(
            Math.atan2(
                targetY - originY,
                targetX - originX
            )
        );

        // Tiga arah: kiri, tengah, kanan
        for (int i = -1; i <= 1; i++) {

            float angle = centerAngle + i * spreadAngle;

            float radians = (float) Math.toRadians(angle);

            float vx = (float) (
                Math.cos(radians) * bulletSpeed
            );

            float vy = (float) (
                Math.sin(radians) * bulletSpeed
            );

            bulletManager.spawnEnemyBullet(
                originX,
                originY,
                vx,
                vy,
                bulletDamage
            );
        }
    }

    public void setShootInterval(float shootInterval) {
        this.shootInterval = Math.max(0.1f, shootInterval);
    }

    public float getShootInterval() {
        return shootInterval;
    }
}
