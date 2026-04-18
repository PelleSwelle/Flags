package io.github.flags;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.scenes.scene2d.Actor;

public class AssemblyBoard extends Actor {
    Flag flag;
    float width;
    float height;

    public AssemblyBoard(Flag flag) {
        this.flag = flag;
        // placeholder values
        this.width = 900;
        this.height = 474;
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        // draw an outline of the flag to be used to compare with the assembled flag.
        ShapeRenderer outlineRenderer = new ShapeRenderer();
        outlineRenderer.begin(ShapeRenderer.ShapeType.Line);
        outlineRenderer.setColor(Color.WHITE);

        float[] vertices = new float[] {
            getX(), getY(),
            getX(), getY() + height,
            getX() + width, getY() + height,
            getX() + width, getY()
        };
        outlineRenderer.polygon(vertices);
        outlineRenderer.end();
    }
}
