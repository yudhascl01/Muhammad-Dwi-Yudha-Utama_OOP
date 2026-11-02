package com.MuhammadDwiYudhaUtama.frontend.commands;

import java.util.List;

import com.MuhammadDwiYudhaUtama.frontend.objects.GameObject;
import com.MuhammadDwiYudhaUtama.frontend.objects.Player;
import com.MuhammadDwiYudhaUtama.frontend.systems.BulletManager;

public class BombCommand implements Command {

    private List<GameObject> entities;

    public BombCommand() {
    }

    public BombCommand(List<GameObject> entities) {
        this.entities = entities;
    }

    public void setEntities(List<GameObject> entities) {
        this.entities = entities;
    }

    @Override
    public void execute(
        Player player,
        BulletManager bulletManager
    ) {
        if (player != null && !player.isDestroyed()) {
            player.useBomb(bulletManager, entities);
        }
    }
}
