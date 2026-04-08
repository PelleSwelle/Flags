package io.github.flags;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.GdxRuntimeException;
import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.JsonValue;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.regex.Pattern;

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
        ArrayList<FlagPiece> pieces = new ArrayList<>();

        String assetstxt = Gdx.files.internal("assets.txt").readString();
        String[] lines = assetstxt.split("\\r?\\n");
        String prefix = "flags/" + country + "/pieces/";
        Pattern pattern = Pattern.compile("^" + Pattern.quote(prefix) + ".+\\.png$", Pattern.CASE_INSENSITIVE);

        for (String line : lines) {
            if (pattern.matcher(line).matches()) {
                String fileName = line.substring(line.lastIndexOf('/') + 1);
                String texturePath = line;
                pieces.add(
                    new FlagPiece(new Texture(Gdx.files.internal(texturePath)), new Vector2())
                );
            }
        }
        return pieces;
    }
}
