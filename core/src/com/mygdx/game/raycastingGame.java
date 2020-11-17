package com.mygdx.game;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.mygdx.game.handlers.GameStateManager;
import com.mygdx.game.handlers.MyInput;
import com.mygdx.game.handlers.MyInputProcessor;
import com.mygdx.game.state.GameState;

public class raycastingGame extends ApplicationAdapter {
	SpriteBatch batch;
	GameStateManager gsm;
	ShapeRenderer shapeRenderer;

	private SpriteBatch sb;
	private OrthographicCamera cam;
	private OrthographicCamera hudCam;

	public static int V_WIDTH = 620;
	public static int V_HEIGHT = 440;

	public static float PPM = 100;

	public static final float STEP = 1 / 60f;
	private float dt = 0;


	@Override
	public void create () {
		Gdx.input.setInputProcessor(new MyInputProcessor());

		shapeRenderer = new ShapeRenderer();
		batch = new SpriteBatch();
		sb = new SpriteBatch();
		cam = new OrthographicCamera();
		cam.setToOrtho(false, V_WIDTH / PPM, V_HEIGHT / PPM);
		hudCam = new OrthographicCamera();

		gsm = new GameStateManager(this);
	}

	@Override
	public void render () {

		dt += Gdx.graphics.getDeltaTime();

		if(dt > STEP) {
			dt -= STEP;
			gsm.update(dt);
			gsm.render();
			MyInput.update();
		}
	}
	
	@Override
	public void dispose () {
		batch.dispose();
	}

	public SpriteBatch getSpriteBatch() {
		return batch;
	}

	public ShapeRenderer getShapeRenderer() {
		return shapeRenderer;
	}

	public OrthographicCamera getCam() {
		return cam;
	}

	public OrthographicCamera getHudCam() {
		return hudCam;
	}
}
