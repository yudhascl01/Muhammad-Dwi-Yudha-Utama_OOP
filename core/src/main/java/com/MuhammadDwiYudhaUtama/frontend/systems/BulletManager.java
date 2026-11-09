
package com.MuhammadDwiYudhaUtama.frontend.systems;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

import com.MuhammadDwiYudhaUtama.frontend.objects.BulletType;
import com.MuhammadDwiYudhaUtama.frontend.objects.GameObject;
import com.MuhammadDwiYudhaUtama.frontend.objects.bullets.Bullet;
import com.MuhammadDwiYudhaUtama.frontend.objects.items.Item;
import com.MuhammadDwiYudhaUtama.frontend.objects.items.ItemType;
import com.MuhammadDwiYudhaUtama.frontend.objects.patterns.bulletStrategy.HomingBulletMovement;


public class BulletManager {


    private List<GameObject> entities; // [TAMBAHAN] Tambahkan field untuk menampung daftar entitas arena

    private final Queue<Bullet> playerBulletPool =
        new LinkedList<>();

    private final Queue<Bullet> enemyBulletPool =
        new LinkedList<>();

    private final List<Bullet> activePlayerBullets =
        new ArrayList<>();

    private final List<Bullet> activeEnemyBullets =
        new ArrayList<>();

    public Bullet spawnPlayerBullet(
        float x,
        float y,
        float vx,
        float vy,
        int damage
    ) {
        Bullet bullet = playerBulletPool.poll();

        if (bullet == null) {
            bullet = EntityFactory.createPlayerBullet(
                x, y, damage
            );
        }

        bullet.init(
            x, y, vx, vy,
            BulletType.AMULET,
            damage,
            true
        );

        applyBulletSprite(
            bullet,
            null,
            "bullet_amulet"
        );

        activePlayerBullets.add(bullet);

        return bullet;
    }

    public Bullet spawnPlayerBullet(float x, float y, float vx, float vy, int damage, String spriteKey) { // Tambahin String spriteKey sebagai parameter
        String key = (spriteKey != null) ? spriteKey : "bullet_amulet"; // Tambahkan variabel String lokal key
        Bullet bullet;
        if (!playerBulletPool.isEmpty()) {
            bullet = playerBulletPool.poll();
            bullet.init(x, y, vx, vy, BulletType.AMULET, damage, true);
            bullet.setSprite(AssetManager.getInstance().getTextureRegion(key)); // Tambahkan setSprite menggunakan key
        } else {
            bullet = EntityFactory.createPlayerBullet(x, y, damage, key); // Tambahkan key sebagai argumen ke-4
            bullet.init(x, y, vx, vy, BulletType.AMULET, damage, true);
        }
        activePlayerBullets.add(bullet);
        return bullet;
    }

    public Bullet spawnPlayerBullet(float x, float y, float vx, float vy, int damage) {
        return spawnPlayerBullet(x, y, vx, vy, damage, "bullet_amulet");
    }

    // [UBAH] Perbarui method spawnEnemyBullet bawaan Modul 8 dengan perubahan berikut:
    public Bullet spawnEnemyBullet(float x, float y, float vx, float vy, int damage, String spriteKey) { // Tambahin String spriteKey sebagai parameter
        String key = (spriteKey != null) ? spriteKey : "bullet_danmaku"; // Tambahkan variabel String lokal key
        Bullet bullet;
        if (!enemyBulletPool.isEmpty()) {
            bullet = enemyBulletPool.poll();
            bullet.init(x, y, vx, vy, BulletType.DANMAKU, damage, false);
            bullet.setSprite(AssetManager.getInstance().getTextureRegion(key)); // Tambahkan setSprite menggunakan key
        } else {
            bullet = EntityFactory.createEnemyBullet(x, y, damage, key); // Tambahkan key sebagai argumen ke-4
            bullet.init(x, y, vx, vy, BulletType.DANMAKU, damage, false);
        }
        activeEnemyBullets.add(bullet);
        return bullet;
    }

    // [TAMBAHAN] Tambahkan method overload 5 parameter berikut (mendelegasikan ke method 6 parameter di atas):
    public Bullet spawnEnemyBullet(float x, float y, float vx, float vy, int damage) {
        return spawnEnemyBullet(x, y, vx, vy, damage, "bullet_danmaku");
    }

    private void applyBulletSprite(
        Bullet bullet,
        String spriteKey,
        String defaultSpriteKey
    ) {
        if (bullet == null) {
            return;
        }

        String key = spriteKey != null
            ? spriteKey
            : defaultSpriteKey;

        TextureRegion sprite =
            AssetManager.getInstance()
                .getTextureRegion(key);

        if (sprite != null) {
            bullet.setSprite(sprite);
        }
    }

    public void update(float delta, float screenWidth, float screenHeight) {
        Iterator<Bullet> pIter = activePlayerBullets.iterator();
        while (pIter.hasNext()) {
            Bullet bullet = pIter.next();

            // [TAMBAHAN] Sisipkan pengecekan ini tepat sebelum bullet.update(delta):
            if (entities != null && bullet.getMovementPattern() instanceof HomingBulletMovement homing && homing.getEntities() == null) {
                homing.setEntities(entities);
            }

            bullet.update(delta);
            ...
        }
        ...
    }

    public void update(
        float delta,
        float screenWidth,
        float screenHeight
    ) {
        updateBulletList(
            activePlayerBullets,
            playerBulletPool,
            delta,
            screenWidth,
            screenHeight
        );

        updateBulletList(
            activeEnemyBullets,
            enemyBulletPool,
            delta,
            screenWidth,
            screenHeight
        );
    }

    private void updateBulletList(
        List<Bullet> activeBullets,
        Queue<Bullet> pool,
        float delta,
        float screenWidth,
        float screenHeight
    ) {
        Iterator<Bullet> iterator =
            activeBullets.iterator();

        while (iterator.hasNext()) {
            Bullet bullet = iterator.next();

            if (!bullet.isDestroyed()) {
                bullet.update(delta);
            }

            if (
                bullet.isDestroyed() ||
                    bullet.isOffScreen(
                        screenWidth,
                        screenHeight
                    )
            ) {
                bullet.destroy();
                iterator.remove();
                pool.offer(bullet);
            }
        }
    }

    public void recycleInactiveBullets() {
        recycleList(
            activePlayerBullets,
            playerBulletPool
        );

        recycleList(
            activeEnemyBullets,
            enemyBulletPool
        );
    }

    private void recycleList(
        List<Bullet> activeBullets,
        Queue<Bullet> pool
    ) {
        Iterator<Bullet> iterator =
            activeBullets.iterator();

        while (iterator.hasNext()) {
            Bullet bullet = iterator.next();

            if (bullet.isDestroyed()) {
                iterator.remove();
                pool.offer(bullet);
            }
        }
    }

    public void render(SpriteBatch batch) {
        for (Bullet bullet : activePlayerBullets) {
            if (!bullet.isDestroyed()) {
                bullet.render(batch);
            }
        }

        for (Bullet bullet : activeEnemyBullets) {
            if (!bullet.isDestroyed()) {
                bullet.render(batch);
            }
        }
    }

    public void clearEnemyBullets(
        List<GameObject> entities
    ) {
        Iterator<Bullet> iterator =
            activeEnemyBullets.iterator();

        while (iterator.hasNext()) {
            Bullet bullet = iterator.next();

            if (entities != null) {
                Item pointItem =
                    EntityFactory.createItem(
                        bullet.getX(),
                        bullet.getY(),
                        ItemType.POINT
                    );

                entities.add(pointItem);
            }

            bullet.destroy();
            iterator.remove();

            enemyBulletPool.offer(bullet);
        }
    }

    public void clearEnemyBullets() {
        clearEnemyBullets(null);
    }

    public void clearPlayerBullets() {
        Iterator<Bullet> iterator =
            activePlayerBullets.iterator();

        while (iterator.hasNext()) {
            Bullet bullet = iterator.next();

            bullet.destroy();
            iterator.remove();

            playerBulletPool.offer(bullet);
        }
    }

    public List<Bullet> getActivePlayerBullets() {
        return activePlayerBullets;
    }

    public List<Bullet> getActiveEnemyBullets() {
        return activeEnemyBullets;
    }

    public int getPlayerPoolSize() {
        return playerBulletPool.size();
    }

    public int getEnemyPoolSize() {
        return enemyBulletPool.size();
    }

    public List<GameObject> getEntities() { return entities; }
    public void setEntities(List<GameObject> entities) { this.entities = entities; }
}

