package com.MuhammadDwiYudhaUtama.frontend.objects.bullets;

import com.MuhammadDwiYudhaUtama.frontend.objects.BulletType;
import com.MuhammadDwiYudhaUtama.frontend.objects.Collidable;
import com.MuhammadDwiYudhaUtama.frontend.objects.GameObject;
import com.MuhammadDwiYudhaUtama.frontend.objects.enemies.Enemy;
import com.MuhammadDwiYudhaUtama.frontend.objects.patterns.bulletStrategy.BulletMovementPattern;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class Bullet extends GameObject {

    private BulletType bulletType;
    private int damage;

    private float vx;
    private float vy;
    private float angle = 90f;

    private boolean isPlayerBullet;
    private boolean grazed;

    private BulletMovementPattern movementPattern;

    public Bullet(
        float x,
        float y,
        BulletType bulletType,
        int damage
    ) {
        super(x, y, 16, 16, 400f, Color.YELLOW);

        this.bulletType = bulletType;
        this.damage = damage;
        this.vx = 0f;
        this.vy = 400f;
        this.angle = 90f;
        this.isPlayerBullet = true;
        this.grazed = false;
    }

    public Bullet(
        float x,
        float y,
        float speed,
        BulletType bulletType,
        int damage
    ) {
        super(x, y, 16, 16, speed, Color.YELLOW);

        this.bulletType = bulletType;
        this.damage = damage;
        this.vx = 0f;
        this.vy = speed;
        this.angle = speed < 0f ? 270f : 90f;
        this.isPlayerBullet = true;
        this.grazed = false;
    }

    public void init(
        float x,
        float y,
        float vx,
        float vy,
        BulletType bulletType,
        int damage,
        boolean isPlayerBullet
    ) {
        this.x = x;
        this.y = y;

        this.vx = vx;
        this.vy = vy;

        this.angle = (float) Math.toDegrees(
            Math.atan2(vy, vx)
        );

        this.speed = (float) Math.hypot(vx, vy);

        this.bulletType = bulletType;
        this.damage = damage;
        this.isPlayerBullet = isPlayerBullet;

        this.active = true;
        this.grazed = false;

        this.setStateTime(0f);
        this.movementPattern = null;
    }

    @Override
    public void update(float delta) {
        super.update(delta);

        if (!active) {
            return;
        }

        if (movementPattern != null) {
            movementPattern.move(this, delta);
        } else {
            x += vx * delta;
            y += vy * delta;
        }
    }

    @Override
    public void render(SpriteBatch batch) {
        if (batch == null || !active) {
            return;
        }

        if (getSprite() != null) {
            batch.draw(
                getSprite(),
                x,
                y,
                width / 2f,
                height / 2f,
                width,
                height,
                1f,
                1f,
                angle - 90f
            );
        } else {
            super.render(batch);
        }
    }

    @Override
    public void onCollision(Collidable other) {
        if (other instanceof Enemy enemy) {
            if (!enemy.isAlive()) {
                return;
            }

            enemy.takeDamage(damage);
            destroy();
        }
    }

    public BulletMovementPattern getMovementPattern() {
        return movementPattern;
    }

    public void setMovementPattern(
        BulletMovementPattern movementPattern
    ) {
        this.movementPattern = movementPattern;
    }

    public BulletType getBulletType() {
        return bulletType;
    }

    public int getDamage() {
        return damage;
    }

    public boolean isPlayerBullet() {
        return isPlayerBullet;
    }

    public float getVx() {
        return vx;
    }

    public float getVy() {
        return vy;
    }

    public float getAngle() {
        return angle;
    }

    public void setAngle(float angle) {
        this.angle = angle;
    }

    public boolean hasBeenGrazed() {
        return grazed;
    }

    public void setGrazed(boolean grazed) {
        this.grazed = grazed;
    }
}
