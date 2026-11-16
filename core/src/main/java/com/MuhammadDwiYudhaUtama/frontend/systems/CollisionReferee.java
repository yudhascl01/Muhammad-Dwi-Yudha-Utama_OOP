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

    private int defeatCounter = 0;
    private int itemDropCount = 0;

    public void resolveCollisions(
        Player player,
        List<GameObject> entities,
        BulletManager bulletManager
    ) {
        if (player == null || entities == null || bulletManager == null) {
            return;
        }

        List<GameObject> newDrops = new ArrayList<>();

        // ==================================================
        // 1. BULLET PEMAIN VS MUSUH
        // ==================================================

        for (Bullet bullet : bulletManager.getActivePlayerBullets()) {
            if (bullet.isDestroyed()) {
                continue;
            }

            for (GameObject entity : entities) {
                if (!(entity instanceof Enemy enemy) || enemy.isDestroyed()) {
                    continue;
                }

                if (bullet.getShotHitbox().overlaps(enemy.getShotHitbox())) {

                    if (enemy.isDamageable()) {
                        boolean defeated = enemy.takeDamage(bullet.getDamage());

                        if (defeated) {
                            player.addScore((int) enemy.getScoreValue());

                            ItemType dropType = null;

                            if (enemy.getItemDrop() != null) {
                                dropType = enemy.getItemDrop();
                            } else if (defeatCounter++ % 3 == 0) {
                                dropType = (itemDropCount++ % 2 == 0)
                                    ? ItemType.POWER
                                    : ItemType.POINT;
                            }

                            if (dropType != null) {
                                Item drop = EntityFactory.createItem(
                                    enemy.getX(),
                                    enemy.getY(),
                                    dropType
                                );

                                newDrops.add(drop);

                                System.out.println(
                                    "[CollisionReferee] Defeated "
                                        + enemy.getName()
                                        + "! Dropped " + dropType
                                        + " item at ("
                                        + enemy.getX() + ", "
                                        + enemy.getY() + ")"
                                );
                            }
                        }
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
            for (Bullet bullet : bulletManager.getActiveEnemyBullets()) {
                if (bullet.isDestroyed()) {
                    continue;
                }

                if (bullet.getCoreHitbox().overlaps(player.getCoreHitbox())) {
                    if (!player.isInvulnerable()) {
                        System.out.println(
                            "[CollisionReferee] CORE HIT! Enemy Bullet hit "
                                + player.getName()
                                + " for " + bullet.getDamage() + " DMG!"
                        );

                        player.takeDamage(100, bulletManager, entities);

                        bullet.destroy();
                        break;
                    }
                } else if (
                    !bullet.hasBeenGrazed()
                        && bullet.getCoreHitbox().overlaps(
                        player.getGrazeHitbox()
                    )
                ) {
                    bullet.setGrazed(true);
                    player.addGraze();

                    System.out.println(
                        "[CollisionReferee] GRAZE! "
                            + player.getName()
                            + " grazed an enemy bullet (+50 pts)!"
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

                if (entity instanceof Item item) {
                    boolean coreHit = item.getCoreHitbox().overlaps(
                        player.getCoreHitbox()
                    );

                    boolean grazeHit = item.getCoreHitbox().overlaps(
                        player.getGrazeHitbox()
                    );

                    if (coreHit || grazeHit) {
                        player.collectItem(item);
                    }

                } else if (entity instanceof Enemy enemy && enemy.isCollidable()) {

                    if (
                        !enemy.isDestroyed()
                            && !player.isInvulnerable()
                            && player.getCoreHitbox().overlaps(
                            enemy.getCoreHitbox()
                        )
                    ) {
                        System.out.println(
                            "[CollisionReferee] CORE COLLISION! "
                                + "Player collided with "
                                + enemy.getName() + "!"
                        );

                        player.takeDamage(100, bulletManager, entities);
                    }
                }
            }
        }

        // Tambahkan item drop setelah iterasi selesai
        // agar struktur entities tidak berubah saat loop.
        entities.addAll(newDrops);
    }
}
