package io.github.flags;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.JsonValue;
import com.badlogic.gdx.utils.ScreenUtils;

import static io.github.flags.FlagAssembly.ui;

public class MenuScreen implements Screen {
    private FlagAssembly game;
    private TextButton newFlagButton;
    private Table table;
    private Stage stage;
//    private UI ui;

    public MenuScreen(FlagAssembly flagAssembly) {
        game = flagAssembly;
        stage = new Stage(game.viewport);
        newFlagButton = new TextButton("Assemble random flag", ui.skin, "default");
        newFlagButton.pad(20);
        table = new Table();
        table.setFillParent(true);
        table.setDebug(true);
        table.add(newFlagButton);
        stage.addActor(table);

        newFlagButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                loadRandomFlag();
            }
        });
    }

    private String getRandomCountryName() {
        int numberOfCountries = 4;
        Json json = new Json();
        String jsonPath = "flags/data.json";

        JsonValue root = json.fromJson(null, Gdx.files.internal(jsonPath));
        int randomNumber = (int)(Math.random() * (numberOfCountries - 0)) + 0;
        JsonValue countryName = root.get(randomNumber);
        System.out.println("json: name: " + countryName.name);
        return countryName.name();
    }

    private void loadRandomFlag() {
        String countryName = getRandomCountryName();
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
