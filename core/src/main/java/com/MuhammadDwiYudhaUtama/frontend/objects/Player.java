package com.MuhammadDwiYudhaUtama.frontend.objects;

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

    public Player(String name, int hp, int power, int spellCards) {
        super(280, 40, 32, 32, 250f, Color.RED);

        this.name = name;
        this.hp = Math.max(0, hp);
        this.power = power;
        this.spellCards = spellCards;
        this.score = 0;
    }

    public Player(float x, float y, String name, int hp, int power, int spellCards) {
        super(x, y, 32, 32, 250f, Color.RED);

        this.name = name;
        this.hp = Math.max(0, hp);
        this.power = power;
        this.spellCards = spellCards;
        this.score = 0;
    }

    // ==========================================
    // PART I - REAL-TIME PLAYER MOVEMENT
    // ==========================================

    @Override
    public void update(float delta) {
        if (Gdx.input != null) {

            // W / UP -> Move Up
            if (Gdx.input.isKeyPressed(Input.Keys.W)
                || Gdx.input.isKeyPressed(Input.Keys.UP)) {
                y += speed * delta;
            }

            // S / DOWN -> Move Down
            if (Gdx.input.isKeyPressed(Input.Keys.S)
                || Gdx.input.isKeyPressed(Input.Keys.DOWN)) {
                y -= speed * delta;
            }

            // A / LEFT -> Move Left
            if (Gdx.input.isKeyPressed(Input.Keys.A)
                || Gdx.input.isKeyPressed(Input.Keys.LEFT)) {
                x -= speed * delta;
            }

            // D / RIGHT -> Move Right
            if (Gdx.input.isKeyPressed(Input.Keys.D)
                || Gdx.input.isKeyPressed(Input.Keys.RIGHT)) {
                x += speed * delta;
            }
        }
    }

    // ==========================================
    // PART II - COLLISION
    // ==========================================

    @Override
    public void onCollision(Collidable other) {
        if (other instanceof Item) {
            Item item = (Item) other;

            if (!item.isCollected()) {
                System.out.println("Player touches items");
                collectItem(item);
            }
        }
    }

    // ==========================================
    // DAMAGE & COMBAT
    // ==========================================

    public void takeDamage(int damage) {
        setHp(getHp() - damage);

        if (getHp() > 0) {
            System.out.println(
                getName() + " took " + damage
                    + " damage! Remaining HP: " + getHp()
            );
        } else {
            System.out.println(
                getName() + " took " + damage
                    + " damage! Remaining HP: 0"
            );

            System.out.println(
                getName() + " was defeated (Pichuun~)!"
            );
        }
    }

    public void shoot(Enemy target) {
        int damage = 10 + getPower();

        System.out.println(
            getName() + " shoots " + target.getName()
                + " dealing " + damage + " DMG!"
        );

        boolean defeated = target.takeDamage(damage);

        if (defeated) {
            addScore(target.getScoreValue());
        }
    }

    public boolean isAlive() {
        return getHp() > 0;
    }

    // ==========================================
    // SCORE SYSTEM
    // ==========================================

    public void addScore(long points) {
        if (points > 0) {
            this.score += points;

            System.out.println(
                getName() + " gained " + points
                    + " pts! Total Score: " + this.score
            );
        }
    }

    // ==========================================
    // PART III - COLLECT ITEM
    // ==========================================

    public void collectItem(Item item) {

        if (item == null || item.isCollected()) {
            return;
        }

        ItemType type = item.getItemTypeEnum();

        if (type != null) {

            switch (type) {

                case POWER -> {
                    this.power += type.getPowerBonus();

                    addScore(item.getScoreValue());

                    System.out.println(
                        name + " collected POWER item! "
                            + "Power increased to " + power
                    );
                }

                case POINT -> {
                    addScore(item.getScoreValue());

                    System.out.println(
                        name + " collected POINT item!"
                    );
                }

                case BOMB -> {
                    this.spellCards += 1;

                    addScore(item.getScoreValue());

                    System.out.println(
                        name + " collected BOMB item! "
                            + "SpellCards: " + spellCards
                    );
                }

                case LIFE -> {
                    this.hp += 20;

                    addScore(item.getScoreValue());

                    System.out.println(
                        name + " collected LIFE item! "
                            + "HP: " + hp
                    );
                }
            }

        } else {

            addScore(item.getScoreValue());

            System.out.println(
                name + " collected " + item.getItemType() + "!"
            );
        }

        // Mark item as collected.
        item.setCollected(true);
    }

    // ==========================================
    // GETTER & SETTER
    // ==========================================

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
