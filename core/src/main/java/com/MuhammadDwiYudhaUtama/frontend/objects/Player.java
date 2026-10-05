package com.MuhammadDwiYudhaUtama.frontend.objects;

import com.MuhammadDwiYudhaUtama.frontend.objects.bullets.Bullet;
import com.MuhammadDwiYudhaUtama.frontend.objects.enemies.Boss;
import com.MuhammadDwiYudhaUtama.frontend.objects.enemies.Enemy;
import com.MuhammadDwiYudhaUtama.frontend.objects.enemies.Fairy;
import com.MuhammadDwiYudhaUtama.frontend.objects.items.Item;
import com.MuhammadDwiYudhaUtama.frontend.systems.AssetManager;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class Player extends GameObject {

    private String name;
    private int hp;
    private int power;
    private int spellCards;
    private int score;

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
        float dy = 0;

        if (
            Gdx.input.isKeyPressed(Input.Keys.W)
                || Gdx.input.isKeyPressed(Input.Keys.UP)
        ) {
            dy += speed * delta;
        }

        if (
            Gdx.input.isKeyPressed(Input.Keys.S)
                || Gdx.input.isKeyPressed(Input.Keys.DOWN)
        ) {
            dy -= speed * delta;
        }

        if (
            Gdx.input.isKeyPressed(Input.Keys.A)
                || Gdx.input.isKeyPressed(Input.Keys.LEFT)
        ) {
            dx -= speed * delta;
        }

        if (
            Gdx.input.isKeyPressed(Input.Keys.D)
                || Gdx.input.isKeyPressed(Input.Keys.RIGHT)
        ) {
            dx += speed * delta;
        }

        x += dx;
        y += dy;

        if (x < 0) {
            x = 0;
        }

        if (x + width > Gdx.graphics.getWidth()) {
            x = Gdx.graphics.getWidth() - width;
        }

        if (y < 0) {
            y = 0;
        }

        if (y + height > Gdx.graphics.getHeight()) {
            y = Gdx.graphics.getHeight() - height;
        }
    }

    public void updateAnimationState(float dx) {
        AssetManager assets = AssetManager.getInstance();
        if (dx < 0) {
            // TODO:
            // 1. Lanjutkan perubahan hanya jika currentDir bukan -1.
            // 2. Ubah currentDir menjadi -1.
            // 3. Ambil animasi "player_left" melalui assets.getAnimation(...).
            //    Simpan pada variabel lokal bertipe Animation<TextureRegion> bernama anim.
            // 4. Jika anim tidak null, pasang anim melalui setAnimation(...).
        } else if (dx > 0) {
            // TODO:
            // 1. Lanjutkan perubahan hanya jika currentDir bukan 1.
            // 2. Ubah currentDir menjadi 1.
            // 3. Ambil animasi "player_right" melalui assets.getAnimation(...).
            //    Simpan pada variabel lokal bertipe Animation<TextureRegion> bernama anim.
            // 4. Jika anim tidak null, pasang anim melalui setAnimation(...).
        } else {
            // TODO:
            // 1. Lanjutkan perubahan hanya jika currentDir bukan 0.
            // 2. Ubah currentDir menjadi 0.
            // 3. Ambil animasi "player_idle" melalui assets.getAnimation(...).
            //    Simpan pada variabel lokal bertipe Animation<TextureRegion> bernama anim.
            // 4. Jika anim tidak null, pasang anim melalui setAnimation(...).
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

        Bullet bullet =
            new Bullet(
                x + width / 2 - 4,
                y + height,
                BulletType.AMULET,
                damage
            );

        TextureRegion sprite =
            AssetManager.getInstance()
                .getTextureRegion("bullet_amulet");

        bullet.setSprite(sprite);

        return bullet;
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

        if (item.isDestroyed()) {
            return;
        }

        String type = item.getItemType();

        switch (type) {

            case "POWER":
                power++;

                System.out.println(
                    name
                        + " collected POWER item! Power increased to "
                        + power
                );
                break;

            case "POINT":
                score += 100;

                System.out.println(
                    name
                        + " collected POINT item! Score increased to "
                        + score
                );
                break;

            case "BOMB":
                spellCards++;

                System.out.println(
                    name
                        + " collected BOMB item! Spell Cards increased to "
                        + spellCards
                );
                break;

            case "LIFE":
                hp++;

                System.out.println(
                    name
                        + " collected LIFE item! HP increased to "
                        + hp
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

        System.out.println(
            name
                + " took "
                + damage
                + " damage! HP: "
                + hp
        );

        if (hp <= 0) {

            destroy();

            System.out.println(
                name
                    + " was defeated!"
            );
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
        this.power = Math.max(0, power);
    }

    public int getSpellCards() {
        return spellCards;
    }

    public void setSpellCards(int spellCards) {
        this.spellCards = Math.max(0, spellCards);
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = Math.max(0, score);
    }
}
