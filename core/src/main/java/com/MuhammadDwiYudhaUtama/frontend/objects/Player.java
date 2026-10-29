
package com.MuhammadDwiYudhaUtama.frontend.objects;

import com.MuhammadDwiYudhaUtama.frontend.objects.bullets.Bullet;
import com.MuhammadDwiYudhaUtama.frontend.objects.enemies.Boss;
import com.MuhammadDwiYudhaUtama.frontend.objects.enemies.Enemy;
import com.MuhammadDwiYudhaUtama.frontend.objects.enemies.Fairy;
import com.MuhammadDwiYudhaUtama.frontend.objects.items.Item;
import com.MuhammadDwiYudhaUtama.frontend.objects.patterns.ShootingPattern;
import com.MuhammadDwiYudhaUtama.frontend.systems.AssetManager;
import com.MuhammadDwiYudhaUtama.frontend.systems.AudioSystem;
import com.MuhammadDwiYudhaUtama.frontend.systems.BulletManager;
import com.MuhammadDwiYudhaUtama.frontend.systems.EntityFactory;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;

public class Player extends GameObject {

    private String name;

    private int hp;
    private int power;
    private int spellCards;
    private int score;

    private int currentDir = 0;

    private ShootingPattern shootingPattern;

    public Player(
        float x,
        float y,
        String name,
        int hp,
        int power,
        int spellCards
    ) {
        super(
            x,
            y,
            32,
            48,
            250f,
            Color.RED
        );

        this.name = name;
        this.hp = hp;
        this.power = power;
        this.spellCards = spellCards;
        this.score = 0;
    }

    public Player(
        String name,
        int hp,
        int power,
        int spellCards
    ) {
        this(
            0,
            0,
            name,
            hp,
            power,
            spellCards
        );
    }

    @Override
    public void update(float delta) {
        super.update(delta);

        float dx = 0f;

        if (Gdx.input != null) {
            if (
                Gdx.input.isKeyPressed(Input.Keys.W)
                    || Gdx.input.isKeyPressed(Input.Keys.UP)
            ) {
                y += speed * delta;
            }

            if (
                Gdx.input.isKeyPressed(Input.Keys.S)
                    || Gdx.input.isKeyPressed(Input.Keys.DOWN)
            ) {
                y -= speed * delta;
            }

            if (
                Gdx.input.isKeyPressed(Input.Keys.A)
                    || Gdx.input.isKeyPressed(Input.Keys.LEFT)
            ) {
                x -= speed * delta;
                dx = -1f;
            }

            if (
                Gdx.input.isKeyPressed(Input.Keys.D)
                    || Gdx.input.isKeyPressed(Input.Keys.RIGHT)
            ) {
                x += speed * delta;
                dx = 1f;
            }
        }

        // Membatasi posisi pemain di area permainan.
        if (x < 0f) {
            x = 0f;
        }

        if (y < 0f) {
            y = 0f;
        }

        if (x + width > 480f) {
            x = 480f - width;
        }

        if (y + height > 480f) {
            y = 480f - height;
        }

        updateAnimationState(dx);
    }

    public void updateAnimationState(float dx) {
        AssetManager assets =
            AssetManager.getInstance();

        if (dx < 0f) {
            if (currentDir != -1) {
                currentDir = -1;

                Animation<TextureRegion> anim =
                    assets.getAnimation("player_left");

                if (anim != null) {
                    setAnimation(anim);
                }
            }
        } else if (dx > 0f) {
            if (currentDir != 1) {
                currentDir = 1;

                Animation<TextureRegion> anim =
                    assets.getAnimation("player_right");

                if (anim != null) {
                    setAnimation(anim);
                }
            }
        } else {
            if (currentDir != 0) {
                currentDir = 0;

                Animation<TextureRegion> anim =
                    assets.getAnimation("player_idle");

                if (anim != null) {
                    setAnimation(anim);
                }
            }
        }
    }

    /**
     * Metode menembak lama untuk kompatibilitas
     * dengan kode yang masih menggunakannya.
     */
    public Bullet shootBullet() {
        int damage = 10 + power;

        AudioSystem.getInstance().playBulletShot();

        System.out.println(
            name
                + " shoots bullet dealing "
                + damage
                + " DMG!"
        );

        return EntityFactory.createPlayerBullet(
            x + width / 2f - 8f,
            y + height,
            damage
        );
    }

    /**
     * Menembakkan bullet melalui BulletManager.
     */
    public void shootBullet(
        BulletManager bulletManager
    ) {
        if (
            bulletManager == null
                || isDestroyed()
        ) {
            return;
        }

        int damage = 10 + power;

        float originX =
            x + width / 2f - 8f;

        float originY =
            y + height;

        AudioSystem.getInstance().playBulletShot();

        if (shootingPattern != null) {
            shootingPattern.execute(
                originX,
                originY,
                bulletManager,
                true
            );
        } else {
            bulletManager.spawnPlayerBullet(
                originX,
                originY,
                0f,
                400f,
                damage
            );
        }
    }

    /**
     * Core hitbox pemain berukuran 8 x 8 pixel.
     */
    @Override
    public Rectangle getCoreHitbox() {
        return new Rectangle(
            x + (width / 2f) - 4f,
            y + (height / 2f) - 4f,
            8f,
            8f
        );
    }

    /**
     * Graze hitbox mengikuti ukuran sprite pemain.
     */
    @Override
    public Rectangle getGrazeHitbox() {
        return new Rectangle(
            x,
            y,
            width,
            height
        );
    }

    public void shoot(Enemy enemy) {
        if (
            enemy == null
                || enemy.isDestroyed()
        ) {
            return;
        }

        int damage = 10 + power;

        System.out.println(
            name
                + " shoots "
                + enemy.getName()
                + " for "
                + damage
                + " DMG!"
        );

        enemy.takeDamage(damage);
    }

    public void shoot(Fairy fairy) {
        shoot((Enemy) fairy);
    }

    public void shoot(Boss boss) {
        shoot((Enemy) boss);
    }

    /**
     * Mengambil item dan menerapkan efeknya.
     */
    public void collectItem(Item item) {
        if (
            item == null
                || item.isDestroyed()
        ) {
            return;
        }

        String type = item.getItemType();

        switch (type) {
            case "POWER":
                power++;

                System.out.println(
                    name + " collected POWER!"
                );
                break;

            case "POINT":
                score += 100;

                System.out.println(
                    name + " collected POINT!"
                );
                break;

            case "BOMB":
                spellCards++;

                System.out.println(
                    name + " collected BOMB!"
                );
                break;

            case "LIFE":
                hp++;

                System.out.println(
                    name + " collected LIFE!"
                );
                break;

            default:
                return;
        }

        AudioSystem.getInstance().playItemPickup();

        item.destroy();
    }

    public void takeDamage(int damage) {
        if (damage <= 0 || isDestroyed()) {
            return;
        }

        hp -= damage;

        if (hp < 0) {
            hp = 0;
        }

        if (hp <= 0) {
            destroy();
        }
    }

    @Override
    public void onCollision(Collidable other) {
        if (other instanceof Item item) {
            collectItem(item);
        }
    }

    public String getName() {
        return name;
    }

    public int getHp() {
        return hp;
    }

    public int getPower() {
        return power;
    }

    public int getSpellCards() {
        return spellCards;
    }

    public int getScore() {
        return score;
    }

    public void setHp(int hp) {
        this.hp = hp;
    }

    public void setPower(int power) {
        this.power = power;
    }

    public void setSpellCards(int spellCards) {
        this.spellCards = spellCards;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public void addScore(int points) {
        if (points > 0) {
            this.score += points;
        }
    }

    public ShootingPattern getShootingPattern() {
        return shootingPattern;
    }

    public void setShootingPattern(
        ShootingPattern shootingPattern
    ) {
        this.shootingPattern = shootingPattern;
    }
}
