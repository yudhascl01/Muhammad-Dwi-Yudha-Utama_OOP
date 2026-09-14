package com.MuhammadDwiYudhaUtama.frontend.objects;

import com.MuhammadDwiYudhaUtama.frontend.objects.enemies.Enemy;
import com.MuhammadDwiYudhaUtama.frontend.objects.items.Item;
import com.badlogic.gdx.graphics.Color;

public class Player extends GameObject {

    private String name;
    private int hp;
    private int power;
    private int spellCards;
    private long score;

    public Player(String name, int hp, int power, int spellCards) {
        super(280, 40, 32, 32, 0, Color.RED);

        this.name = name;
        this.hp = Math.max(0, hp);
        this.power = power;
        this.spellCards = spellCards;
        this.score = 0;
    }

    public Player(float x, float y, String name, int hp, int power, int spellCards) {
        super(x, y, 32, 32, 0, Color.RED);

        this.name = name;
        this.hp = Math.max(0, hp);
        this.power = power;
        this.spellCards = spellCards;
        this.score = 0;
    }

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

    public void addScore(long points) {
        if (points > 0) {
            this.score += points;

            System.out.println(
                getName() + " gained " + points
                    + " pts! Total Score: " + this.score
            );
        }
    }

    public void collectItem(Item item) {
        System.out.println(
            getName() + " collected " + item.getItemType() + "!"
        );

        if (item.getScoreValue() > 0) {
            addScore(item.getScoreValue());
        }
    }

    // Getter & Setter

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

    @Override
    public void onCollision(Collidable other) {
        // TODO: Cek apakah other yang diterima method ini adalah Item
        // TODO: Cetak "Player touches items" lalu panggil collectItem((Item) other)

        if(System.out.println(other instanceof Item)) {
            System.out.println("Player touches items");
            collectItem((Item) other);
        }

    }

}
