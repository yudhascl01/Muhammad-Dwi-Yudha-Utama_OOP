
package com.MuhammadDwiYudhaUtama.frontend.objects.patterns;

import com.MuhammadDwiYudhaUtama.frontend.systems.BulletManager;

public interface ShootingPattern {

    void execute(
        float originX,
        float originY,
        BulletManager bulletManager,
        boolean isPlayer
    );
}
