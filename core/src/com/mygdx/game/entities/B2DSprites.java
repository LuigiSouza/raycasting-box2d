package com.mygdx.game.entities;

import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.Fixture;

abstract class B2DSprites {
    protected Body body;
    protected Fixture fixture;

    public Body getBody() {
        return body;
    }

    abstract void update(float dt);
    abstract void dispose();
}
