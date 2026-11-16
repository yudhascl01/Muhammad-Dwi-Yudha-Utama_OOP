package com.MuhammadDwiYudhaUtama.frontend.states;

import com.MuhammadDwiYudhaUtama.frontend.objects.enemies.Boss;
import com.MuhammadDwiYudhaUtama.frontend.systems.LevelWaveManager;

public class StageClearState implements WaveState {

    @Override
    public void onEnter(LevelWaveManager manager) {

        System.out.println("=== STAGE 1 CLEAR! ===");

        Boss boss = manager.getBoss();

        if (boss != null && !boss.isDestroyed()) {
            boss.destroy();
        }
    }

    @Override
    public void update(LevelWaveManager manager, float delta) {
        // Stage Clear tidak memerlukan update tambahan.
    }
}
