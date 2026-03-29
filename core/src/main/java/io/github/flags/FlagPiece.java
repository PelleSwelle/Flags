package io.github.flags;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector2;

public class FlagPiece {
    Sprite sprite;
    public Vector2 currentPosition;
    public Vector2 intendedPosition;

    public FlagPiece(Sprite sprite, Vector2 intendedPosition) {
        this.sprite = sprite;
        this.intendedPosition = intendedPosition;
        this.currentPosition = new Vector2(intendedPosition);
    }

    public boolean isPositionCloseEnough() {
        return Utils.isAlmostEqual(this.currentPosition, this.intendedPosition, 5);
    }

    public boolean isRotationCloseEnough() {
        return this.sprite.getRotation() < 5 || this.sprite.getRotation() > 355;
    }

    public void setPosition(float x, float y) {
        this.currentPosition.x = x;
        this.currentPosition.y = y;

        this.sprite.setPosition(currentPosition.x, currentPosition.y);
    }

    public void setRotation(float degrees) {
        this.sprite.setRotation(degrees);
    }
}
