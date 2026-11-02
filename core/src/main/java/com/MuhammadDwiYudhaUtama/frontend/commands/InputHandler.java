
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
        if (Gdx.input == null) {
            return activeCommands;
        }

        float dx = 0;
        float dy = 0;

        if (Gdx.input.isKeyPressed(Input.Keys.A) || Gdx.input.isKeyPressed(Input.Keys.D)) {
            boolean isFocusHeld = true;
            activeCommands.add(isFocusHeld ? focusOn : focusOff);

            if (Gdx.input.isKeyPressed(Input.Keys.W)) {
                dy += 1;
            }

            if (Gdx.input.isKeyPressed(Input.Keys.S)) {
                dy -= 1;
            }

            if (Gdx.input.isKeyPressed(Input.Keys.A)) {
                dx -= 1;
            }

            if (Gdx.input.isKeyPressed(Input.Keys.D)) {
                dx += 1;
            }

            float speedMult = isFocusHeld ? 0.45f : 1.0f;
            moveCommand.set(dx * delta * speedMult, dy * delta * speedMult);
            activeCommands.add(moveCommand);

            if (Gdx.input.isKeyPressed(keyShoot)) {
                activeCommands.add(shootCommand);
            }

            if (Gdx.input.isKeyPressed(keyBomb)) {
                activeCommands.add(bombCommand);
            }
        }
        return List.of();
    }
}
