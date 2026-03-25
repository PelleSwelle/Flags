package io.github.flags;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector2;

public class FlagPiece {
    Sprite sprite;
    Vector2 currentPosition;
    public Vector2 intendedPosition;

    public FlagPiece(Sprite sprite, Vector2 intendedPosition) {
        this.sprite = sprite;
        this.intendedPosition = intendedPosition;
    }

    public void setPosition(float x, float y) {
        this.sprite.setPosition(x, y);
    }

    public void setRotation(float degrees) {
        this.sprite.setRotation(degrees);
    }
}
