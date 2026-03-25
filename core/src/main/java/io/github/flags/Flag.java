package io.github.flags;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import java.util.ArrayList;

public class Flag {
    public ArrayList<FlagPiece> pieces;

//    TODO: selection of country
    public Flag(String country) {
        this.pieces = loadPieces();
    }

    public ArrayList<FlagPiece> loadPieces() {
        String[] files = {"black.png", "red.png", "green.png", "seal.png"};
        ArrayList<FlagPiece> pieces = new ArrayList<>();
        for (String file : files) {
            pieces.add(new FlagPiece(new Sprite(new Texture(file))));
        }

        for (FlagPiece piece : pieces) {
//            TODO: make this the width and height of the window.
            piece.setPosition((float)Math.random() * (800), (float)Math.random() * (800));
        }
        return pieces;
    }

//    TODO: public void moveUpSprite(Sprite sprite) {}
}
