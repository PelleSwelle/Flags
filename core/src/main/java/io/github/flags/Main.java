package io.github.flags;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.FitViewport;

/** {@link com.badlogic.gdx.ApplicationListener} implementation shared by all platforms. */
public class Main extends ApplicationAdapter {
    private Stage stage;
    private SpriteBatch batch;
    private Texture image;
    private Flag flag;
    private Vector2 touchPosition;
    private FitViewport viewport;
    private Sprite grabbedPiece;

    @Override
    public void create() {
        batch = new SpriteBatch();
        image = new Texture("libgdx.png");
        flag = new Flag("Afghanistan");
        flag.loadPieces();
        touchPosition = new Vector2();
        viewport = new FitViewport(1028, 800);
        stage = new Stage(viewport);
        Gdx.input.setInputProcessor(stage);

        grabbedPiece = null;
    }

    @Override
    public void render() {
        ScreenUtils.clear(0.15f, 0.15f, 0.2f, 1f);
        batch.begin();
        for (Sprite sprite : flag.sprites) {
            sprite.draw(batch);
        }
        batch.end();

        enableInput();
    }

    private boolean isAboveSprite(Sprite sprite) {
        return touchPosition.x > sprite.getX() &&
        touchPosition.x < sprite.getX() + sprite.getWidth() &&
        touchPosition.y > sprite.getY() &&
        touchPosition.y < sprite.getY() + sprite.getHeight();
    }

    private void enableInput() {
        if (Gdx.input.justTouched()) {
            touchPosition.set(Gdx.input.getX(), Gdx.input.getY());
            viewport.unproject(touchPosition);

            for (Sprite sprite : flag.sprites) {
                if (
                    isAboveSprite(sprite)
                ) {
                    grabbedPiece = sprite;
                    System.out.println("grabbed piece: " + grabbedPiece);
                }
            }
        }

        if (Gdx.input.isTouched() && grabbedPiece != null) {
            touchPosition.set(Gdx.input.getX(), Gdx.input.getY());
            viewport.unproject(touchPosition);
            grabbedPiece.setCenterX(touchPosition.x);
            grabbedPiece.setCenterY(touchPosition.y);
        }

        if (!Gdx.input.isTouched()) {
            grabbedPiece = null;
        }
    }

    @Override
    public void dispose() {
        batch.dispose();
        image.dispose();
    }

    private void grabPiece() {

    }

    @Override
    public void resize(int width, int height) {
        stage.getViewport().update(width, height, true);
    }
}

