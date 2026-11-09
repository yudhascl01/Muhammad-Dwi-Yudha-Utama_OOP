
package com.MuhammadDwiYudhaUtama.frontend.objects;

import java.util.ArrayList;
import java.util.List;

import com.MuhammadDwiYudhaUtama.frontend.objects.bullets.Bullet;
import com.MuhammadDwiYudhaUtama.frontend.objects.enemies.Boss;
import com.MuhammadDwiYudhaUtama.frontend.objects.enemies.Enemy;
import com.MuhammadDwiYudhaUtama.frontend.objects.enemies.Fairy;
import com.MuhammadDwiYudhaUtama.frontend.objects.items.Item;
import com.MuhammadDwiYudhaUtama.frontend.objects.items.ItemType;
import com.MuhammadDwiYudhaUtama.frontend.objects.patterns.ShootingPattern;
import com.MuhammadDwiYudhaUtama.frontend.observers.GameObserver;
import com.MuhammadDwiYudhaUtama.frontend.systems.AssetManager;
import com.MuhammadDwiYudhaUtama.frontend.systems.AudioSystem;
import com.MuhammadDwiYudhaUtama.frontend.systems.BulletManager;
import com.MuhammadDwiYudhaUtama.frontend.systems.EntityFactory;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Rectangle;

public class Player extends EntityShooter {

    private String name;

    private int hp;
    private int lives = 3;
    private int power;
    private int spellCards;
    private long score;

    private int grazeCount = 0;
    private boolean focused = false;
    private float shootTimer = 0.1f;
    private float shootCooldown = 0.1f;
    private float invulnerableTimer = 0f;

    private int currentDir = 0;

    private List<GameObserver> observers = new ArrayList<>();

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
        this.score = 0L;
    }

    public Player(
        String name,
        int hp,
        int power,
        int spellCards
    ) {
        this(0, 0, name, hp, power, spellCards);
    }

    @Override
    public void update(float delta) {
        super.update(delta);

        shootTimer += delta;

        if (invulnerableTimer > 0f) {
            invulnerableTimer -= delta;

            if (invulnerableTimer < 0f) {
                invulnerableTimer = 0f;
            }
        }
    }

    // Pergerakan pemain
    public void move(float dx, float dy) {
        this.x += dx * speed;
        this.y += dy * speed;

        clampToPlayfield();
    }

    private void clampToPlayfield() {
        float minX = 32f;
        float maxX = 416f - width;
        float minY = 16f;
        float maxY = 560f - height;

        x = Math.max(minX, Math.min(x, maxX));
        y = Math.max(minY, Math.min(y, maxY));
    }

    // Mengubah animasi pemain
    public void updateAnimationState(float dx) {
        AssetManager assets = AssetManager.getInstance();

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

    // Metode menembak lama
    public Bullet shootBullet() {
        int damage = 10 + power;

        AudioSystem.getInstance().playBulletShot();

        System.out.println(
            name + " shoots bullet dealing " + damage + " DMG!"
        );

        return EntityFactory.createPlayerBullet(
            x + width / 2f - 8f,
            y + height,
            damage
        );
    }

    // Menembak menggunakan cooldown dan Shooting Pattern
    @Override
    public void shootBullet(BulletManager bulletManager) {
        if (
            bulletManager == null ||
                isDestroyed() ||
                !active
        ) {
            return;
        }

        if (shootTimer >= shootCooldown) {
            float originX = x + width / 2f - 8f;
            float originY = y + height;

            AudioSystem.getInstance().playBulletShot();

            ShootingPattern pattern = getShootingPattern();

            if (pattern != null) {
                pattern.execute(
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
                    10 + power
                );
            }

            shootTimer = 0f;
        }
    }

    // Spell Card / Bomb
    public void useBomb(
        BulletManager bulletManager,
        List<GameObject> entities
    ) {
        if (spellCards > 0) {
            spellCards--;
            notifySpellCardsChanged();

            invulnerableTimer = 5.0f;

            if (bulletManager != null) {
                bulletManager.clearEnemyBullets(entities);
            }

            System.out.println(
                name + " unleashes SPELL CARD (Fantasy Seal)! "
                    + "All enemy bullets cleared!"
            );
        } else {
            System.out.println(
                "No Spell Cards (Bombs) remaining!"
            );
        }
    }

    public void useBomb(BulletManager bulletManager) {
        useBomb(bulletManager, null);
    }

    // Menambahkan graze
    public void addGraze() {
        this.grazeCount++;

        addScore(50);

        notifyGrazeChanged();
    }

    @Override
    public Rectangle getCoreHitbox() {
        return new Rectangle(
            x + width / 2f - 4f,
            y + height / 2f - 4f,
            8f,
            8f
        );
    }

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
        if (enemy == null || enemy.isDestroyed()) {
            return;
        }

        int damage = 10 + power;

        System.out.println(
            name + " shoots " + enemy.getName()
                + " for " + damage + " DMG!"
        );

        enemy.takeDamage(damage);
    }

    public void shoot(Fairy fairy) {
        shoot((Enemy) fairy);
    }

    public void shoot(Boss boss) {
        shoot((Enemy) boss);
    }

    // Mengambil item
    public void collectItem(Item item) {
        if (item == null || item.isDestroyed()) {
            return;
        }

        ItemType type = item.getItemTypeEnum();

        if (type != null) {
            switch (type) {
                case POWER -> {
                    power += type.getPowerBonus();
                    notifyPowerChanged();

                    addScore((int) item.getScoreValue());

                    System.out.println(
                        name + " collected POWER item! "
                            + "Power increased to " + power
                    );
                }

                case POINT -> {
                    addScore((int) item.getScoreValue());

                    System.out.println(
                        name + " collected POINT item!"
                    );
                }

                case BOMB -> {
                    spellCards++;
                    notifySpellCardsChanged();

                    addScore((int) item.getScoreValue());

                    System.out.println(
                        name + " collected BOMB item! "
                            + "SpellCards: " + spellCards
                    );
                }

                case LIFE -> {
                    lives++;
                    hp = 100;

                    notifyLivesChanged();
                    notifyHpChanged();

                    addScore((int) item.getScoreValue());

                    System.out.println(
                        name + " collected LIFE item! "
                            + "Remaining Lives: " + lives
                    );
                }
            }
        } else {
            addScore((int) item.getScoreValue());

            System.out.println(
                name + " collected " + item.getItemType() + "!"
            );
        }

        AudioSystem.getInstance().playItemPickup();
        item.destroy();
    }

    // Sistem damage dan nyawa
    public void takeDamage(int damage) {
        takeDamage(damage, null, null);
    }

    public void takeDamage(
        int damage,
        BulletManager bulletManager,
        List<GameObject> entities
    ) {
        if (damage <= 0 || isInvulnerable() || isDestroyed()) {
            if (isInvulnerable()) {
                System.out.println(
                    name + " is INVULNERABLE! (0 damage taken)"
                );
            }

            return;
        }

        hp -= damage;

        if (hp < 0) {
            hp = 0;
        }

        notifyHpChanged();

        System.out.println(
            name + " took " + damage
                + " damage! Remaining HP: " + hp
        );

        if (hp == 0) {
            System.out.println(
                name + " was defeated (Pichuun~)! "
            );

            loseLife(bulletManager, entities);
        }
    }

    public void loseLife(
        BulletManager bulletManager,
        List<GameObject> entities
    ) {
        lives--;
        notifyLivesChanged();

        if (bulletManager != null) {
            bulletManager.clearEnemyBullets();
        }

        if (lives > 0) {
            if (bulletManager != null) {
                System.out.println(
                    "[Player] " + name + " lost a life! "
                        + "Remaining Lives: " + lives
                        + ". Respawning at (200, 50)..."
                );
            }

            respawn();
        } else {
            if (bulletManager != null) {
                System.out.println(
                    "[Player] GAME OVER! " + name
                        + " has run out of lives!"
                );
            }

            destroy();
        }
    }

    public void respawn() {
        active = true;
        x = 200;
        y = 50;
        hp = 100;

        invulnerableTimer = 3.0f;

        power = Math.max(10, power - 16);
        spellCards = 3;

        notifyHpChanged();
        notifyPowerChanged();
        notifySpellCardsChanged();
    }

    public void resetGame() {
        lives = 3;
        hp = 100;
        power = 15;
        spellCards = 3;
        score = 0L;
        grazeCount = 0;

        active = true;
        x = 200;
        y = 50;

        invulnerableTimer = 3.0f;
        shootTimer = 0.1f;

        notifyLivesChanged();
        notifyHpChanged();
        notifyPowerChanged();
        notifySpellCardsChanged();
        notifyScoreChanged();
        notifyGrazeChanged();
    }

    public boolean isAlive() {
        return hp > 0 && lives > 0 && !isDestroyed();
    }

    public boolean isGameOver() {
        return lives <= 0;
    }

    public boolean isInvulnerable() {
        return invulnerableTimer > 0f;
    }

    public float getInvulnerableTimer() {
        return invulnerableTimer;
    }

    // Render pemain
    @Override
    public void render(SpriteBatch batch) {
        if (batch != null && active && !isDestroyed()) {
            if (invulnerableTimer > 0f) {
                boolean visible =
                    ((int) (invulnerableTimer * 16)) % 2 == 0;

                if (!visible) {
                    return;
                }
            }

            if (focused) {
                batch.setColor(1f, 1f, 1f, 0.5f);
            }

            super.render(batch);

            if (focused) {
                batch.setColor(1f, 1f, 1f, 1f);
            }
        }
    }

    // Indikator Core Hitbox
    public void renderFocusIndicator(ShapeRenderer shapeRenderer) {
        if (focused && shapeRenderer != null && active) {
            Rectangle core = getCoreHitbox();

            shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);

            shapeRenderer.setColor(Color.RED);

            shapeRenderer.circle(
                core.x + core.width / 2f,
                core.y + core.height / 2f,
                4f
            );

            shapeRenderer.setColor(Color.WHITE);

            shapeRenderer.circle(
                core.x + core.width / 2f,
                core.y + core.height / 2f,
                2f
            );

            shapeRenderer.end();
        }
    }

    @Override
    public void onCollision(Collidable other) {
        if (other instanceof Item item) {
            collectItem(item);
        }
    }

    // Getter dan setter
    public String getName() {
        return name;
    }

    public int getHp() {
        return hp;
    }

    public int getLives() {
        return lives;
    }

    public void setLives(int lives) {
        this.lives = lives;
        notifyLivesChanged();
    }

    public int getPower() {
        return power;
    }

    public int getSpellCards() {
        return spellCards;
    }

    public long getScore() {
        return score;
    }

    public int getGrazeCount() {
        return grazeCount;
    }

    public boolean isFocused() {
        return focused;
    }

    public void setFocused(boolean focused) {
        this.focused = focused;
    }

    public void setHp(int hp) {
        this.hp = hp;
        notifyHpChanged();
    }

    public void setPower(int power) {
        this.power = power;
        notifyPowerChanged();
    }

    public void setSpellCards(int spellCards) {
        this.spellCards = spellCards;
        notifySpellCardsChanged();
    }

    public void setScore(long score) {
        this.score = score;
        notifyScoreChanged();
    }

    public void addScore(int points) {
        if (points > 0) {
            score += points;
            notifyScoreChanged();
        }
    }

    // Registrasi & Penghapusan Observer
    public void registerObserver(GameObserver observer) {
        if (observer != null && !observers.contains(observer)) {
            observers.add(observer);

            observer.onScoreChanged(score);
            observer.onHpChanged(hp);
            observer.onLivesChanged(lives);
            observer.onSpellCardsChanged(spellCards);
            observer.onPowerChanged(power);
            observer.onGrazeChanged(grazeCount);
        }
    }

    public void removeObserver(GameObserver observer) {
        observers.remove(observer);
    }

    // Metode Pemancar Notifikasi ke Seluruh Observer
    private void notifyScoreChanged() {
        for (GameObserver obs : observers) {
            obs.onScoreChanged(score);
        }
    }

    private void notifyHpChanged() {
        for (GameObserver obs : observers) {
            obs.onHpChanged(hp);
        }
    }

    private void notifyLivesChanged() {
        for (GameObserver obs : observers) {
            obs.onLivesChanged(lives);
        }
    }

    private void notifySpellCardsChanged() {
        for (GameObserver obs : observers) {
            obs.onSpellCardsChanged(spellCards);
        }
    }

    private void notifyPowerChanged() {
        for (GameObserver obs : observers) {
            obs.onPowerChanged(power);
        }
    }

    private void notifyGrazeChanged() {
        for (GameObserver obs : observers) {
            obs.onGrazeChanged(grazeCount);
        }
    }
}
