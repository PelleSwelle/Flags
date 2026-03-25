package io.github.flags;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import java.util.ArrayList;

public class Flag {
    public String country;
    public Sprite reference;
    public ArrayList<FlagPiece> pieces;
    private final String path = "flags/";
    public boolean reference_displayed = false;

//    TODO: selection of country
    public Flag(String country) {
        this.country = country;
        this.pieces = loadPieces();
        this.reference = new Sprite(new Texture(path+country+"/flag.png"));
        this.reference.setPosition(0, 0); // TODO: this should be in the middle of the screen
    }

    public void toggleReference() {
        if (this.reference_displayed) {
            this.reference_displayed = false;
        } else {
            this.reference_displayed = true;
        }
    }

    public ArrayList<FlagPiece> loadPieces() {
        String[] files = {"black.png", "red.png", "green.png", "seal.png"};
        ArrayList<FlagPiece> pieces = new ArrayList<>();
        for (String file : files) {
            pieces.add(new FlagPiece(new Sprite(new Texture(path+this.country+"/pieces/" + file))));
        }

        for (FlagPiece piece : pieces) {
//            TODO: make this the width and height of the window.
            piece.setPosition((float)Math.random() * (800), (float)Math.random() * (800));
        }
        return pieces;
    }

//    TODO: public void moveUpSprite(Sprite sprite) {}
}
