package io.github.flags;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator.FreeTypeFontParameter;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton.TextButtonStyle;

public class UI {
    public Skin skin;
    public BitmapFont font;
    public TextButtonStyle textButtonStyle;

    public UI() {
        this.skin = new Skin(Gdx.files.internal("skin/glassy/skin/glassy-ui.json"));
        this.font = this.generateFont();
        this.skin.add("default-font", font);

        // TEXT BUTTON STYLE
        this.textButtonStyle = new TextButtonStyle();
        this.textButtonStyle.font = this.font;
        this.textButtonStyle.up = this.skin.newDrawable("white", Color.LIGHT_GRAY);
        this.textButtonStyle.down = this.skin.newDrawable("white", Color.GRAY);
        this.skin.add("default", textButtonStyle);
    }

    public void setSkin(String path) {
        this.skin = new Skin(Gdx.files.internal(path));
    }

    public BitmapFont generateFont() {
        String fontPath = "truetypefont/DroidSerif-Regular.ttf";
        FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal(fontPath));
        FreeTypeFontParameter fontParameter = new FreeTypeFontParameter();
        return generator.generateFont(fontParameter);
    }

    public static Image setBackgroundImage(TextureRegion textureRegion) {
        Image background = new Image(textureRegion);
        background.setSize(Gdx.graphics.getWidth(),Gdx.graphics.getWidth());
        background.setPosition(0,Gdx.graphics.getHeight()-background.getHeight());
        return background;
    }
}
