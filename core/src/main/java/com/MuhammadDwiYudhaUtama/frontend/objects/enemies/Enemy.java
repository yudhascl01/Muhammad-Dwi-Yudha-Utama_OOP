package com.MuhammadDwiYudhaUtama.frontend.objects.enemies;

import com.MuhammadDwiYudhaUtama.frontend.objects.GameObject;
import com.MuhammadDwiYudhaUtama.frontend.objects.Player;
import com.badlogic.gdx.graphics.Color;

public class Enemy extends GameObject {

    private String name;
    private int hp;
    private int maxHp;

    protected long scoreValue;

    public Enemy(
        String name,
        int hp
    ) {

        super(
            200,
            380,
            24,
            24,
            0,
            Color.PINK
        );

        this.name = name;
        this.hp = Math.max(0, hp);
        this.maxHp = Math.max(0, hp);
        this.scoreValue = 100L;

        if (this.hp <= 0) {
            destroy();
        }
    }

    public Enemy(
        float x,
        float y,
        float width,
        float height,
        Color color,
        String name,
        int hp,
        long scoreValue
    ) {

        super(
            x,
            y,
            width,
            height,
            0,
            color
        );

        this.name = name;
        this.hp = Math.max(0, hp);
        this.maxHp = Math.max(0, hp);
        this.scoreValue = scoreValue;

        if (this.hp <= 0) {
            destroy();
        }
    }

    public boolean takeDamage(
        int damage
    ) {

        if (!isAlive()) {
            return false;
        }

        if (damage <= 0) {
            return false;
        }

        boolean wasAlive =
            getHp() > 0;

        setHp(
            getHp() - damage
        );

        System.out.println(
            getName()
                + " took "
                + damage
                + " damage! HP: "
                + getHp()
                + "/"
                + getMaxHp()
        );

        if (
            wasAlive
                && getHp() == 0
        ) {

            destroy();

            System.out.println(
                getName()
                    + " was defeated!"
            );

            return true;
        }

        return false;
    }

    public void attack(
        Player player,
        int damage
    ) {

        if (!isAlive()) {
            return;
        }

        System.out.println(
            getName()
                + " unleashes bullet barrage on "
                + player.getName()
                + "!"
        );

        player.takeDamage(damage);
    }

    public boolean isAlive() {

        return getHp() > 0
            && !isDestroyed();
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

    public int getMaxHp() {
        return maxHp;
    }

    public long getScoreValue() {
        return scoreValue;
    }

    public void setScoreValue(
        long scoreValue
    ) {
        this.scoreValue = scoreValue;
    }
}
