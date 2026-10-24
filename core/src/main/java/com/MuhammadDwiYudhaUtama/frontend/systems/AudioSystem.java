
package com.MuhammadDwiYudhaUtama.frontend.systems;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;

public class AudioSystem {

    private static AudioSystem instance;

    private Music bgm;
    private Sound bulletShot;
    private Sound enemyDeath;
    private Sound itemPickup;

    private boolean initialized = false;

    private AudioSystem() {
    }

    public static AudioSystem getInstance() {
        if (instance == null) {
            instance = new AudioSystem();
        }
        return instance;
    }

    public void init() {
        if (initialized) {
            return;
        }

        if (Gdx.audio == null || Gdx.files == null) {
            Gdx.app.error("AudioSystem", "Audio API belum tersedia.");
            return;
        }

        try {
            Gdx.app.log("AudioSystem", "Memuat audio...");

            bgm = Gdx.audio.newMusic(
                Gdx.files.internal("audio/bgm.wav")
            );
            bgm.setLooping(true);
            bgm.setVolume(0.5f);

            bulletShot = Gdx.audio.newSound(
                Gdx.files.internal("audio/bullet_shoot.wav")
            );

            enemyDeath = Gdx.audio.newSound(
                Gdx.files.internal("audio/enemy_death.wav")
            );

            itemPickup = Gdx.audio.newSound(
                Gdx.files.internal("audio/item_pickup.wav")
            );

            initialized = true;
            Gdx.app.log("AudioSystem", "Audio berhasil dimuat.");

        } catch (Exception e) {
            Gdx.app.error("AudioSystem", "Gagal memuat audio.", e);
            dispose();
        }
    }

    public void playBgm() {
        if (bgm != null && !bgm.isPlaying()) {
            bgm.play();
        }
    }

    public void stopBgm() {
        if (bgm != null) {
            bgm.stop();
        }
    }

    public void setBgmVolume(float volume) {
        if (bgm != null) {
            bgm.setVolume(Math.max(0f, Math.min(1f, volume)));
        }
    }

    public void playBulletShot() {
        if (bulletShot != null) {
            bulletShot.play(0.7f);
        }
    }

    public void playEnemyDeath() {
        if (enemyDeath != null) {
            enemyDeath.play(0.8f);
        }
    }

    public void playItemPickup() {
        if (itemPickup != null) {
            itemPickup.play(0.8f);
        }
    }

    public boolean isInitialized() {
        return initialized;
    }

    public void dispose() {
        if (bgm != null) {
            bgm.stop();
            bgm.dispose();
            bgm = null;
        }

        if (bulletShot != null) {
            bulletShot.dispose();
            bulletShot = null;
        }

        if (enemyDeath != null) {
            enemyDeath.dispose();
            enemyDeath = null;
        }

        if (itemPickup != null) {
            itemPickup.dispose();
            itemPickup = null;
        }

        initialized = false;
    }
}
