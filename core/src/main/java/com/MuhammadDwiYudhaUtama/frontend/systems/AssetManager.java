package com.MuhammadDwiYudhaUtama.frontend.systems;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

import java.util.HashMap;
import java.util.Map;

public class AssetManager {

    private static AssetManager instance;

    private Map<String, TextureRegion> textureRegionMap;
    private Map<String, Animation<TextureRegion>> animationMap;
    private Map<String, Texture> textureMap;

    private AssetManager() {
        textureRegionMap = new HashMap<>();
        animationMap = new HashMap<>();
        textureMap = new HashMap<>();
    }

    public static AssetManager getInstance() {
        if (instance == null) {
            instance = new AssetManager();
        }

        return instance;
    }

    public Texture loadTexture(String filename) {
        if (!textureMap.containsKey(filename)) {
            if (Gdx.files == null || !Gdx.files.internal(filename).exists()) {
                return null;
            }

            Texture texture = new Texture(Gdx.files.internal(filename));
            textureMap.put(filename, texture);
        }

        return textureMap.get(filename);
    }

    public void registerRegion(String key, TextureRegion region) {
        textureRegionMap.put(key, region);
    }

    public void registerRegionFromSheet(
        String key,
        String filename,
        int tileWidth,
        int tileHeight,
        int row,
        int col
    ) {
        Texture texture = loadTexture(filename);

        if (texture != null) {
            TextureRegion[][] grid =
                TextureRegion.split(
                    texture,
                    tileWidth,
                    tileHeight
                );

            textureRegionMap.put(
                key,
                grid[row][col]
            );
        }
    }

    public void registerAnimationFromSheet(
        String key,
        String filename,
        int tileWidth,
        int tileHeight,
        int row,
        int numFrames,
        float frameDuration
    ) {
        registerAnimationFromSheet(
            key,
            filename,
            tileWidth,
            tileHeight,
            row,
            0,
            numFrames,
            frameDuration,
            Animation.PlayMode.LOOP
        );
    }

    public void registerAnimationFromSheet(
        String key,
        String filename,
        int tileWidth,
        int tileHeight,
        int row,
        int startCol,
        int numFrames,
        float frameDuration,
        Animation.PlayMode playMode
    ) {
        Texture texture = loadTexture(filename);

        if (texture != null) {
            TextureRegion[][] grid =
                TextureRegion.split(
                    texture,
                    tileWidth,
                    tileHeight
                );

            TextureRegion[] frames =
                new TextureRegion[numFrames];

            for (int i = 0; i < numFrames; i++) {
                frames[i] =
                    grid[row][startCol + i];
            }

            Animation<TextureRegion> animation =
                new Animation<>(
                    frameDuration,
                    frames
                );

            animation.setPlayMode(playMode);

            animationMap.put(
                key,
                animation
            );

            textureRegionMap.put(
                key,
                frames[0]
            );
        }
    }

    public void registerFlippedAnimationFromSheet(
        String key,
        String filename,
        int tileWidth,
        int tileHeight,
        int row,
        int numFrames,
        float frameDuration,
        boolean flipX,
        boolean flipY
    ) {
        registerFlippedAnimationFromSheet(
            key,
            filename,
            tileWidth,
            tileHeight,
            row,
            0,
            numFrames,
            frameDuration,
            Animation.PlayMode.LOOP,
            flipX,
            flipY
        );
    }

    public void registerFlippedAnimationFromSheet(
        String key,
        String filename,
        int tileWidth,
        int tileHeight,
        int row,
        int startCol,
        int numFrames,
        float frameDuration,
        Animation.PlayMode playMode,
        boolean flipX,
        boolean flipY
    ) {
        Texture texture = loadTexture(filename);

        if (texture != null) {
            TextureRegion[][] grid =
                TextureRegion.split(
                    texture,
                    tileWidth,
                    tileHeight
                );

            TextureRegion[] frames =
                new TextureRegion[numFrames];

            for (int i = 0; i < numFrames; i++) {
                TextureRegion frame =
                    new TextureRegion(
                        grid[row][startCol + i]
                    );

                frame.flip(
                    flipX,
                    flipY
                );

                frames[i] = frame;
            }

            Animation<TextureRegion> animation =
                new Animation<>(
                    frameDuration,
                    frames
                );

            animation.setPlayMode(playMode);

            animationMap.put(
                key,
                animation
            );

            textureRegionMap.put(
                key,
                frames[0]
            );
        }
    }

    public TextureRegion getTextureRegion(String key) {
        return textureRegionMap.get(key);
    }

    public TextureRegion getRegion(String key) {
        return getTextureRegion(key);
    }

    public Animation<TextureRegion> getAnimation(String key) {
        return animationMap.get(key);
    }

    public void init() {
        registerAnimationFromSheet(
            "player_idle",
            "player.png",
            32,
            48,
            0,
            0,
            8,
            0.125f,
            Animation.PlayMode.LOOP
        );

        registerAnimationFromSheet(
            "player_left",
            "player.png",
            32,
            48,
            1,
            0,
            4,
            0.12f,
            Animation.PlayMode.LOOP
        );

        registerAnimationFromSheet(
            "player_right",
            "player.png",
            32,
            48,
            2,
            0,
            4,
            0.12f,
            Animation.PlayMode.LOOP
        );

        registerAnimationFromSheet(
            "boss_idle",
            "rumia.png",
            64,
            64,
            0,
            0,
            4,
            0.2f,
            Animation.PlayMode.LOOP
        );

        registerAnimationFromSheet(
            "boss_left",
            "rumia.png",
            64,
            64,
            1,
            0,
            4,
            0.15f,
            Animation.PlayMode.REVERSED
        );

        registerAnimationFromSheet(
            "boss_right",
            "rumia.png",
            64,
            64,
            2,
            0,
            4,
            0.15f,
            Animation.PlayMode.NORMAL
        );

        registerAnimationFromSheet(
            "fairy_idle_red",
            "fairy.png",
            32,
            32,
            1,
            0,
            8,
            0.125f,
            Animation.PlayMode.LOOP
        );

        registerAnimationFromSheet(
            "fairy_idle_blue",
            "fairy.png",
            32,
            32,
            0,
            0,
            8,
            0.125f,
            Animation.PlayMode.LOOP
        );

        registerRegionFromSheet(
            "bullet_amulet",
            "amulet_reimu.png",
            16,
            16,
            0,
            0
        );

        registerRegionFromSheet(
            "bullet_amulet_homing",
            "amulet_reimu.png",
            16,
            16,
            1,
            0
        );

        registerRegionFromSheet(
            "bullet_danmaku",
            "bullets_small.png",
            16,
            16,
            2,
            3
        );

        registerRegionFromSheet(
            "item_power",
            "items.png",
            16,
            16,
            0,
            0
        );

        registerRegionFromSheet(
            "item_point",
            "items.png",
            16,
            16,
            0,
            1
        );

        registerRegionFromSheet(
            "item_bomb",
            "items.png",
            16,
            16,
            0,
            3
        );

        registerRegionFromSheet(
            "item_life",
            "items.png",
            16,
            16,
            0,
            5
        );
    }

    public void dispose() {
        for (Texture texture : textureMap.values()) {
            texture.dispose();
        }

        textureMap.clear();
        textureRegionMap.clear();
        animationMap.clear();
    }
}
