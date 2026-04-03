package io.github.flags;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.FitViewport;

/** {@link com.badlogic.gdx.ApplicationListener} implementation shared by all platforms. */

public class Main extends ApplicationAdapter {
    private Stage stage;
    private SpriteBatch batch;
    private static Flag flag;
    private Vector2 touchPos;
    private FitViewport viewport;
    private boolean isDebugEnabled;
    private ShapeRenderer cursor;
    private Vector2 cursorPosition;
    private static Table table;
    private Label testLabel;
    private TextButton checkButton;
    private static Label countryNameLabel;
    private static UI ui;

    @Override
    public void create() {
        ui = new UI();
        checkButton = new TextButton("Check", ui.skin, "default");
        checkButton.pad(20);
        checkButton.addListener(checkCorrectness());
        flag = new Flag("afghanistan");

        countryNameLabel = new Label(flag.country, ui.skin);
        countryNameLabel.setVisible(false);
        viewport = new FitViewport(1028, 800);
        stage = new Stage(viewport);
        table = new Table();
        stage.addActor(table);

        table.setFillParent(true);
        table.setDebug(true);
        table.add(checkButton);
        table.row();
        table.add(countryNameLabel);
        table.right().top();

        batch = new SpriteBatch();
        touchPos = new Vector2();

        Gdx.input.setInputProcessor(stage);

        this.isDebugEnabled = false;

        for (FlagPiece piece : flag.pieces) {
            stage.addActor(piece);
        }

        drawCursor();
    }

    private static ChangeListener checkCorrectness() {
        return new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                flag.compare();
                System.out.println("flag is solved? " + flag.isSolved);
                countryNameLabel.setVisible(flag.isSolved);
            }
        };
    }

    @Override
    public void render() {
        viewport.unproject(cursorPosition);
        Gdx.gl.glClearColor(1, 1, 1, 1);
        ScreenUtils.clear((float).20, (float).20, (float).20, 1, true);

        float delta = Gdx.graphics.getDeltaTime();
        batch.begin();

        if (flag.reference_displayed) {
            flag.reference.draw(batch);
        }
        batch.end();
        enableInput();

        stage.act(delta);
        stage.draw();
    }

    public void drawCursor() {
        cursorPosition = new Vector2(Gdx.input.getX(), Gdx.input.getY());

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
    }

    @Override
    public void dispose() {
        batch.dispose();
    }

    @Override
    public void resize(int width, int height) {
        stage.getViewport().update(width, height, true);
    }
}

