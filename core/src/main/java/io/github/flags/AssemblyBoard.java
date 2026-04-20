package io.github.flags;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;

public class AssemblyBoard extends Actor {
    Flag flag;
    Vector2 size;
    Texture reference;
    public boolean isGhostDisplayed = false;

    public AssemblyBoard(Flag flag) {
        this.flag = flag;
        this.size = getDimensions(flag.country);
        this.reference = new Texture("flags/" + flag.country + "/" + "flag.png");
    }

    private Vector2 getDimensions(String countryName) {
        Texture t = new Texture("flags/" + countryName + "/flag.png");
        return new Vector2(
            t.getWidth(),
            t.getHeight()
        );
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
            getX(), getY() + size.y,
            getX() + size.x, getY() + size.y,
            getX() + size.x, getY()
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
