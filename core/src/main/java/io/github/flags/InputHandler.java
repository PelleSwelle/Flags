package io.github.flags;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;

public class InputHandler {

    public static void handleInput(Flag flag, AssemblyBoard board) {
        if (Gdx.input.isKeyJustPressed(Input.Keys.C)) {
            new InputController.CompareCommand(flag).execute();
        }

        if (Gdx.input.isKeyJustPressed(Input.Keys.Q)) {
            new InputController.MoveToIntendedPositionsCommand(flag).execute();
        }

        if (Gdx.input.isKeyJustPressed(Input.Keys.R)) {
            board.toggleGhost();
        }

        if (Gdx.input.isKeyJustPressed(Input.Keys.P)) {
            flag.togglePolygons();
        }

        if (Gdx.input.isKeyJustPressed(Input.Keys.S)) {
            flag.toggleSprites();
        }
    }
}
