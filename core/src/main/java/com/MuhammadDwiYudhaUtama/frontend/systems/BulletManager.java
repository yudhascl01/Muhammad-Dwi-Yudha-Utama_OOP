
package com.MuhammadDwiYudhaUtama.frontend.systems;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

import com.MuhammadDwiYudhaUtama.frontend.objects.BulletType;
import com.MuhammadDwiYudhaUtama.frontend.objects.bullets.Bullet;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class  BulletManager {

    private Queue<Bullet> playerBulletPool;
    private Queue<Bullet> enemyBulletPool;

    private List<Bullet> activePlayerBullets;
    private List<Bullet> activeEnemyBullets;

    public BulletManager() {
        playerBulletPool = new LinkedList<>();
        enemyBulletPool = new LinkedList<>();

        activePlayerBullets = new ArrayList<>();
        activeEnemyBullets = new ArrayList<>();
    }

    // Peminjaman bullet Pemain dari Pool (5 poin)
    public Bullet spawnPlayerBullet(float x, float y, float vx, float vy, int damage) {
        Bullet bullet;
        // TODO:
        // 1. Cek apakah playerBulletPool tidak kosong.
        //    Kalau ya, poll dari pool. Kalau tidak, buat baru menggunakan Factory.
        bullet = playerBulletPool.poll();

        if (bullet == null) {
            bullet = new Bullet(
                x,
                y,
                BulletType.AMULET,
                damage
            );
        }


        // 2. Re-initialize bullet.
        bullet.init(
            x,
            y,
            vx,
            vy,
            BulletType.AMULET,
            damage,
            true
        );

        // 3. Tambahkan bullet ke dalam daftar activePlayerBullets.
        activePlayerBullets.add(bullet);

        // 4. Kembalikan instance bullet tersebut.
        return bullet;
    }

    // Peminjaman bullet Musuh dari Pool (5 poin)
    public Bullet spawnEnemyBullet(float x, float y, float vx, float vy, int damage) {
        Bullet bullet;
        // TODO:
        // 1. Cek apakah enemyBulletPool tidak kosong:
        //    Kalau ya, poll dari pool. Kalau tidak, buat baru menggunakan Factory.

        bullet = enemyBulletPool.poll();

        if (bullet == null) {
            bullet = new Bullet(
                x,
                y,
                BulletType.DANMAKU,
                damage
            );
        }

        // 2. Re-initialize bullet.
        bullet.init(
            x,
            y,
            vx,
            vy,
            BulletType.DANMAKU,
            damage,
            false
        );
        // 3. Tambahkan bullet ke dalam daftar activeEnemyBullets
        activeEnemyBullets.add(bullet);
        // 4. Kembalikan instance bullet tersebut
        return bullet;
    }



    // Memindahkan peluru nonaktif kembali ke pool.
    public void update(float delta, float screenWidth, float screenHeight) {
        Iterator<Bullet> playerIterator =
            activePlayerBullets.iterator();

        while (playerIterator.hasNext()) {
            Bullet bullet = playerIterator.next();

            if (bullet.isDestroyed()) {
                playerIterator.remove();
                playerBulletPool.offer(bullet);
            }
        }

        Iterator<Bullet> enemyIterator =
            activeEnemyBullets.iterator();

        while (enemyIterator.hasNext()) {
            Bullet bullet = enemyIterator.next();

            if (bullet.isDestroyed()) {
                enemyIterator.remove();
                enemyBulletPool.offer(bullet);
            }
        }
    }

    public void render(SpriteBatch batch) {
        for (Bullet b : activePlayerBullets) {
            if (!b.isDestroyed()) b.render(batch);
        }
        for (Bullet b : activeEnemyBullets) {
            if (!b.isDestroyed()) b.render(batch);
        }
    }


    public List<Bullet> getActivePlayerBullets() {
        return activePlayerBullets;
    }

    public static List<Bullet> getActiveEnemyBullets() {
        return activeEnemyBullets;
    }

    public int getPlayerPoolSize() {
        return playerBulletPool.size();
    }

    public int getEnemyPoolSize() {
        return enemyBulletPool.size();
    }
}
