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
    private Vector2 touchPos;
    private FitViewport viewport;
    private FlagPiece selectedPiece;

    @Override
    public void create() {
        batch = new SpriteBatch();
        image = new Texture("libgdx.png");
        flag = new Flag("Afghanistan");
        flag.loadPieces();
        touchPos = new Vector2();
        viewport = new FitViewport(1028, 800);
        stage = new Stage(viewport);
        Gdx.input.setInputProcessor(stage);

        selectedPiece = null;
    }

    @Override
    public void render() {
        ScreenUtils.clear(0.15f, 0.15f, 0.2f, 1f);
        batch.begin();
        for (FlagPiece piece : flag.pieces) {
            piece.sprite.draw(batch);
        }
        batch.end();

        enableInput();
    }

    private boolean isAbovePiece(FlagPiece piece) {
        return touchPos.x > piece.sprite.getX() &&
        touchPos.x < piece.sprite.getX() + piece.sprite.getWidth() &&
        touchPos.y > piece.sprite.getY() &&
        touchPos.y < piece.sprite.getY() + piece.sprite.getHeight();
    }

    private void enableInput() {
//        if (Gdx.input.isKeyPressed(Input.Keys.U)) {
//            if (selectedSprite != null) {
//
//            }
//        }
        if (Gdx.input.justTouched()) {
            touchPos.set(Gdx.input.getX(), Gdx.input.getY());
            viewport.unproject(touchPos);

            for (FlagPiece piece : flag.pieces) {
                if (isAbovePiece(piece)) {
                    selectedPiece = piece;
                }
            }
        }

        if (Gdx.input.isTouched() && selectedPiece != null) {
            if (Gdx.input.isKeyPressed(Input.Keys.CONTROL_LEFT)) {
//                ROTATE SPRITE
                touchPos.set(Gdx.input.getX(), Gdx.input.getY());
                viewport.unproject(touchPos);
                selectedPiece.setRotation(touchPos.y);
            }
            else {
//                MOVE SPRITE
                touchPos.set(Gdx.input.getX(), Gdx.input.getY());
                viewport.unproject(touchPos);
                selectedPiece.sprite.setCenterX(touchPos.x);
                selectedPiece.sprite.setCenterY(touchPos.y);

            }
        }

        if (!Gdx.input.isTouched()) {
            selectedPiece = null;
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

