package io.github.flags;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;

public class AssemblyBoard extends Actor {
    Vector2 dimensions;
    Texture reference;
    public boolean isGhostDisplayed = false;

    /**
     * The space in which the flag is to be assembled.
     *
     * @param dimensions The dimensions on which to base the size and dimensions of the space.
     * @return An assembly board corresponding to the current flags dimensions.
     */
    public AssemblyBoard(Vector2 dimensions) {
        this.dimensions = dimensions;
    }

    public void toggleGhost() {
        isGhostDisplayed = !isGhostDisplayed;
    }

    public void drawOutline() {
        ShapeRenderer outlineRenderer = new ShapeRenderer();
        outlineRenderer.begin(ShapeRenderer.ShapeType.Line);
        outlineRenderer.setColor(Color.WHITE);

        float[] vertices = new float[] {
            getX(), getY(),
            getX(), getY() + dimensions.y,
            getX() + dimensions.x, getY() + dimensions.y,
            getX() + dimensions.x, getY()
        };
        outlineRenderer.polygon(vertices);
        outlineRenderer.end();

    }

    // TODO: at the moment this displays white, but is at the correct location, so gets the job done for now.
    public void drawGhost(Batch batch, boolean isVisible) {
        if (isVisible) {
            batch.draw(reference, getX(), getY());
        }
    }
    @Override
    public void draw(Batch batch, float parentAlpha) {
        // draw an outline of the flag to be used to compare with the assembled flag.
        drawOutline();
        drawGhost(batch, isGhostDisplayed);
    }
}
