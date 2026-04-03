package io.github.flags;

import com.badlogic.gdx.Screen;

public class MenuScreen implements Screen {
    private Main parent;

    public MenuScreen(Main main) {
        parent = main;
    }

    @Override
    public void show() {

    }

    @Override
    public void render(float delta) {
        parent.changeScreen(Main.MENU);
    }

    @Override
    public void resize(int width, int height) {

    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {

    }
}
