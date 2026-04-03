package io.github.flags;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.utils.GdxRuntimeException;
import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.JsonValue;

import java.util.ArrayList;

public class Flag {
    public String country;
    public Sprite reference;
    public ArrayList<FlagPiece> pieces;
    private final String path = "flags/";
    public boolean reference_displayed = false;
    public boolean isSolved = false;

//    TODO: selection of country
    public Flag(String country) {
        this.country = country;
        this.pieces = loadPieces();
        this.reference = new Sprite(new Texture(path+country+"/flag.png"));
        this.reference.setPosition(0, 0); // TODO: this should be in the middle of the screen
    }

    public void toggleReference() {
        this.reference_displayed = !this.reference_displayed;
    }

    public void compare() {
//        TODO: this does not work anymore after converting to actors.
        for ( FlagPiece piece : pieces) {
            if (!piece.isPositionCloseEnough() || !piece.isRotationCloseEnough()) {
                System.out.println("You lose!");
            } else {
                System.out.println("You win!");
                isSolved = true;
                break;
            }
        }
    }

    public void print() {
        for (int i = 0 ; i < pieces.size(); i++) {
            System.out.println("Piece: " + i + ":");
            System.out.println("difference: " + new Vector2(
                Math.abs(pieces.get(i).intendedPosition.x - pieces.get(i).getX()),
                Math.abs(pieces.get(i).intendedPosition.y - pieces.get(i).getY())
                ) + "\n"
            );
        }
    }
    public ArrayList<FlagPiece> loadPieces() {
        Json json = new Json();
        String jsonPath = "flags/data.json";

        // Load and parse the JSON file
        JsonValue root = json.fromJson(null, Gdx.files.internal(jsonPath));
        System.out.println("root");
        System.out.println(root);
        JsonValue countryData = root.get(this.country);
        System.out.println("countryData");
        System.out.println(countryData);

        if (countryData == null) {
            throw new GdxRuntimeException("Country not found: " + this.country);
        }

        JsonValue piecesData = countryData.get("pieces");
        ArrayList<FlagPiece> pieces = new ArrayList<>();

        for (JsonValue pieceData : piecesData) {
            String file = pieceData.getString("file");
            float x = pieceData.getFloat("x");
            float y = pieceData.getFloat("y");

            pieces.add(
                new FlagPiece(
                    new Sprite(
                        new Texture(path + this.country + "/pieces/" + file)),
                    new Vector2(x, y),
                pieces.size()
            ));
        }


//        String[] files = {"black.png", "red.png", "green.png", "seal.png"};
//        Vector2[] positions = {
//            new Vector2(0, 0),
//            new Vector2(300, 0),
//            new Vector2(600, 0),
//            new Vector2((float)225.99, (float)60.41)
//        };
//        ArrayList<FlagPiece> pieces = new ArrayList<>();
//        for (int i = 0; i < files.length; i++) {
//            pieces.add(
//                new FlagPiece(
//                    new Sprite(new Texture(path+this.country+"/pieces/" + files[i])),
//                    positions[i],
//                    i
//                )
//            );
//        }
//
//        System.out.println("coordinates: ");
//        for (FlagPiece piece : pieces) {
////            TODO: make this the width and height of the window.
//            piece.setPosition(piece.intendedPosition.x, piece.intendedPosition.y);
//            System.out.println(piece.getX() + " " + piece.getY());
//        }
        return pieces;
    }
}
