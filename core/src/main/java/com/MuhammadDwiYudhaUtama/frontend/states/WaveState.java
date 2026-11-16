package com.MuhammadDwiYudhaUtama.frontend.states;

import com.MuhammadDwiYudhaUtama.frontend.systems.LevelWaveManager;

public interface WaveState {

    void onEnter(LevelWaveManager manager);

    void update(LevelWaveManager manager, float delta);
}
