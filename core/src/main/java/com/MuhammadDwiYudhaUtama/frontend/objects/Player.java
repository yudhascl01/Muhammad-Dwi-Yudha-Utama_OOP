package com.MuhammadDwiYudhaUtama.frontend.objects;

import com.MuhammadDwiYudhaUtama.frontend.objects.bullets.Bullet;
import com.MuhammadDwiYudhaUtama.frontend.objects.enemies.Enemy;
import com.MuhammadDwiYudhaUtama.frontend.objects.items.Item;
import com.MuhammadDwiYudhaUtama.frontend.objects.items.ItemType;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;

public class Player extends GameObject {

    private String name;
    private int hp;
    private int power;
    private int spellCards;
    private long score;

    public Player(
        String name,
        int hp,
        int power,
        int spellCards
    ) {
        super(
            280,
            40,
            32,
            32,
            250f,
            Color.RED
        );

        this.name = name;
        this.hp = Math.max(0, hp);
        this.power = power;
        this.spellCards = spellCards;
        this.score = 0;
    }

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
            32,
            250f,
            Color.RED
        );

        this.name = name;
        this.hp = Math.max(0, hp);
        this.power = power;
        this.spellCards = spellCards;
        this.score = 0;
    }

    @Override
    public void update(float delta) {

        if (Gdx.input != null) {

            // ==========================================
            // MOVEMENT
            // ==========================================

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
            }

            if (
                Gdx.input.isKeyPressed(Input.Keys.D)
                    || Gdx.input.isKeyPressed(Input.Keys.RIGHT)
            ) {
                x += speed * delta;
            }

            // ==========================================
            // BATASI PLAYER AGAR TIDAK KELUAR FRAME
            // ==========================================

            float screenWidth =
                Gdx.graphics.getWidth();

            float screenHeight =
                Gdx.graphics.getHeight();

            // Batas kiri
            if (x < 0) {
                x = 0;
            }

            // Batas kanan
            if (x + width > screenWidth) {
                x = screenWidth - width;
            }

            // Batas bawah
            if (y < 0) {
                y = 0;
            }

            // Batas atas
            if (y + height > screenHeight) {
                y = screenHeight - height;
            }
        }
    }

    @Override
    public void onCollision(Collidable other) {

        if (other instanceof Item) {

            Item item = (Item) other;

            if (
                !item.isCollected()
                    && !item.isDestroyed()
            ) {

                System.out.println(
                    "Player touches items"
                );

                collectItem(item);
            }
        }
    }

    public void takeDamage(int damage) {

        setHp(getHp() - damage);

        if (getHp() > 0) {

            System.out.println(
                getName()
                    + " took "
                    + damage
                    + " damage! Remaining HP: "
                    + getHp()
            );

        } else {

            System.out.println(
                getName()
                    + " took "
                    + damage
                    + " damage! Remaining HP: 0"
            );

            System.out.println(
                getName()
                    + " was defeated (Pichuun~)!"
            );
        }
    }

    public void shoot(Enemy target) {

        int damage = 10 + getPower();

        System.out.println(
            getName()
                + " shoots "
                + target.getName()
                + " dealing "
                + damage
                + " DMG!"
        );

        boolean defeated =
            target.takeDamage(damage);

        if (defeated) {
            addScore(target.getScoreValue());
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

        return new Bullet(
            x + width / 2 - 4,
            y + height,
            BulletType.AMULET,
            damage
        );
    }

    public boolean isAlive() {
        return getHp() > 0;
    }

    public void addScore(long points) {

        if (points > 0) {

            this.score += points;

            System.out.println(
                getName()
                    + " gained "
                    + points
                    + " pts! Total Score: "
                    + this.score
            );
        }
    }

    public void collectItem(Item item) {

        if (
            item == null
                || item.isDestroyed()
        ) {
            return;
        }

        ItemType type =
            item.getItemTypeEnum();

        if (type != null) {

            switch (type) {

                case POWER -> {

                    this.power +=
                        type.getPowerBonus();

                    addScore(
                        item.getScoreValue()
                    );

                    System.out.println(
                        name
                            + " collected POWER item! "
                            + "Power increased to "
                            + power
                    );
                }

                case POINT -> {

                    addScore(
                        item.getScoreValue()
                    );

                    System.out.println(
                        name
                            + " collected POINT item!"
                    );
                }

                case BOMB -> {

                    this.spellCards += 1;

                    addScore(
                        item.getScoreValue()
                    );

                    System.out.println(
                        name
                            + " collected BOMB item! "
                            + "SpellCards: "
                            + spellCards
                    );
                }

                case LIFE -> {

                    this.hp += 20;

                    addScore(
                        item.getScoreValue()
                    );

                    System.out.println(
                        name
                            + " collected LIFE item! "
                            + "HP: "
                            + hp
                    );
                }
            }

        } else {

            addScore(
                item.getScoreValue()
            );

            System.out.println(
                name
                    + " collected "
                    + item.getItemType()
                    + "!"
            );
        }

        item.setCollected(true);
        item.destroy();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getHp() {
        return hp;
    }

    public void setHp(int hp) {
        this.hp = Math.max(0, hp);
    }

    public int getPower() {
        return power;
    }

    public void setPower(int power) {
        this.power = power;
    }

    public int getSpellCards() {
        return spellCards;
    }

    public void setSpellCards(int spellCards) {
        this.spellCards = spellCards;
    }

    public long getScore() {
        return score;
    }
}
