package io.github.flags;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;

public class FlagPiece extends Actor {
    Sprite sprite;
    public Vector2 intendedPosition;

    public FlagPiece(Sprite sprite, Vector2 intendedPosition, int zIndex) {
        this.sprite = sprite;
        this.intendedPosition = intendedPosition;
        this.setPosition(intendedPosition.x, intendedPosition.y);
        this.setZIndex(zIndex);
        this.setSize(sprite.getWidth(), sprite.getHeight());

        addListener(new InputListener() {
            private float dragStartX, dragStartY;
            private float actorStartX, actorStartY;
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                dragStartX = event.getStageX();
                dragStartY = event.getStageY();
                actorStartX = getX();
                actorStartY = getY();
                return true;
            }
            @Override
            public void touchDragged(InputEvent event, float x, float y, int pointer) {
                float currentX = event.getStageX();
                float currentY = event.getStageY();
                setPosition(actorStartX + (currentX - dragStartX), actorStartY + (currentY - dragStartY));
            }
        });
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        sprite.setPosition(getX(), getY());
        sprite.setRotation(getRotation());
        sprite.draw(batch);
    }

    public boolean isPositionCloseEnough() {
        return Utils.isAlmostEqual(new Vector2(getX(), getY()), intendedPosition, 5);
    }

    public boolean isRotationCloseEnough() {
        return sprite.getRotation() < 5 || sprite.getRotation() > 355;
    }
}
