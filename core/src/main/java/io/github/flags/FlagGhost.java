package io.github.flags;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.scenes.scene2d.ui.Image;

public class FlagGhost extends Image {
    private Texture texture;
    public Sprite sprite;

    public FlagGhost(Texture texture) {
        this.texture = texture;
        this.sprite = new Sprite(this.texture);
    }
}
