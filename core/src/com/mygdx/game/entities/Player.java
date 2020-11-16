package com.mygdx.game.entities;

import com.badlogic.gdx.physics.box2d.Body;

public class Player {
    private Body body;

    public static float MAX_VELOCITY = 2;

    public Player(Body body) {
        this.body = body;
    }

    public Body getBody() {
        return body;
    }
}
