package com.MuhammadDwiYudhaUtama.frontend.systems;

import com.MuhammadDwiYudhaUtama.frontend.objects.BulletType;
import com.MuhammadDwiYudhaUtama.frontend.objects.Player;
import com.MuhammadDwiYudhaUtama.frontend.objects.bullets.Bullet;
import com.MuhammadDwiYudhaUtama.frontend.objects.enemies.Boss;
import com.MuhammadDwiYudhaUtama.frontend.objects.enemies.Fairy;
import com.MuhammadDwiYudhaUtama.frontend.objects.items.Item;
import com.MuhammadDwiYudhaUtama.frontend.objects.items.ItemType;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class EntityFactory {

    public static Player createPlayer(
        float x,
        float y,
        String name,
        int hp,
        int power,
        int spellCards
    ) {
        Player player =
            new Player(x, y, name, hp, power, spellCards);

        Animation<TextureRegion> animation =
            AssetManager.getInstance()
                .getAnimation("player_idle");

        player.setAnimation(animation);

        return player;
    }

    public static Item createItem(
        float x,
        float y,
        ItemType itemType
    ) {
        Item item =
            new Item(x, y, itemType);

        String key = switch (itemType) {
            case POWER -> "item_power";
            case POINT -> "item_point";
            case BOMB -> "item_bomb";
            case LIFE -> "item_life";
        };

        TextureRegion sprite =
            AssetManager.getInstance()
                .getTextureRegion(key);

        item.setSprite(sprite);

        return item;
    }

    public static Bullet createEnemyBullet(
        float x,
        float y,
        int damage
    ) {
        Bullet bullet =
            new Bullet(
                x,
                y,
                0f,
                BulletType.DANMAKU,
                damage
            );

        TextureRegion sprite =
            AssetManager.getInstance()
                .getTextureRegion("bullet_danmaku");

        bullet.setSprite(sprite);

        return bullet;
    }

    public static Boss createBoss(float x, float y, String name, int hp) {
        // TODO:
        // 1. Buat Boss baru dengan x, y, name, dan hp dari parameter;
        //    simpan pada variabel lokal bernama `boss`.
        Boss boss = new Boss (x, y, name, hp);
        // 2. Ambil animasi "boss_idle" melalui getAnimation(...)
        Animation<TextureRegion>idleAnim = AssetManager.getInstance().getAnimation("Boss_Idle");
        //    dari AssetManager.getInstance(). Simpan hasilnya pada
        //    variabel lokal bernama `idleAnim`.
        // 3. Pasang idleAnim pada boss melalui boss.setAnimation(...).
        boss.setAnimation(idleAnim);
        // 4. Kembalikan boss.
        return boss;
    }

    public static Fairy createFairy(float x, float y, String name, int hp) {
        // TODO:
        // 1. Buat Fairy baru dengan x, y, name, dan hp dari parameter;
        //    simpan pada variabel lokal bernama `fairy`.
        Fairy fairy = new Fairy (x, y, name, hp);
        // 2. Ambil animasi "fairy_idle_red" melalui getAnimation(...)
        //    dari AssetManager.getInstance(). Simpan hasilnya pada
        //    variabel lokal bernama `idleAnim`.
        Animation<TextureRegion>idleAnim = AssetManager.getInstance().getAnimation("fairy_idle_red");
        // 3. Pasang idleAnim pada fairy melalui fairy.setAnimation(...).
        fairy.setAnimation(idleAnim);
        // 4. Kembalikan fairy.
        return fairy;
    }

}
