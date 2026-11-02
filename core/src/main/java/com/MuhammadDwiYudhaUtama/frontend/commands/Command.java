package com.MuhammadDwiYudhaUtama.frontend.commands;

import com.MuhammadDwiYudhaUtama.frontend.objects.Player;
import com.MuhammadDwiYudhaUtama.frontend.systems.BulletManager;

public interface Command {

    void execute(
        Player player,
        BulletManager bulletManager
    );
}
