package com.MuhammadDwiYudhaUtama.frontend.objects.patterns.bulletStrategy;

import com.MuhammadDwiYudhaUtama.frontend.objects.bullets.Bullet;

public interface BulletMovementPattern {

    // TODO (4 poin): Deklarasikan method `move` dengan kontrak dan parameter yang dijelaskan di atas.
    void move(Bullet bullet, float delta);
}
