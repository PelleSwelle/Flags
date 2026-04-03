package io.github.flags;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.Game;

/** {@link com.badlogic.gdx.ApplicationListener} implementation shared by all platforms. */

public class Main extends Game {
    // SCREENS
    private MainScreen mainScreen;
    private MenuScreen menuScreen;

    public final static int MENU = 0;
    public final static int APPLICATION = 1;

    public SpriteBatch batch;
    public FitViewport viewport;
    private static UI ui;

    @Override
    public void create() {
        batch = new SpriteBatch();
        viewport = new FitViewport(1028, 800);
        menuScreen = new MenuScreen(this);
        mainScreen = new MainScreen(this);
        setScreen(mainScreen);
        ui = new UI();
    }

    public void changeScreen(int screen) {
        switch (screen){
            case MENU:
                if (menuScreen == null) menuScreen = new MenuScreen(this);
                this.setScreen(menuScreen);
                break;
            case APPLICATION:
                if (mainScreen == null) mainScreen = new MainScreen(this);
                this.setScreen(mainScreen);
                break;
        }
    }

    @Override
    public void render() {
        super.render();
    }

    @Override
    public void dispose() {
        batch.dispose();
    }
}

