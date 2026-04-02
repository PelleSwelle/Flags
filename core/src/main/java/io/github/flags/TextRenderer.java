package io.github.flags;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.scenes.scene2d.ui.Label;

public class TextRenderer {


    public static Label.LabelStyle getLabelStyle() {
        FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal("truetypefont/DroidSerif-Regular.ttf"));
        FreeTypeFontGenerator.FreeTypeFontParameter parameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
        parameter.size = 30;
        parameter.borderWidth = 1;
        parameter.color = Color.YELLOW;
        parameter.shadowOffsetX = 3;
        parameter.shadowOffsetY = 3;
        parameter.shadowColor = new Color(0, 0.5f, 0, 0.75f);
        BitmapFont font24 = generator.generateFont(parameter); // font size 24 pixels
        generator.dispose();

        Label.LabelStyle labelStyle = new Label.LabelStyle();
        labelStyle.font = font24;
        return labelStyle;
    }

}
