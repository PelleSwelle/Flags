package io.github.flags;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.ScreenUtils;

public class GameScreen implements Screen {
    final FlagAssembly flagAssembly;
    private Stage stage;
    private Flag flag;
    public ShapeRenderer debugRenderer;
//    private InputHandler inputHandler;

    public GameScreen(final FlagAssembly flags, Flag _flag) {
        flagAssembly = flags;
        flag = _flag;
    }

    @Override
    public void show() {
        stage = new Stage(flagAssembly.viewport);
        stage.addActor(flagAssembly.ui.getGameUILayout());

        flag.board.setPosition(
            stage.getWidth() / 2 - flag.board.dimensions.x / 2,
            stage.getHeight() / 2 - flag.board.dimensions.y / 2);


        for (FlagPiece piece : flag.pieces) {
            float x_translated = piece.getX() + flag.board.getX();
            float y_translated = piece.getY() + flag.board.getY();
            piece.setIntendedPosition(new Vector2(x_translated, y_translated));
            stage.addActor(piece);
        }

        Gdx.input.setInputProcessor(stage);

        debugRenderer = new ShapeRenderer();
    }

    @Override
    public void render(float delta) {
        ScreenUtils.clear((float).20, (float).20, (float).20, 1, true);
        flagAssembly.batch.setProjectionMatrix(flagAssembly.viewport.getCamera().combined);
        debugRenderer.setProjectionMatrix(flagAssembly.viewport.getCamera().combined);
        debugRenderer.begin(ShapeRenderer.ShapeType.Line);
        Vector2 screenCoordinates = new Vector2(Gdx.input.getX(), Gdx.input.getY());
        Vector2 stageCoordinates = stage.screenToStageCoordinates(screenCoordinates);

        flag.setOutlines(debugRenderer, flagAssembly.isDebugEnabled);

        debugRenderer.end();

        flagAssembly.batch.begin();

//        DRAW ASSEMBLY BOARD
        flag.board.draw(flagAssembly.batch, 0);
        System.out.println("Assembly board coordinates: " + flag.board.getX() + " " + flag.board.getY());

        flagAssembly.batch.end();

        InputHandler.handleInput(flagAssembly, flag);

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
        flagAssembly.viewport.update(width, height);
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
