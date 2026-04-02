package io.github.flags;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator.FreeTypeFontParameter;
/** {@link com.badlogic.gdx.ApplicationListener} implementation shared by all platforms. */

public class Main extends ApplicationAdapter {
    private Stage stage;
    private SpriteBatch batch;
    private Texture image;
    private Flag flag;
    private Vector2 touchPos;
    private FitViewport viewport;
    private FlagPiece selectedPiece;
    private boolean isDebugEnabled;
    private Vector2 cursorOffset;
    private ShapeRenderer cursor;
    private Vector2 cursorPosition;
    private Color cursorColor;
    private Table table;
    private Label testLabel;
    private TextButton textButton;
    private UI ui;

    @Override
    public void create() {
        ui = new UI();
        textButton = new TextButton("I am a button", ui.skin, "default");

        batch = new SpriteBatch();
        viewport = new FitViewport(1028, 800);
        touchPos = new Vector2();

        stage = new Stage(viewport);
        Gdx.input.setInputProcessor(stage);
        table = new Table();

        this.isDebugEnabled = false;

        flag = new Flag("afghanistan");
        // CURSOR
        cursorOffset = new Vector2();
        cursorPosition = new Vector2();
        cursorColor = Color.CORAL;

        selectedPiece = null;

        for (FlagPiece piece : flag.pieces) {
            stage.addActor(piece);
//            piece.sprite.draw(batch);
        }
        int Help_Guides = 12;
        int row_height = Gdx.graphics.getWidth() / 12;
        int col_width = Gdx.graphics.getWidth() / 12;

        Label theLabel = new Label("True Type Font (.ttf) - Gdx FreeType", TextRenderer.getLabelStyle());
        theLabel.setSize(Gdx.graphics.getWidth()/Help_Guides*5,row_height);
        theLabel.setPosition(col_width*2,Gdx.graphics.getHeight()-row_height*4);

        stage.addActor(table);
        table.add(theLabel);
        table.add(textButton);
        table.setFillParent(true);
        table.setDebug(true);

        drawCursor();
    }

    @Override
    public void render() {
        cursorPosition.x = Gdx.input.getX();
        cursorPosition.y = Gdx.input.getY();
        viewport.unproject(cursorPosition);
        Gdx.gl.glClearColor(1, 1, 1, 1);
        ScreenUtils.clear((float).20, (float).20, (float).20, 1, true);

        float delta = Gdx.graphics.getDeltaTime();
        batch.begin();
        if (flag.reference_displayed) {
            flag.reference.draw(batch);
        }
        batch.end();
        if (isDebugEnabled) {
            drawDebugLines();
        }
        enableInput();

        stage.act(delta);
        stage.draw();
    }

    public void drawCursor() {
        ShapeRenderer cursor = new ShapeRenderer();

        cursor.setProjectionMatrix(viewport.getCamera().combined);
        cursor.begin(ShapeRenderer.ShapeType.Filled);
        if (isAboveAnyPiece()) {
            cursor.setColor(Color.BLUE);
        } else {
            cursor.setColor(Color.DARK_GRAY);
        }
        cursor.circle(cursorPosition.x, cursorPosition.y, 15);
        cursor.end();
    }

    private boolean isAbovePiece(FlagPiece piece) {
        return cursorPosition.x > piece.sprite.getX() &&
            cursorPosition.x < piece.sprite.getX() + piece.sprite.getWidth() &&
            cursorPosition.y > piece.sprite.getY() &&
            cursorPosition.y < piece.sprite.getY() + piece.sprite.getHeight();
    }

    public void drawDebugLines() {
        ShapeRenderer debugRenderer = new ShapeRenderer();
        Gdx.gl.glLineWidth(1);

        debugRenderer.setProjectionMatrix(viewport.getCamera().combined);
        debugRenderer.begin(ShapeRenderer.ShapeType.Line);
        debugRenderer.setColor(Color.WHITE);

        for (FlagPiece piece : flag.pieces) {
            debugRenderer.line(piece.currentPosition, piece.intendedPosition);
        }
        debugRenderer.end();
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

