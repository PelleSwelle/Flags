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

    public AssemblyBoard(Flag flag) {
        this.flag = flag;
        // placeholder values
        this.size = getDimensions(flag.country);
    }

    private Vector2 getDimensions(String countryName) {
        Texture t = new Texture("flags/" + countryName + "/flag.png");
        return new Vector2(
            t.getWidth(),
            t.getHeight()
        );
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        // draw an outline of the flag to be used to compare with the assembled flag.
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
}
