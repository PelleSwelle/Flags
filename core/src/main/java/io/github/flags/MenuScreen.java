package io.github.flags;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.SelectBox;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ScreenUtils;

public class MenuScreen implements Screen {
    private FlagAssembly game;
    private TextButton randomFlagButton;
    private TextButton loadFlagButton;
    private Table table;
    private Stage stage;
    private SelectBox selectBox;

    public MenuScreen(FlagAssembly flagAssembly) {
        game = flagAssembly;
        stage = new Stage(game.viewport);
        randomFlagButton = new TextButton("Load random flag", game.ui.skin, "default");
        randomFlagButton.pad(20);
        randomFlagButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                loadRandomFlag();
            }
        });

        loadFlagButton = new TextButton("Load Flag", game.ui.skin, "default");
        loadFlagButton.pad(20);
        loadFlagButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                loadFlag(selectBox.getSelected().toString());
            }
        });
        selectBox = new SelectBox(game.ui.skin);

        selectBox.setItems(createSelectList());

        selectBox.getSelected();

        table = new Table();
        table.setFillParent(true);
        table.setDebug(true);
        table.add(randomFlagButton);
        table.add(selectBox);
        table.row();
        table.add(loadFlagButton);
        stage.addActor(table);

    }

    private Array<String> createSelectList() {
        Array<String> names = new Array<>();

        for (String name : getAllCountryNames()) {
            names.add(name);
        }

        return names;
    }

    private String[] getAllCountryNames() {
        return new String[] {
            "afghanistan",
            "iceland",
            "liberia",
            "marshall_islands"
        };
    }

    private String getRandomCountryName() {

        int randomNumber = (int)(Math.random() * (getAllCountryNames().length - 0)) + 0;
        return getAllCountryNames()[randomNumber];
    }

    private void loadRandomFlag() {
        String countryName = getRandomCountryName();
        game.currentFlag = new Flag(countryName);
        game.changeScreen(FlagAssembly.APPLICATION);
    }

    private void loadFlag(String countryName) {
        game.currentFlag = new Flag(countryName);
        game.changeScreen(FlagAssembly.APPLICATION);
    }

    @Override
    public void show() {
        Gdx.input.setInputProcessor(stage);
    }

    @Override
    public void render(float delta) {
        ScreenUtils.clear(Color.BLACK);
        stage.act(delta);
        stage.draw();
    }

    @Override
    public void resize(int width, int height) {
        stage.getViewport().update(width, height);
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
        stage.dispose();
    }
}
