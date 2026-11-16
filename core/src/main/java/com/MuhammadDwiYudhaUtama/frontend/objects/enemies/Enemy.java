package com.MuhammadDwiYudhaUtama.frontend.objects.enemies;

import com.MuhammadDwiYudhaUtama.frontend.objects.EntityShooter;
import com.MuhammadDwiYudhaUtama.frontend.objects.Player;
import com.MuhammadDwiYudhaUtama.frontend.objects.items.ItemType;
import com.MuhammadDwiYudhaUtama.frontend.objects.patterns.ShootingPattern;
import com.MuhammadDwiYudhaUtama.frontend.objects.patterns.entityStrategy.EntityMovementPattern;
import com.MuhammadDwiYudhaUtama.frontend.systems.AudioSystem;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Rectangle;

public class Enemy extends EntityShooter {

    private String name;
    protected int hp;
    protected int maxHp;

    protected long scoreValue;

    protected ItemType itemDrop = null;
    protected boolean damageable = true;
    protected boolean collidable = true;

    protected float shotHitboxWidth = 0f;
    protected float shotHitboxHeight = 0f;

    public Enemy(String name, int hp) {
        super(200, 380, 24, 24, 0, Color.PINK);

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
        super(x, y, width, height, 0, color);

        this.name = name;
        this.hp = Math.max(0, hp);
        this.maxHp = Math.max(0, hp);
        this.scoreValue = scoreValue;

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
        long scoreValue,
        ShootingPattern shootingPattern,
        EntityMovementPattern movementPattern
    ) {
        super(
            x, y, width, height, 0, color,
            shootingPattern, movementPattern
        );

        this.name = name;
        this.hp = Math.max(0, hp);
        this.maxHp = Math.max(0, hp);
        this.scoreValue = scoreValue;

        if (this.hp <= 0) {
            destroy();
        }
    }

    public boolean takeDamage(int damage) {
        if (!damageable) {
            return false;
        }

        if (!isAlive() || damage <= 0) {
            return false;
        }

        boolean wasAlive = getHp() > 0;

        setHp(getHp() - damage);

        System.out.println(
            getName() + " took " + damage
                + " damage! HP: " + getHp()
                + "/" + getMaxHp()
        );

        if (wasAlive && getHp() == 0) {
            AudioSystem.getInstance().playEnemyDeath();

            destroy();

            System.out.println(
                getName() + " was defeated!"
            );

            return true;
        }

        return false;
    }

    public void attack(Player player, int damage) {
        if (!isAlive()) {
            return;
        }

        System.out.println(
            getName() + " unleashes bullet barrage on "
                + player.getName() + "!"
        );

        player.takeDamage(damage);
    }

    public boolean isAlive() {
        return getHp() > 0 && !isDestroyed();
    }

    public ItemType getItemDrop() {
        return itemDrop;
    }

    public void setItemDrop(ItemType itemDrop) {
        this.itemDrop = itemDrop;
    }

    public boolean isDamageable() {
        return damageable;
    }

    public void setDamageable(boolean damageable) {
        this.damageable = damageable;
    }

    public boolean isCollidable() {
        return collidable;
    }

    public void setCollidable(boolean collidable) {
        this.collidable = collidable;
    }

    public Rectangle getShotHitbox() {
        if (shotHitboxWidth <= 0f || shotHitboxHeight <= 0f) {
            return getCoreHitbox();
        }

        return new Rectangle(
            x + (width - shotHitboxWidth) / 2f,
            y + (height - shotHitboxHeight) / 2f,
            shotHitboxWidth,
            shotHitboxHeight
        );
    }

    public void setShotHitboxSize(float width, float height) {
        this.shotHitboxWidth = width;
        this.shotHitboxHeight = height;
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

    public void setScoreValue(long scoreValue) {
        this.scoreValue = scoreValue;
    }
}
