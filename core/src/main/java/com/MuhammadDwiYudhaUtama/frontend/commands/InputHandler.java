package com.MuhammadDwiYudhaUtama.frontend.commands;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.MuhammadDwiYudhaUtama.frontend.objects.GameObject;

import java.util.ArrayList;
import java.util.List;

public class InputHandler {

    // 1. Konfigurasi tombol
    private int keyShoot = Input.Keys.Z;
    private int keyBomb = Input.Keys.X;
    private int keyFocus = Input.Keys.SHIFT_LEFT;

    // 2. Slot Command Pre-allocated
    private final ShootCommand shootCommand =
        new ShootCommand();

    private final BombCommand bombCommand =
        new BombCommand();

    private final FocusCommand focusOn =
        new FocusCommand(true);

    private final FocusCommand focusOff =
        new FocusCommand(false);

    private final MoveCommand moveCommand =
        new MoveCommand(0, 0);

    // 3. Buffer Command
    private final List<Command> activeCommands =
        new ArrayList<>();

    public InputHandler() {
    }

    public InputHandler(List<GameObject> entities) {
        this.bombCommand.setEntities(entities);
    }

    public void setEntities(List<GameObject> entities) {
        this.bombCommand.setEntities(entities);
    }

    // 4. Getter dan Setter Key Rebinding

    public int getKeyShoot() {
        return keyShoot;
    }

    public void setKeyShoot(int keyShoot) {
        this.keyShoot = keyShoot;
    }

    public int getKeyBomb() {
        return keyBomb;
    }

    public void setKeyBomb(int keyBomb) {
        this.keyBomb = keyBomb;
    }

    public int getKeyFocus() {
        return keyFocus;
    }

    public void setKeyFocus(int keyFocus) {
        this.keyFocus = keyFocus;
    }

    // 5. Kerangka Input Handler
    // Logika pemetaan tombol dikerjakan pada Case Study.

    public List<Command> handleInput(float delta) {
        activeCommands.clear();

        if (Gdx.input == null) {
            return activeCommands;
        }

        // 1. Focus Mode Toggle (Shift Kiri atau Kanan) (3 poin)
        boolean isFocusHeld =
            Gdx.input.isKeyPressed(keyFocus)
                || Gdx.input.isKeyPressed(Input.Keys.SHIFT_RIGHT);

        activeCommands.add(
            isFocusHeld ? focusOn : focusOff
        );

        // 2. Pergerakan 8-Arah (WASD atau Arrow Keys) (5 poin)
        float dx = 0;
        float dy = 0;

        if (Gdx.input.isKeyPressed(Input.Keys.W)
            || Gdx.input.isKeyPressed(Input.Keys.UP)) {
            dy += 1;
        }

        if (Gdx.input.isKeyPressed(Input.Keys.S)
            || Gdx.input.isKeyPressed(Input.Keys.DOWN)) {
            dy -= 1;
        }

        if (Gdx.input.isKeyPressed(Input.Keys.A)
            || Gdx.input.isKeyPressed(Input.Keys.LEFT)) {
            dx -= 1;
        }

        if (Gdx.input.isKeyPressed(Input.Keys.D)
            || Gdx.input.isKeyPressed(Input.Keys.RIGHT)) {
            dx += 1;
        }

        float speedMult = isFocusHeld ? 0.45f : 1.0f;

        moveCommand.set(
            dx * delta * speedMult,
            dy * delta * speedMult
        );

        activeCommands.add(moveCommand);

        // 3. Shoot Command (Tahan tombol untuk tembakan beruntun) (3 poin)
        if (Gdx.input.isKeyPressed(keyShoot)) {
            activeCommands.add(shootCommand);
        }

        // 4. Bomb / Spell Card Command (Sekali tekan) (3 poin)
        if (Gdx.input.isKeyJustPressed(keyBomb)) {
            activeCommands.add(bombCommand);
        }

        return activeCommands;
    }
}
