package io.github.flags;

import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.ConvexHull;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.FloatArray;

public class FlagPiece extends Image {
    Pixmap pixmap;
    Sprite sprite;
    private final Vector2 dragOffset;
    public Vector2 intendedPosition;

    public FlagPiece(Texture texture, Vector2 intendedPosition) {
        super(texture);

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

        // Add input listener for dragging
        addListener(new InputListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                // Convert touch coordinates to the sprite's local coordinates
                // (accounts for position, origin, scale, and rotation)
                Vector2 localTouch = new Vector2(x, y);
                localToActorCoordinates(event.getListenerActor(), localTouch); // Converts to actor-local coordinates

                // Convert local coordinates to texture coordinates
                // (accounts for the sprite's origin and flip Y-axis for Pixmap)
                int texX = (int) (localTouch.x - getOriginX());
                int texY = (int) (pixmap.getHeight() - (localTouch.y - getOriginY()));

                // Clamp to texture bounds
                if (texX >= 0 && texY >= 0 && texX < pixmap.getWidth() && texY < pixmap.getHeight()) {
                    if (isPixelOpaque(texX, texY)) {
                        dragOffset.set(x, y);
                        toFront();
                        return true;
                    }
                }
                return false;
            }

            @Override
            public void touchDragged(InputEvent event, float x, float y, int pointer) {
                setPosition(getX() + x - dragOffset.x, getY() + y - dragOffset.y);
            }
        });
    }

    private boolean isPixelOpaque(int x, int y) {
        if (x < 0 || y < 0 || x >= pixmap.getWidth() || y >= pixmap.getHeight()) {
            return false;
        }
        int pixel = pixmap.getPixel(x, y);
        return ((pixel >>> 24) & 0xff) > 10; // Alpha > 0 means opaque
    }

//    private FloatArray computeConvexHull(Array<Vector2> boundaryPoints) {
//        return new ConvexHull().computePolygon(boundaryPoints, false);
//    }

    private Array<Vector2> getBoundaryPoints(Pixmap pixmap) {
        Array<Vector2> boundaryPoints = new Array<>();

        for (int y = 0; y < pixmap.getHeight(); y++) {
            for (int x = 0; x < pixmap.getWidth(); x++) {
                if (isBoundary(pixmap, x, y)) {
                    boundaryPoints.add(new Vector2(x, y));
                }
            }
        }
        return boundaryPoints;
    }

    private boolean isBoundary(Pixmap pixmap, int x, int y) {
        int alpha = (pixmap.getPixel(x, y) >>> 24) & 0xff;
        if (alpha <= 10) return false;

        for (int dy = -1; dy <= 1; dy++) {
            for (int dx = -1; dx <= 1; dx++) {
                int nx =x + dx;
                int ny = y+ dy;

                if (nx < 0 || ny < 0 || nx >= pixmap.getWidth() || ny >= pixmap.getHeight())
                    return true;

                int neighborAlpha = (pixmap.getPixel(nx, ny) >>> 24) & 0xff;
                if (neighborAlpha <= 10)
                    return true;
            }
        }
        return false;
    }

//    @Override
//    public void draw(Batch batch, float parentAlpha) {
//        sprite.setPosition(getX(), getY());
//        sprite.setRotation(getRotation());
//        sprite.draw(batch);
//    }

    public boolean isPositionCloseEnough() {
        return Utils.isAlmostEqual(new Vector2(getX(), getY()), intendedPosition, 5);
    }

    public boolean isRotationCloseEnough() {
        return getRotation() < 5 || getRotation() > 355;
    }
}
