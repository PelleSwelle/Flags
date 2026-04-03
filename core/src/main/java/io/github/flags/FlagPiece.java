package io.github.flags;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.EventListener;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.utils.DragListener;

public class FlagPiece extends Actor {
    Sprite sprite;
    public Vector2 intendedPosition;
    private Vector2 dragOffset;
    TextureRegion region;

    public FlagPiece(Sprite sprite, Vector2 intendedPosition, int zIndex) {
        this.sprite = sprite;
        this.intendedPosition = intendedPosition;
        this.setPosition(intendedPosition.x, intendedPosition.y);
        this.setZIndex(zIndex);
        this.dragOffset = new Vector2();

//        addListener(new InputListener() {
//            @Override
//            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
//                dragOffset.set(x - getX(), y - getY());
//                System.out.println("touch down");
//                return true;
//            }
//
//            @Override
//            public void touchDragged(InputEvent event, float x, float y, int pointer) {
//                setPosition(x - dragOffset.x, y - dragOffset.y);
//            }
//
//            @Override
//            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
//                currentPosition.set(getX(), getY());
//            }
//        });
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
