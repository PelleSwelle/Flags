package io.github.flags;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.Array;
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
    private static ShapeRenderer debugRenderer;
    BitmapFont font;
    private boolean isDebugEnabled;
    private Vector2 cursorOffset;
    private ShapeRenderer cursor;
    private Vector2 cursorPos;
    private Color cursorColor;

    @Override
    public void create() {
        batch = new SpriteBatch();
        image = new Texture("libgdx.png");
        flag = new Flag("afghanistan");
        touchPos = new Vector2();
        viewport = new FitViewport(1028, 800);
        stage = new Stage(viewport);
        Gdx.input.setInputProcessor(stage);
        debugRenderer = new ShapeRenderer();
        cursor = new ShapeRenderer();
        selectedPiece = null;
        font = new BitmapFont();
        this.isDebugEnabled = false;
        cursorOffset = new Vector2();
        cursorPos = new Vector2();
        cursorColor = Color.CORAL;

        for (FlagPiece piece : flag.pieces) {
            stage.addActor(piece);
//            piece.sprite.draw(batch);
        }
    }

    @Override
    public void render() {
        cursorPos.x = Gdx.input.getX();
        cursorPos.y = Gdx.input.getY();
        viewport.unproject(cursorPos);

        ScreenUtils.clear(0.15f, 0.15f, 0.2f, 1f);
        float delta = Gdx.graphics.getDeltaTime();
//        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        batch.begin();
        if (flag.reference_displayed) {
            flag.reference.draw(batch);
        }
        font.draw(batch, "hello", 10, 10);
        drawCursor();
        batch.end();
        if (isDebugEnabled) {
            drawDebugLines();
        }
        enableInput();

        stage.act(delta);
        stage.draw();
    }

    private void drawDebugLines() {
        Gdx.gl.glLineWidth(1);
        debugRenderer.setProjectionMatrix(viewport.getCamera().combined);
        debugRenderer.begin(ShapeRenderer.ShapeType.Line);
        debugRenderer.setColor(Color.WHITE);

        for (FlagPiece piece : flag.pieces) {
            debugRenderer.line(piece.currentPosition, piece.intendedPosition);
        }
        debugRenderer.end();
    }

    private void drawCursor() {
        cursor.setProjectionMatrix(viewport.getCamera().combined);
        cursor.begin(ShapeRenderer.ShapeType.Filled);
        if (isAboveAnyPiece()) {
            cursor.setColor(Color.BLUE);
        } else {
            cursor.setColor(Color.DARK_GRAY);
        }
        cursor.circle(cursorPos.x, cursorPos.y, 15);
        cursor.end();
    }

    private boolean isAboveAnyPiece() {
        boolean isAbove = false;
        for (FlagPiece piece : flag.pieces) {
            if (isAbovePiece(piece)) {
                isAbove = true;
                break;
            } else {
                isAbove = false;
            }
        }
        return isAbove;
    }

    private boolean isAbovePiece(FlagPiece piece) {
        return cursorPos.x > piece.sprite.getX() &&
        cursorPos.x < piece.sprite.getX() + piece.sprite.getWidth() &&
        cursorPos.y > piece.sprite.getY() &&
        cursorPos.y < piece.sprite.getY() + piece.sprite.getHeight();
    }

    private void enableInput() {
        if (Gdx.input.isKeyJustPressed(Input.Keys.R)) {
            flag.toggleReference();
        }


        if (Gdx.input.isKeyJustPressed(Input.Keys.C)) {
            flag.compare();
            if (isDebugEnabled) {
                flag.print();
            }
        }

        if (Gdx.input.isKeyJustPressed(Input.Keys.SPACE)) {
            isDebugEnabled = !isDebugEnabled;
        }

        if (Gdx.input.isKeyJustPressed(Input.Keys.O)) {
            for (FlagPiece piece : flag.pieces) {
                if (isAbovePiece(piece)) {
                    if (piece.getZIndex() >= 0) {
                        System.out.println("moving from " + piece.getZIndex() + " to " + (piece.getZIndex()-1));
                        piece.setZIndex(piece.getZIndex() + 1 );
                    }
                }
            }
        }

        if (Gdx.input.justTouched()) {
            touchPos.set(Gdx.input.getX(), Gdx.input.getY());
            viewport.unproject(touchPos);

            for (FlagPiece piece : flag.pieces) {
                if (isAbovePiece(piece)) {
                    // TODO: get local coordinates of piece.
                    cursorOffset.set(
                        touchPos.x - piece.sprite.getX(),
                        touchPos.y - piece.sprite.getY()
                    );
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
                selectedPiece.setPosition(touchPos.x - cursorOffset.x, touchPos.y - cursorOffset.y);
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

