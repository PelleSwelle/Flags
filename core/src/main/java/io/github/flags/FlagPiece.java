package io.github.flags;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.ConvexHull;
import com.badlogic.gdx.math.Polygon;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.FloatArray;
import com.badlogic.gdx.utils.JsonReader;
import com.badlogic.gdx.utils.JsonValue;

public class FlagPiece extends Actor {
    Pixmap pixmap;
    Sprite sprite;
    private final Vector2 dragOffset;
    public Vector2 intendedPosition;
    Polygon polygon;

    public FlagPiece(Texture texture, Vector2 intendedPosition) {

        this.intendedPosition = intendedPosition;
        this.dragOffset = new Vector2();

        if (!texture.getTextureData().isPrepared()) {
            texture.getTextureData().prepare();
        }
        pixmap = texture.getTextureData().consumePixmap();

        this.intendedPosition = intendedPosition;
        this.setPosition(intendedPosition.x, intendedPosition.y);
//        this.setZIndex(zIndex);
        this.setSize(texture.getWidth(), texture.getHeight());
        this.loadPolygon();

        // Add input listener for dragging
        addListener(new InputListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                return polygon.contains(x, y);
            }

            @Override
            public void touchDragged(InputEvent event, float x, float y, int pointer) {
                moveBy(x - getWidth() / 2, y - getHeight() / 2);
            }
        });
    }

    private void syncPolygonToActor() {
        polygon.setPosition(getX(), getY());
        polygon.setRotation(getRotation());
        polygon.setScale(getScaleX(), getScaleY());
    }

    private void loadPolygon() {
        JsonValue root = new JsonReader().parse(Gdx.files.internal("flags/marshall_islands/pieces.json"));
        JsonValue piece = root.get(0); //TODO: just trying the first.
        JsonValue shape = piece.get("shape");

        float[] vertices = shape.asFloatArray();

        this.polygon = new Polygon(vertices);
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        sprite.setPosition(getX(), getY());
        sprite.setRotation(getRotation());
        sprite.draw(batch);
        syncPolygonToActor();
    }

    public boolean isPositionCloseEnough() {
        return Utils.isAlmostEqual(new Vector2(getX(), getY()), intendedPosition, 5);
    }

    public boolean isRotationCloseEnough() {
        return getRotation() < 5 || getRotation() > 355;
    }

}
