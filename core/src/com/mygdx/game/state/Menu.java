package com.mygdx.game.state;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import com.badlogic.gdx.physics.box2d.joints.DistanceJoint;
import com.badlogic.gdx.physics.box2d.joints.DistanceJointDef;
import com.mygdx.game.entities.Player;
import com.mygdx.game.handlers.GameStateManager;
import com.mygdx.game.handlers.MyInput;

import java.util.ArrayList;

import static com.mygdx.game.raycastingGame.STEP;
import static com.mygdx.game.raycastingGame.PPM;

public class Menu extends GameState{
    private int img_x = 0;
    private int img_y = 0;

    private Texture img;
    private World world;
    private Box2DDebugRenderer b2dr;

    private Player player;
    private ArrayList<Body> bodies = new ArrayList<Body>();

    public void createGround() {
        // Create our body definition
        BodyDef groundBodyDef = new BodyDef();
        // Set its world position
        groundBodyDef.position.set(new Vector2(0 / PPM, 10 / PPM));

        // Create a body from the definition and add it to the world
        Body groundBody = world.createBody(groundBodyDef);

        // Create a polygon shape
        PolygonShape groundBox = new PolygonShape();
        // Set the polygon shape as a box which is twice the size of our view port and 20 high
        // (setAsBox takes half-width and half-height as arguments)
        groundBox.setAsBox(cam.viewportWidth, 10.0f / PPM);
        // Create a fixture from our polygon shape and add it to our ground body
        groundBody.createFixture(groundBox, 0.0f);
        // Clean up after ourselves
        groundBox.dispose();
    }

    public void createPlayer(int posx, int posy) {
        // First we create a body definition
        BodyDef bodyDef = new BodyDef();
        // We set our body to dynamic, for something like ground which doesn't move we would set it to StaticBody
        bodyDef.type = BodyDef.BodyType.DynamicBody;
        // Set our body's starting position in the world
        bodyDef.position.set(posx / PPM, posy / PPM);

        // Create our body in the world using our body definition
        Body body = world.createBody(bodyDef);

        // Create a circle shape and set its radius to 6
        CircleShape circle = new CircleShape();
        circle.setRadius(6f / PPM);

        // Create a fixture definition to apply our shape to
        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = circle;
        fixtureDef.density = 0.0f; // 0.0
        fixtureDef.friction = 0.4f; // 0.2
        fixtureDef.restitution = 0.0f; // Make it bounce a little bit 0.0

        // Create our fixture and attach it to the body
        Fixture fixture = body.createFixture(fixtureDef);

        // Remember to dispose of any shapes after you're done with them!
        // BodyDef and FixtureDef don't need disposing, but shapes do.
        circle.dispose();

        player = new Player(body);

        DistanceJointDef defJoint = new DistanceJointDef ();
        defJoint.length = 0;
        defJoint.initialize(player.getBody(), bodies.get(0), new Vector2(player.getBody().getPosition().x,
                player.getBody().getPosition().y), new Vector2(bodies.get(0).getPosition().x, bodies.get(0).getPosition().y));

        DistanceJoint joint = (DistanceJoint) world.createJoint(defJoint);
    }

    public void createBody(BodyDef.BodyType type, int posx, int posy) {
        // First we create a body definition
        BodyDef bodyDef = new BodyDef();
        // We set our body to dynamic, for something like ground which doesn't move we would set it to StaticBody
        bodyDef.type = type;
        // Set our body's starting position in the world
        bodyDef.position.set(posx / PPM, posy / PPM);

        // Create our body in the world using our body definition
        Body body = world.createBody(bodyDef);

        // Create a circle shape and set its radius to 6

        PolygonShape groundBox = new PolygonShape();
        groundBox.setAsBox(5 / PPM, 10 / PPM);

        // Create a fixture definition to apply our shape to
        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = groundBox;
        fixtureDef.density = 0.0f; // 0.0
        fixtureDef.friction = 0.4f; // 0.2
        fixtureDef.restitution = 0.8f; // Make it bounce a little bit 0.0

        // Create our fixture and attach it to the body
        Fixture fixture = body.createFixture(fixtureDef);

        // Remember to dispose of any shapes after you're done with them!
        // BodyDef and FixtureDef don't need disposing, but shapes do.
        groundBox.dispose();
        bodies.add(body);
    }

    public Menu(GameStateManager gsm) {
        super(gsm);

        world = new World(new Vector2(0,-5), true);
        b2dr = new Box2DDebugRenderer();
        img = new Texture("badlogic.jpg");
        createBody(BodyDef.BodyType.DynamicBody, 100, 300);
        createPlayer(150, 150);
        createGround();
    }

    @Override
    public void handleInput(float dt) {
        Vector2 vel = this.player.getBody().getLinearVelocity();
        Vector2 pos = this.player.getBody().getPosition();

        // apply left impulse, but only if max velocity is not reached yet
        if (MyInput.isDown(MyInput.BUTTON_A) && vel.x > -Player.MAX_VELOCITY) {
            this.player.getBody().applyLinearImpulse(-0.5f, 0, pos.x, pos.y, true);
            System.out.println(this.player.getBody().getPosition().x);
        }
        // apply right impulse, but only if max velocity is not reached yet
        if (MyInput.isDown(MyInput.BUTTON_D) && vel.x < Player.MAX_VELOCITY) {
            this.player.getBody().applyLinearImpulse(0.5f, 0, pos.x, pos.y, true);
        }
        // apply right impulse, but only if max velocity is not reached yet
        System.out.println(vel.y);
        if (MyInput.isDown(MyInput.BUTTON_W) && vel.y <= 0) {
            this.player.getBody().applyLinearImpulse(0f, 2f, pos.x, pos.y, true);
        }

    }

    @Override
    public void update(float dt) {
        handleInput(dt);
        render();
    }

    @Override
    public void render() {
        Gdx.gl.glClearColor(81 / 255f, 72 / 255f, 72 / 255f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        sb.begin();
        //sb.draw(img, img_x, img_y);
        sb.end();

        b2dr.render(world, cam.combined);
        world.step(STEP, 6, 2);
    }

    @Override
    public void dispose() {
        img.dispose();
    }
}
