package io.github.flags;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Polygon;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.JsonValue;

public class FlagPiece extends Actor {
    private final Sprite sprite;
    private final Vector2 dragOffset;
    public Vector2 intendedPosition;
    private final Array<Polygon> polygons;

    public FlagPiece(String countryName, JsonValue data) {
        String pieceName = data.name;
        String textureFile = "flags/" + countryName + "/pieces/" + pieceName + ".png";
        this.intendedPosition = new Vector2(0, 0);
        this.dragOffset = new Vector2();
        this.sprite = new Sprite(new Texture(textureFile));

        this.setPosition(intendedPosition.x, intendedPosition.y);
        this.setSize(sprite.getWidth(), sprite.getHeight());
        this.polygons = getPolygons(data);

        addListener(touchAndDragListener);
    }

    InputListener touchAndDragListener = new InputListener() {
        @Override
        public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
            dragOffset.x = x;
            dragOffset.y = y;
            toFront();
            return true;
        }

        @Override
        public void touchDragged(InputEvent event, float x, float y, int pointer) {
            float newX = getX() + x - dragOffset.x;
            float newY = getY() + y - dragOffset.y;

            setPosition(newX, newY);
            for (Polygon p : new Array.ArrayIterator<>(polygons)) {
                p.setPosition(newX, newY);
            }
        }
    };

    @Override
    public Actor hit(float x, float y, boolean touchable) {
        for (Polygon p : new Array.ArrayIterator<>(polygons)) {
            if (p.contains(x, y)) {
                return this;
            }
        }
        return null;
    }

    private Array<Polygon> getPolygons(JsonValue pieceData) {
        Array<Polygon> polygons = new Array<>();

        for (JsonValue fixture = pieceData.child; fixture != null; fixture = fixture.next) {
            float[] vertices = fixture.get("shape").asFloatArray();

            for (int i = 1; i < vertices.length; i += 2) {
                vertices[i] = sprite.getHeight() - vertices[i];
            }
            polygons.add(new Polygon(vertices));
        }
        return polygons;
    }

    public void drawPolygons(ShapeRenderer renderer) {
        for (Polygon p : new Array.ArrayIterator<>(polygons)) {
            renderer.polygon(p.getTransformedVertices());
        }
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
        return getRotation() < 5 || getRotation() > 355;
    }

}
