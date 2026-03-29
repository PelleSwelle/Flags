package io.github.flags;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import java.util.ArrayList;

public class Flag {
    public ArrayList<Sprite> sprites;

//    TODO: selection of country
    public Flag(String country) {
        this.sprites = loadPieces();
    }

    public ArrayList<Sprite> loadPieces() {
        String[] files = {"black.png", "red.png", "green.png", "seal.png"};
        ArrayList<Sprite> sprites = new ArrayList<>();
        for (String file : files) {
            sprites.add(new Sprite(new Texture(file)));
        }

        for (Sprite sprite : sprites) {
//            TODO: make this the width and height of the window.
            sprite.setPosition((float)Math.random() * (800), (float)Math.random() * (800));
        }
        return sprites;
    }

//    TODO: public void moveUpSprite(Sprite sprite) {}
}
