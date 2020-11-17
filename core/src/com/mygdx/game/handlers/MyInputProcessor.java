package com.mygdx.game.handlers;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputAdapter;

public class MyInputProcessor extends InputAdapter {

    public boolean keyDown(int k){
        if(k == Input.Keys.W){
            MyInput.setKey(MyInput.BUTTON_W, true);
        }
        if(k == Input.Keys.S){
            MyInput.setKey(MyInput.BUTTON_S, true);
        }
        if(k == Input.Keys.A){
            MyInput.setKey(MyInput.BUTTON_A, true);
        }
        if(k == Input.Keys.D){
            MyInput.setKey(MyInput.BUTTON_D, true);
        }
        if(k == Input.Keys.SPACE){
            MyInput.setKey(MyInput.BUTTON_SPACE, true);
        }
        if(k == Input.Keys.ESCAPE){
            MyInput.setKey(MyInput.BUTTON_ESCAPE, true);
        }
        return true;
    }

    public boolean keyUp(int k){
        if(k == Input.Keys.W){
            MyInput.setKey(MyInput.BUTTON_W, false);
        }
        if(k == Input.Keys.S){
            MyInput.setKey(MyInput.BUTTON_S, false);
        }
        if(k == Input.Keys.A){
            MyInput.setKey(MyInput.BUTTON_A, false);
        }
        if(k == Input.Keys.D){
            MyInput.setKey(MyInput.BUTTON_D, false);
        }
        if(k == Input.Keys.SPACE){
            MyInput.setKey(MyInput.BUTTON_SPACE, false);
        }
        if(k == Input.Keys.ESCAPE){
            MyInput.setKey(MyInput.BUTTON_ESCAPE, false);
        }
        return true;
    }

}
