package com.MuhammadDwiYudhaUtama.frontend;

import com.MuhammadDwiYudhaUtama.frontend.objects.Player;
import com.MuhammadDwiYudhaUtama.frontend.objects.bullets.Bullet;
import com.MuhammadDwiYudhaUtama.frontend.objects.enemies.Boss;
import com.MuhammadDwiYudhaUtama.frontend.objects.enemies.Enemy;
import com.MuhammadDwiYudhaUtama.frontend.objects.enemies.Fairy;
import com.MuhammadDwiYudhaUtama.frontend.objects.items.Item;

public class Test {

    public static void main(String[] args) {

        System.out.println(
            "=== TOUHOU OOP PRACTICUM - MODULE 1 ==="
        );

        Player reimu = new Player(
            "Reimu Hakurei",
            100,
            15,
            3
        );

        Enemy fairyBoss = new Enemy(
            "Cirno (Stage 2 Boss)",
            50
        );

        System.out.println(
            "\n--- Initial Battle State ---"
        );

        System.out.println(
            "Player: "
                + reimu.getName()
                + " | HP: "
                + reimu.getHp()
                + " | Power: "
                + reimu.getPower()
                + " | SpellCards: "
                + reimu.getSpellCards()
        );

        System.out.println(
            "Enemy: "
                + fairyBoss.getName()
                + " | HP: "
                + fairyBoss.getHp()
        );

        System.out.println(
            "\n--- Turn 1: Player Shoots Enemy ---"
        );

        reimu.shoot(fairyBoss);

        System.out.println(
            "\n--- Turn 2: Enemy Counter-attacks ---"
        );

        fairyBoss.attack(
            reimu,
            30
        );

        System.out.println(
            "\n--- Turn 3: Player Shoots Enemy ---"
        );

        reimu.shoot(fairyBoss);

        System.out.println(
            "\n--- Turn 4: Enemy Counter-attacks ---"
        );

        fairyBoss.attack(
            reimu,
            80
        );

        System.out.println(
            "\n=== MODULE 2 TEST ==="
        );

        Player reimu2 = new Player(
            "Reimu Hakurei",
            100,
            15,
            3
        );

        Fairy fairy = new Fairy(
            "Stage 1 Fairy",
            20
        );

        Boss cirno = new Boss(
            "Cirno (Stage 2 Boss)",
            150
        );

        Item pointItem = new Item(
            200,
            450,
            12,
            12,
            120f,
            "Point Item",
            1000L
        );

        System.out.println(
            "Player: "
                + reimu2.getName()
        );

        System.out.println(
            "Fairy: "
                + fairy.getName()
                + " | Score Value: "
                + fairy.getScoreValue()
        );

        System.out.println(
            "Boss: "
                + cirno.getName()
                + " | Score Value: "
                + cirno.getScoreValue()
        );

        System.out.println(
            "Initial Item Y: "
                + pointItem.getY()
        );

        pointItem.update(0.5f);

        System.out.println(
            "Item Y after 0.5s: "
                + pointItem.getY()
        );

        reimu2.shoot(fairy);

        reimu2.collectItem(pointItem);

        reimu2.shoot(cirno);

        System.out.println(
            "Final Score: "
                + reimu2.getScore()
        );

        System.out.println(
            "\n=== PRE-CS MODUL 4 - BULLET SANITY CHECK ==="
        );

        Player reimu3 = new Player(
            "Reimu Hakurei",
            100,
            15,
            3
        );

        Bullet bullet =
            reimu3.shootBullet();

        System.out.println(
            "Bullet created at: ("
                + bullet.getX()
                + ", "
                + bullet.getY()
                + ") | Damage: "
                + bullet.getDamage()
        );

        bullet.update(0.1f);

        System.out.println(
            "Bullet Y after 0.1s: "
                + bullet.getY()
        );

        System.out.println(
            "Is bullet off screen? "
                + bullet.isOffScreen(
                640,
                480
            )
        );

        System.out.println(
            "\n=== CS MODUL 4 - COLLISION TEST ==="
        );

        Fairy testFairy = new Fairy(
            "Stage 1 Fairy",
            20
        );

        Bullet testBullet =
            reimu3.shootBullet();

        testBullet.onCollision(testFairy);

        System.out.println(
            "Bullet destroyed: "
                + testBullet.isDestroyed()
        );

        System.out.println(
            "Fairy destroyed: "
                + testFairy.isDestroyed()
        );

        System.out.println(
            "\n=== TEST COMPLETE ==="
        );
    }
}
