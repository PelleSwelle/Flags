package io.github.flags;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.ScreenUtils;

public class GameScreen implements Screen {
    final FlagAssembly parent;

    private Stage stage;
    private Flag flag;
    private boolean isDebugEnabled;
    private ShapeRenderer cursor;
    private Table table;
    private TextButton checkButton;
    private Label countryNameLabel;
    private UI ui;
    public ShapeRenderer debugRenderer;
    private Vector2 screenCoordinates;
    private Vector2 stageCoordinates;
    private AssemblyBoard board;

    public GameScreen(final FlagAssembly flags, Flag _flag) {
        parent = flags;
        debugRenderer = new ShapeRenderer();
        ui = new UI();
        checkButton = new TextButton("Check", ui.skin, "default");
        checkButton.pad(20);
        checkButton.addListener(checkCorrectness());
        flag = _flag;
        board = new AssemblyBoard(flag);
        countryNameLabel = new Label(flag.country, ui.skin);
        countryNameLabel.setVisible(false);
        stage = new Stage(parent.viewport);
        table = new Table();
        stage.addActor(table);
        table.setFillParent(true);
        table.setDebug(true);
        table.add(checkButton);
        table.row();
        table.add(countryNameLabel);
        table.right().top();

        Gdx.input.setInputProcessor(stage);

        this.isDebugEnabled = false;

        for (FlagPiece piece : flag.pieces) {
            stage.addActor(piece);
        }
    }
    @Override
    public void show() {

    }

    private ChangeListener checkCorrectness() {
        return new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                flag.compare();
                System.out.println("flag is solved? " + flag.isSolved);
                countryNameLabel.setVisible(flag.isSolved);
            }
        };
    }

    private void enableInput() {
        if (Gdx.input.isKeyJustPressed(Input.Keys.R)) {
            flag.toggleReference();
        }

        if (Gdx.input.isKeyJustPressed(Input.Keys.P)) {
            flag.togglePolygons();
        }

        if (Gdx.input.isKeyJustPressed(Input.Keys.S)) {
            flag.toggleSprites();
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
    }

    @Override
    public void render(float delta) {
        ScreenUtils.clear((float).20, (float).20, (float).20, 1, true);
        parent.batch.setProjectionMatrix(parent.viewport.getCamera().combined);
        debugRenderer.setProjectionMatrix(parent.viewport.getCamera().combined);
        debugRenderer.begin(ShapeRenderer.ShapeType.Line);
        debugRenderer.setColor(Color.RED);
        screenCoordinates = new Vector2(Gdx.input.getX(), Gdx.input.getY());
        stageCoordinates = stage.screenToStageCoordinates(screenCoordinates);

        if (flag.isPolygonsVisible) {
            flag.drawPolygons(debugRenderer);
        }
        debugRenderer.end();

        parent.batch.begin();

        if (flag.reference_displayed) {
            flag.reference.draw(parent.batch);
        }
        board.setPosition(
            stage.getWidth() / 2 - board.size.x / 2,
            stage.getHeight() / 2 - board.size.y / 2);
        board.draw(parent.batch, 0);
        parent.batch.end();

        enableInput();

        stage.act(delta);
        stage.draw();
        boolean isHovering = false;
        for (FlagPiece piece: flag.pieces) {
            if (piece.hit(stageCoordinates.x, stageCoordinates.y, true) != null) {
                isHovering = true;
                break;
            }
        }

        debugRenderer.begin(ShapeRenderer.ShapeType.Filled);
        debugRenderer.setColor(isHovering ? Color.GREEN : Color.RED);
        debugRenderer.circle(Gdx.input.getX(), stage.getHeight() - Gdx.input.getY(), 20);
        debugRenderer.end();
    }

    @Override
    public void resize(int width, int height) {
        parent.viewport.update(width, height);
    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {
    }
}
