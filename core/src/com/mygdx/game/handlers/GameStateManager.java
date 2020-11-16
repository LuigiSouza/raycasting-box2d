package com.mygdx.game.handlers;

import com.mygdx.game.raycastingGame;
import com.mygdx.game.state.GameState;
import com.mygdx.game.state.Menu;

import java.util.Stack;

public class GameStateManager {

    private final raycastingGame game;
    private final Stack<GameState> gameStates;

    public static final int MENU = 420;

    public GameStateManager(raycastingGame game) {
        this.game = game;
        gameStates = new Stack<>();
        pushState(MENU);
    }

    public raycastingGame game() { return game;}

    public void update(float dt){
        gameStates.peek().update(dt);
    }

    public void render(){
        gameStates.peek().render();
    }

    private GameState getState(int state) {
        if(state == MENU) return new Menu(this);
        return null;
    }

    public void setState(int state) {
        popState();
        pushState(state);
    }

    public void pushState(int state) {
        gameStates.push(getState(state));
    }

    public void popState() {
        GameState g = gameStates.pop();
        g.dispose();
    }
}
