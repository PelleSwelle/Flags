package io.github.flags;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Polygon;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.utils.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.regex.Pattern;

public class Flag {
    public String country;
    public Sprite reference;
    public ArrayList<FlagPiece> pieces;
    private final String path = "flags/";
    public boolean isSolved = false;
    public boolean isPolygonsVisible = false;
    private boolean isSpritesVisible = true;

    //    TODO: selection of country
    public Flag(String country) {
        this.country = country;
        pieces = loadPieces();
    }

    public void setOutlines(ShapeRenderer renderer, boolean isVisible) {
        if (isVisible) {
            for (FlagPiece p : pieces) {
                renderer.setColor(p.isPositionCloseEnough() ? Color.GREEN : Color.RED);
                p.drawPolygons(renderer);
            }
        }
    }

    public void togglePolygons() {
        isPolygonsVisible = !isPolygonsVisible;
    }

    public void toggleSprites() {
        isSpritesVisible = !isSpritesVisible;
        for (FlagPiece p: pieces) {
            p.isSpriteVisible = isSpritesVisible;
        }
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

    public void setOutlines() {

    }

    public void drawPolygons(ShapeRenderer renderer) {
        for (FlagPiece p: pieces) {
            p.drawPolygons(renderer);
        }
    }

    public ArrayList<FlagPiece> loadPieces() {
        ArrayList<FlagPiece> pieces = new ArrayList<>();
        JsonValue root = new JsonReader().parse(Gdx.files.internal(path + country + "/data.json"));

        for (JsonValue pieceData = root.child; pieceData != null; pieceData = pieceData.next) {
            pieces.add(new FlagPiece(country, pieceData));
        }

        return pieces;
    }
}
