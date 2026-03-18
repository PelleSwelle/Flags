package io.github.flags;

import com.badlogic.gdx.assets.loaders.AssetLoader;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.scenes.scene2d.Stage;

import java.util.ArrayList;
import java.util.List;

public class Flag {
    public ArrayList<Sprite> sprites;
    private float scalar = .4f;

    public Flag(String country) {
        this.sprites = loadPieces();
    }

    public ArrayList<Sprite> loadPieces() {
        Texture texture1 = new Texture("Group3.png");
        Texture texture2 = new Texture("Vector0.png");
        Texture texture3 = new Texture("Vector1.png");
        Texture texture4 = new Texture("Vector2.png");

        ArrayList<Texture> textures = new ArrayList<>();
        textures.add(texture1);
        textures.add(texture2);
        textures.add(texture3);
        textures.add(texture4);


        ArrayList<Sprite> sprites = new ArrayList<>();
        for (Texture texture : textures) {
            sprites.add(new Sprite(texture));
        }

        for (Sprite sprite : sprites) {
//            TODO: make this the width and height of the window.
            sprite.setPosition((float)Math.random() * (800), (float)Math.random() * (800));
        }
        sprites.get(0).setPosition(1, 1);
        sprites.get(0).setSize(
            sprites.get(0).getTexture().getWidth() * this.scalar,
            sprites.get(0).getTexture().getHeight() * this.scalar
        );

        return sprites;
    }
}
