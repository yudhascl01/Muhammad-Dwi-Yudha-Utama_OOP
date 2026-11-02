package com.MuhammadDwiYudhaUtama.frontend.systems;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

import com.MuhammadDwiYudhaUtama.frontend.objects.BulletType;
import com.MuhammadDwiYudhaUtama.frontend.objects.GameObject;
import com.MuhammadDwiYudhaUtama.frontend.objects.bullets.Bullet;
import com.MuhammadDwiYudhaUtama.frontend.objects.items.ItemType;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class BulletManager {

    private final Queue<Bullet> playerBulletPool =
        new LinkedList<>();

    private final Queue<Bullet> enemyBulletPool =
        new LinkedList<>();

    private final List<Bullet> activePlayerBullets =
        new ArrayList<>();

    private final List<Bullet> activeEnemyBullets =
        new ArrayList<>();

    public Bullet spawnPlayerBullet(
        float x, float y, float vx, float vy, int damage
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

        activePlayerBullets.add(bullet);

        return bullet;
    }

    public Bullet spawnEnemyBullet(
        float x, float y, float vx, float vy, int damage
    ) {
        Bullet bullet = enemyBulletPool.poll();

        if (bullet == null) {
            bullet = EntityFactory.createEnemyBullet(
                x, y, damage
            );
        }

        bullet.init(
            x, y, vx, vy,
            BulletType.DANMAKU,
            damage,
            false
        );

        activeEnemyBullets.add(bullet);

        return bullet;
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

            if (bullet.isDestroyed()
                || bullet.isOffScreen(
                screenWidth,
                screenHeight
            )) {
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

    // Menghapus peluru musuh dan membuat item POINT
    public void clearEnemyBullets(List<GameObject> entities) {
        Iterator<Bullet> iterator =
            activeEnemyBullets.iterator();

        while (iterator.hasNext()) {
            Bullet bullet = iterator.next();

            if (entities != null) {
                entities.add(
                    EntityFactory.createItem(
                        bullet.getX(),
                        bullet.getY(),
                        ItemType.POINT
                    )
                );
            }

            bullet.destroy();
            iterator.remove();

            enemyBulletPool.offer(bullet);
        }
    }

    public void clearEnemyBullets() {
        clearEnemyBullets(null);
    }

    // Menghapus seluruh peluru pemain
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
}
