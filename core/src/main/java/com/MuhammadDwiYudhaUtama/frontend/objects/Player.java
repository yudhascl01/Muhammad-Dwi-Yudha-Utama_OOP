package com.MuhammadDwiYudhaUtama.frontend.objects;

import com.MuhammadDwiYudhaUtama.frontend.objects.bullets.Bullet;
import com.MuhammadDwiYudhaUtama.frontend.objects.enemies.Boss;
import com.MuhammadDwiYudhaUtama.frontend.objects.enemies.Enemy;
import com.MuhammadDwiYudhaUtama.frontend.objects.enemies.Fairy;
import com.MuhammadDwiYudhaUtama.frontend.objects.items.Item;
import com.MuhammadDwiYudhaUtama.frontend.systems.AssetManager;
import com.MuhammadDwiYudhaUtama.frontend.systems.EntityFactory;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class Player extends GameObject {

    private String name;
    private int hp;
    private int power;
    private int spellCards;
    private int score;
    private int currentDir = 0;

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

        float dx = 0;

        if (Gdx.input != null) {

            if (Gdx.input.isKeyPressed(Input.Keys.W)
                || Gdx.input.isKeyPressed(Input.Keys.UP)) {
                y += speed * delta;
            }

            if (Gdx.input.isKeyPressed(Input.Keys.S)
                || Gdx.input.isKeyPressed(Input.Keys.DOWN)) {
                y -= speed * delta;
            }

            if (Gdx.input.isKeyPressed(Input.Keys.A)
                || Gdx.input.isKeyPressed(Input.Keys.LEFT)) {
                x -= speed * delta;
                dx = -1;
            }

            if (Gdx.input.isKeyPressed(Input.Keys.D)
                || Gdx.input.isKeyPressed(Input.Keys.RIGHT)) {
                x += speed * delta;
                dx = 1;
            }
        }

        if (x < 0) {
            x = 0;
        }

        if (y < 0) {
            y = 0;
        }

        if (x + width > 480) {
            x = 480 - width;
        }

        if (y + height > 480) {
            y = 480 - height;
        }

        updateAnimationState(dx);
    }

    public void updateAnimationState(float dx) {

        AssetManager assets =
            AssetManager.getInstance();

        if (dx < 0) {

            if (currentDir != -1) {

                currentDir = -1;

                Animation<TextureRegion> anim =
                    assets.getAnimation(
                        "player_left"
                    );

                if (anim != null) {
                    setAnimation(anim);
                }
            }

        } else if (dx > 0) {

            if (currentDir != 1) {

                currentDir = 1;

                Animation<TextureRegion> anim =
                    assets.getAnimation(
                        "player_right"
                    );

                if (anim != null) {
                    setAnimation(anim);
                }
            }

        } else {

            if (currentDir != 0) {

                currentDir = 0;

                Animation<TextureRegion> anim =
                    assets.getAnimation(
                        "player_idle"
                    );

                if (anim != null) {
                    setAnimation(anim);
                }
            }
        }
    }

    public Bullet shootBullet() {

        int damage = 10 + power;

        System.out.println(
            name
                + " shoots bullet dealing "
                + damage
                + " DMG!"
        );

        return EntityFactory.createPlayerBullet(
            x + width / 2f - 8,
            y + height,
            damage
        );
    }

    public void shoot(Enemy enemy) {

        if (enemy == null || enemy.isDestroyed()) {
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

    public void collectItem(Item item) {

        if (item == null || item.isDestroyed()) {
            return;
        }

        String type = item.getItemType();

        switch (type) {

            case "POWER":
                power++;
                System.out.println(
                    name
                        + " collected POWER!"
                );
                break;

            case "POINT":
                score += 100;
                System.out.println(
                    name
                        + " collected POINT!"
                );
                break;

            case "BOMB":
                spellCards++;
                System.out.println(
                    name
                        + " collected BOMB!"
                );
                break;

            case "LIFE":
                hp++;
                System.out.println(
                    name
                        + " collected LIFE!"
                );
                break;
        }

        item.destroy();
    }

    public void takeDamage(int damage) {

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
}
