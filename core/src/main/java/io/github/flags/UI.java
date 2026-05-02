package io.github.flags;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator.FreeTypeFontParameter;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton.TextButtonStyle;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;

public class UI {
    public Skin skin;
    public BitmapFont font;
    public TextButtonStyle textButtonStyle;
    public Label.LabelStyle labelStyle;
    public Label countryNameLabel;
    private Table table;
    private TextButton checkButton;
    public TextButton outlinesButton;
    private FlagAssembly flagAssembly;

    public UI(FlagAssembly assembly) {
        this.skin = new Skin(Gdx.files.internal("skin/glassy/skin/glassy-ui.json"));
        this.font = this.generateFont(40);
        this.skin.add("default-font", font);
        this.flagAssembly = assembly;
        // TEXT BUTTON STYLE
        this.textButtonStyle = new TextButtonStyle();
        this.textButtonStyle.font = this.font;
        this.textButtonStyle.up = this.skin.newDrawable("white", Color.LIGHT_GRAY);
        this.textButtonStyle.down = this.skin.newDrawable("white", Color.GRAY);
        this.skin.add("default", textButtonStyle);

        this.labelStyle = new Label.LabelStyle();
        this.labelStyle.font = this.font;
    }

    public void displayCountryLabel(String countryName, Table table) {
        Label nameLabel = new Label(countryName, this.skin); // TODO: capitalize this
        table.add(nameLabel);
    }

    public BitmapFont generateFont(int size) {
        String fontPath = "truetypefont/DroidSerif-Regular.ttf";
        FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal(fontPath));
        FreeTypeFontParameter fontParameter = new FreeTypeFontParameter();
        fontParameter.size = size;
        return generator.generateFont(fontParameter);
    }

    public static Image setBackgroundImage(TextureRegion textureRegion) {
        Image background = new Image(textureRegion);
        background.setSize(Gdx.graphics.getWidth(),Gdx.graphics.getWidth());
        background.setPosition(0,Gdx.graphics.getHeight()-background.getHeight());
        return background;
    }

    public Table getGameUILayout() {

        checkButton = new TextButton("Check", skin, "default");
        checkButton.pad(20);
        checkButton.addListener(checkCorrectness(FlagAssembly.currentFlag));

        countryNameLabel = new Label(FlagAssembly.currentFlag.country, skin);
        countryNameLabel.setVisible(false);

        outlinesButton = new TextButton("Debug: " + flagAssembly.isDebugEnabled, skin, "default");
        outlinesButton.addListener(toggleDebugMode());

        Table table = new Table();
        table.setFillParent(true);
        table.setDebug(true);
        table.add(checkButton);
        table.add(outlinesButton);
        table.row();
        table.add(countryNameLabel);
        table.right().top();

        return table;
    }

    private ChangeListener toggleDebugMode() {
        return new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                Command command = new InputController.toggleDebugCommand(flagAssembly);
                command.execute();
            }
        };
    }

    private ChangeListener checkCorrectness(Flag flag) {
        return new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                flag.compare();
                System.out.println("flag is solved? " + flag.isSolved);
                countryNameLabel.setVisible(flag.isSolved);
            }
        };
    }
}
