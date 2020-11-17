package com.mygdx.game.entities;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import com.mygdx.game.handlers.MyInput;

import static com.mygdx.game.raycastingGame.PPM;

public class Player extends B2DSprites{

    private enum State { STOP, JUMP, WALK, FALL };
    public State currentState;
    public State previousState;

    public static float MAX_VELOCITY = 50f / PPM;
    public static float acceleration = 20f / PPM;
    public static float rotation = 200f / PPM;

    private double angle;

    public Player(int posx, int posy, World world) {
        createPlayer(posx, posy, world);

        currentState = State.STOP;
        previousState = State.STOP;
        angle = this.getBody().getAngle();
    }

    @Override
    public void update(float dt) {
        handleInput(dt);
        setState();
        angle = this.getBody().getAngle();
        //System.out.println(currentState);
    }

    public double getAngle() {
        return this.angle;
    }

    public void setState() {
        currentState = getState();
        previousState = currentState;
    }

    public State getState(){
        //Test to Box2D for velocity on the X and Y-Axis
        //if mario is going positive in Y-Axis he is jumping... or if he just jumped and is falling remain in jump state
        if((this.body.getLinearVelocity().y > 0 && currentState == State.JUMP) || (this.body.getLinearVelocity().y < 0 && previousState == State.JUMP))
            return State.JUMP;
            //if negative in Y-Axis mario is falling
        else if(this.body.getLinearVelocity().y < 0)
            return State.FALL;
        return State.STOP;

    }

    public void handleInput(float dt) {
        Vector2 vel = this.getBody().getLinearVelocity();
        Vector2 pos = this.getBody().getPosition();

        // apply left impulse, but only if max velocity is not reached yet
        if (MyInput.isDown(MyInput.BUTTON_A) && vel.x > -Player.MAX_VELOCITY) {
            this.getBody().setAngularVelocity(rotation);
        }
        if (MyInput.isUp(MyInput.BUTTON_A)) {
            this.getBody().setAngularVelocity(0);
            //System.out.println(angle);
        }
        // apply right impulse, but only if max velocity is not reached yet
        if (MyInput.isDown(MyInput.BUTTON_D) && vel.x < Player.MAX_VELOCITY) {
            this.getBody().setAngularVelocity(-rotation);
            //System.out.println(angle);
        }
        if (MyInput.isUp(MyInput.BUTTON_D)) {
            this.getBody().setAngularVelocity(0);
        }
        // apply right impulse, but only if max velocity is not reached yet
        if (MyInput.isDown(MyInput.BUTTON_W) && currentState != State.JUMP) {
            this.getBody().setLinearVelocity((float) Math.cos(angle) * acceleration,(float) Math.sin(angle) * acceleration);
            //this.getBody().applyLinearImpulse((float) Math.cos(angle),(float) Math.sin(angle), pos.x, pos.y, true);
            //this.currentState = State.JUMP;
        }
        if (MyInput.isUp(MyInput.BUTTON_W)) {
            this.getBody().setLinearVelocity(0f, 0f);
        }
        if (MyInput.isDown(MyInput.BUTTON_S) && currentState != State.JUMP) {
            this.getBody().setLinearVelocity((float) -Math.cos(angle) * acceleration,(float) -Math.sin(angle) * acceleration);
            //this.getBody().applyLinearImpulse((float) -Math.cos(angle), (float) -Math.sin(angle), pos.x, pos.y, true);
        }
        if (MyInput.isUp(MyInput.BUTTON_S)) {
            this.getBody().setLinearVelocity(0f, 0f);
        }

    }

    private void createPlayer(int posx, int posy, World world) {
        // First we create a body definition
        BodyDef bodyDef = new BodyDef();
        // We set our body to dynamic, for something like ground which doesn't move we would set it to StaticBody
        bodyDef.type = BodyDef.BodyType.DynamicBody;
        // Set our body's starting position in the world
        bodyDef.position.set(posx / PPM, posy / PPM);

        // Create our body in the world using our body definition
        this.body = world.createBody(bodyDef);

        PolygonShape groundBox = new PolygonShape();
        groundBox.setAsBox(5 / PPM, 5 / PPM);

        // Create a fixture definition to apply our shape to
        //FixtureDef fixtureDef = new FixtureDef();
        //fixtureDef.shape = groundBox;

        // Create a circle shape and set its radius to 6
        CircleShape circle = new CircleShape();
        circle.setRadius(6f / PPM);

        // Create a fixture definition to apply our shape to
        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = circle;
        fixtureDef.density = 0.0f; // 0.0
        fixtureDef.friction = 0.0f; // 0.2
        fixtureDef.restitution = 0.0f; // Make it bounce a little bit 0.0

        // Create our fixture and attach it to the body
        this.fixture = this.body.createFixture(fixtureDef);

        // Remember to dispose of any shapes after you're done with them!
        // BodyDef and FixtureDef don't need disposing, but shapes do.
        groundBox.dispose();
    }

    public static float inBetween(float min, float max, float x) {
        return Math.max(Math.min(x, max), min);
    }

    @Override
    public void dispose() {
    }
}
