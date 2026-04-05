package io.github.flags;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.Game;

/** {@link com.badlogic.gdx.ApplicationListener} implementation shared by all platforms. */

public class FlagAssembly extends Game {
    // SCREENS
    private MainScreen mainScreen;
    private MenuScreen menuScreen;

    public final static int MENU = 0;
    public final static int APPLICATION = 1;

    public SpriteBatch batch;
    public FitViewport viewport;
    public static UI ui;
    public Flag currentFlag;

    @Override
    public void create() {
        batch = new SpriteBatch();
        ui = new UI();
        viewport = new FitViewport(1028, 800);
        menuScreen = new MenuScreen(this);
        if (currentFlag != null) {
            mainScreen = new MainScreen(this, currentFlag);
        }
        setScreen(menuScreen);
    }

    public void changeScreen(int screen) {
        switch (screen){
            case MENU:
                currentFlag = null;
                if (menuScreen == null) menuScreen = new MenuScreen(this);
                this.setScreen(menuScreen);
                break;
            case APPLICATION:
                if (mainScreen == null) mainScreen = new MainScreen(this, currentFlag);
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

