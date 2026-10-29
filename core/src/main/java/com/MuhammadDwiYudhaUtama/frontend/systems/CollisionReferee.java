
package com.MuhammadDwiYudhaUtama.frontend.systems;

import java.util.ArrayList;
import java.util.List;

import com.MuhammadDwiYudhaUtama.frontend.objects.GameObject;
import com.MuhammadDwiYudhaUtama.frontend.objects.Player;
import com.MuhammadDwiYudhaUtama.frontend.objects.bullets.Bullet;
import com.MuhammadDwiYudhaUtama.frontend.objects.enemies.Enemy;
import com.MuhammadDwiYudhaUtama.frontend.objects.items.Item;
import com.MuhammadDwiYudhaUtama.frontend.objects.items.ItemType;

public class CollisionReferee {

    public void resolveCollisions(
        Player player,
        List<GameObject> entities,
        BulletManager bulletManager
    ) {
        if (
            player == null
                || entities == null
                || bulletManager == null
        ) {
            return;
        }

        List<GameObject> newDrops =
            new ArrayList<>();

        // ==================================================
        // 1. BULLET PEMAIN VS MUSUH
        // ==================================================

        for (
            Bullet bullet :
            bulletManager.getActivePlayerBullets()
        ) {
            if (bullet.isDestroyed()) {
                continue;
            }

            for (GameObject entity : entities) {
                if (
                    !(entity instanceof Enemy enemy)
                        || enemy.isDestroyed()
                ) {
                    continue;
                }

                if (
                    bullet.getCoreHitbox().overlaps(
                        enemy.getCoreHitbox()
                    )
                ) {
                    boolean defeated =
                        enemy.takeDamage(
                            bullet.getDamage()
                        );

                    if (defeated) {
                        player.addScore(
                            (int) enemy.getScoreValue()
                        );

                        Item powerDrop =
                            EntityFactory.createItem(
                                enemy.getX(),
                                enemy.getY(),
                                ItemType.POWER
                            );

                        newDrops.add(powerDrop);
                    }

                    bullet.destroy();

                    break;
                }
            }
        }

        // ==================================================
        // 2. BULLET MUSUH VS PEMAIN
        // CORE HIT DIPRIORITASKAN DARIPADA GRAZE
        // ==================================================

        if (!player.isDestroyed()) {
            for (
                Bullet bullet :
                bulletManager.getActiveEnemyBullets()
            ) {
                if (bullet.isDestroyed()) {
                    continue;
                }

                // Prioritas 1: Core Hit
                if (
                    bullet.getCoreHitbox().overlaps(
                        player.getCoreHitbox()
                    )
                ) {
                    player.takeDamage(
                        bullet.getDamage()
                    );

                    bullet.destroy();
                }

                // Prioritas 2: Graze Hit
                else if (
                    !bullet.hasBeenGrazed()
                        && bullet.getCoreHitbox().overlaps(
                        player.getGrazeHitbox()
                    )
                ) {
                    bullet.setGrazed(true);

                    player.addScore(50);

                    System.out.println(
                        "[CollisionReferee] GRAZE! "
                            + player.getName()
                            + " grazed an enemy bullet "
                            + "(+50 pts)!"
                    );
                }
            }
        }

        // ==================================================
        // 3. PEMAIN VS ITEM DAN KONTAK BADAN MUSUH
        // ==================================================

        if (!player.isDestroyed()) {
            for (GameObject entity : entities) {
                if (entity.isDestroyed()) {
                    continue;
                }

                // Kasus A: Pemain mengambil item
                if (entity instanceof Item item) {
                    boolean coreHit =
                        item.getCoreHitbox().overlaps(
                            player.getCoreHitbox()
                        );

                    boolean grazeHit =
                        item.getCoreHitbox().overlaps(
                            player.getGrazeHitbox()
                        );

                    if (coreHit || grazeHit) {
                        player.collectItem(item);
                    }
                }

                // Kasus B: Kontak badan pemain dengan musuh
                else if (entity instanceof Enemy enemy) {
                    if (
                        !enemy.isDestroyed()
                            && player.getCoreHitbox().overlaps(
                            enemy.getCoreHitbox()
                        )
                    ) {
                        player.takeDamage(10);
                    }
                }
            }
        }

        // Tambahkan item drop setelah iterasi selesai
        // agar struktur entities tidak berubah saat loop.
        entities.addAll(newDrops);
    }
}
