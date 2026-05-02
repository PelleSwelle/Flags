package io.github.flags;

import com.badlogic.gdx.graphics.g2d.PolygonSpriteBatch;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.Game;

/** {@link com.badlogic.gdx.ApplicationListener} implementation shared by all platforms. */

public class FlagAssembly extends Game {
    // SCREENS
    private GameScreen gameScreen;
    private MenuScreen menuScreen;

    public final static int MENU = 0;
    public final static int APPLICATION = 1;
    public static InputController inputController = new InputController();

    public SpriteBatch batch;
    public PolygonSpriteBatch polySpriteBatch;

    public FitViewport viewport;
    public UI ui;
    public static Flag currentFlag;

    public boolean isDebugEnabled = false;

    public void setDebugEnabled(boolean enabled) {
        isDebugEnabled = enabled;
    }

    @Override
    public void create() {
        batch = new SpriteBatch();
        polySpriteBatch = new PolygonSpriteBatch();
        ui = new UI(this);
        viewport = new FitViewport(1028, 800);
        menuScreen = new MenuScreen(this);

        if (currentFlag != null) {
            gameScreen = new GameScreen(this, currentFlag);
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
                if (gameScreen == null) gameScreen = new GameScreen(this, currentFlag);
                this.setScreen(gameScreen);
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

