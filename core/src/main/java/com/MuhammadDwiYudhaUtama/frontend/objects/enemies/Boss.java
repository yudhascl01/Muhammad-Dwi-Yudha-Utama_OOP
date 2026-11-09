
package com.MuhammadDwiYudhaUtama.frontend.objects.enemies;

import com.badlogic.gdx.graphics.Color;

import com.MuhammadDwiYudhaUtama.frontend.objects.Player;
import com.MuhammadDwiYudhaUtama.frontend.systems.BulletManager;
import com.MuhammadDwiYudhaUtama.frontend.objects.patterns.shootingStrategy.SpreadShot;
import com.MuhammadDwiYudhaUtama.frontend.objects.patterns.entityStrategy.FixedMovement;

public class Boss extends Enemy {

    private float shootTimer = 0f;

    private float shootInterval = 1.25f;

    private float bulletSpeed = 180f;

    private int bulletDamage = 10;

    private float spreadAngle = 25f;

    public Boss(String name, int hp) {
        super(380, 400, 64, 64, Color.BLUE, name, hp, 5000L);
        // TODO (2 poin): Suntikkan strategi bawaan Boss via setter
        // 1. Tetapkan Shooting Pattern sebagai Spread Shot dengan speed 200f, 3 peluru, dan 30f spreadAngleDegerees).
        setShootingPattern(new SpreadShot(200f,3,30f));
        // 2. Tetapkan Shooting Pattern dengan Fixed Movement.
        setMovementPattern(new FixedMovement());

    }

    public Boss(float x, float y, String name, int hp) {
        super(x, y, 64, 64, Color.BLUE, name, hp, 5000L);
        // TODO (2 poin): Lakukan penyuntikan strategi yang sama pada konstruktor koordinat kustom
    }


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
