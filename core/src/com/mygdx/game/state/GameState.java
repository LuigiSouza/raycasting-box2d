package com.mygdx.game.state;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.mygdx.game.raycastingGame;
import com.mygdx.game.handlers.GameStateManager;

public abstract class GameState {
    protected GameStateManager gsm;
    protected raycastingGame game;

    protected SpriteBatch sb;
    protected OrthographicCamera cam;
    protected OrthographicCamera hudCam;

    protected GameState(GameStateManager gsm) {
        this.gsm = gsm;
        game = gsm.game();
        sb = game.getSpriteBatch();
        cam = game.getCam();
        hudCam = game.getHudCam();
    }

    public abstract void handleInput(float dt);
    public abstract void update(float dt);
    public abstract void render();
    public abstract void dispose();
}
