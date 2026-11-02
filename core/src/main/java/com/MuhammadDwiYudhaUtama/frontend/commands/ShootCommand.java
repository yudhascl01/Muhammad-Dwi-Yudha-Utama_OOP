package com.MuhammadDwiYudhaUtama.frontend.commands;

import com.MuhammadDwiYudhaUtama.frontend.objects.Player;
import com.MuhammadDwiYudhaUtama.frontend.systems.BulletManager;

public class ShootCommand implements Command {

    @Override
    public void execute(
        Player player,
        BulletManager bulletManager
    ) {
        if (player != null && !player.isDestroyed()) {
            player.shootBullet(bulletManager);
        }
    }
}
