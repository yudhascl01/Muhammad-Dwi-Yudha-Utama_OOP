
package com.MuhammadDwiYudhaUtama.frontend.systems;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

import com.MuhammadDwiYudhaUtama.frontend.objects.BulletType;
import com.MuhammadDwiYudhaUtama.frontend.objects.bullets.Bullet;
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

    /**
     * Meminjam atau membuat bullet pemain.
     */
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
                x,
                y,
                damage
            );
        }

        bullet.init(
            x,
            y,
            vx,
            vy,
            BulletType.AMULET,
            damage,
            true
        );

        activePlayerBullets.add(bullet);

        return bullet;
    }

    /**
     * Meminjam atau membuat bullet musuh.
     */
    public Bullet spawnEnemyBullet(
        float x,
        float y,
        float vx,
        float vy,
        int damage
    ) {
        Bullet bullet = enemyBulletPool.poll();

        if (bullet == null) {
            bullet = EntityFactory.createEnemyBullet(
                x,
                y,
                damage
            );
        }

        bullet.init(
            x,
            y,
            vx,
            vy,
            BulletType.DANMAKU,
            damage,
            false
        );

        activeEnemyBullets.add(bullet);

        return bullet;
    }

    /**
     * Memperbarui posisi bullet dan mendaur ulang
     * bullet yang hancur atau keluar dari layar.
     */
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
                bullet.isDestroyed()
                    || bullet.isOffScreen(
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

    /**
     * Mendaur ulang bullet yang dihancurkan oleh
     * CollisionReferee setelah proses collision.
     */
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

    /**
     * Merender semua bullet aktif.
     * Method ini dipanggil di antara batch.begin()
     * dan batch.end().
     */
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
