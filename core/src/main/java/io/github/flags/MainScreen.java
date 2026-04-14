package io.github.flags;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.Box2D;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.ScreenUtils;
import com.codeandweb.physicseditor.PhysicsShapeCache;

public class MainScreen implements Screen {
    final FlagAssembly parent;

    private Stage stage;
    private Flag flag;
    private boolean isDebugEnabled;
    private ShapeRenderer cursor;
    private Table table;
    private TextButton checkButton;
    private Label countryNameLabel;
    private UI ui;
    World world;
    static final float STEP_TIME = 1f / 60f;
    static final int VELOCITY_ITERATIONS = 6;
    static final int POSITION_ITERATIONS = 2;

    float accumulator = 0;
    PhysicsShapeCache physicsBodies;
    Box2DDebugRenderer debugRenderer;

    public MainScreen(final FlagAssembly flags, Flag _flag) {
        parent = flags;

        ui = new UI();
        checkButton = new TextButton("Check", ui.skin, "default");
        checkButton.pad(20);
        checkButton.addListener(checkCorrectness());
        flag = _flag;

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

        Box2D.init();
        world = new World(new Vector2(0, -10), true);
        physicsBodies = new PhysicsShapeCache("flags/marshall_islands/physicsEditorPieces.xml");
        debugRenderer = new Box2DDebugRenderer();

        Body body = physicsBodies.createBody("marshall_islands_x0_y0", world, 1, 1);
        body.setTransform(10, 10, 0);
    }
    @Override
    public void show() {

    }

    private void stepWorld() {
        float delta = Gdx.graphics.getDeltaTime();
        accumulator += Math.min(delta, 0.25f);

        if (accumulator >= STEP_TIME) {
            accumulator -= STEP_TIME;
            world.step(STEP_TIME, VELOCITY_ITERATIONS, POSITION_ITERATIONS);
        }
    }

    private ChangeListener checkCorrectness() {
        return new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
//                flag.compare();
                System.out.println("flag is solved? " + flag.isSolved);
                countryNameLabel.setVisible(flag.isSolved);
            }
        };
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
    }

    @Override
    public void render(float delta) {
        ScreenUtils.clear((float).20, (float).20, (float).20, 1, true);
        parent.batch.setProjectionMatrix(parent.viewport.getCamera().combined);

        parent.batch.begin();

        stepWorld();
        if (flag.reference_displayed) {
            flag.reference.draw(parent.batch);
        }
        parent.batch.end();
        enableInput();



        stage.act(delta);
        stage.draw();
        debugRenderer.render(world, parent.viewport.getCamera().combined);
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
        world.dispose();
        debugRenderer.dispose();
    }
}
