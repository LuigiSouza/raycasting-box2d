package com.mygdx.game.state;

import box2dLight.RayHandler;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.physics.box2d.*;
import com.mygdx.game.entities.Player;
import com.mygdx.game.handlers.GameStateManager;
import com.mygdx.game.handlers.MyInput;
import com.mygdx.game.handlers.Tuple;

import javax.sound.midi.SysexMessage;
import java.awt.*;
import java.awt.geom.RectangularShape;
import java.util.ArrayList;

import static com.mygdx.game.raycastingGame.STEP;
import static com.mygdx.game.raycastingGame.PPM;

public class Menu extends GameState{
    private int img_x = 0;
    private int img_y = 0;

    private final Texture img;
    private final World world;
    private final Box2DDebugRenderer b2dr;

    private final Player player;
    private ArrayList<Body> bodies = new ArrayList<Body>();

    private Vector2 collisionPoint = new Vector2();
    float closestFraction = 1.0f;

    private final int rays = 60;
    private final ArrayList<Tuple<Double, Vector2>> distance = new ArrayList<>();

    public static final int width = 9;
    public static final int height = 8;
    private static final int[] field = {
            1, 1, 1, 1, 1, 1, 1, 1, 1,
            1, 0, 0, 0, 0, 0, 0, 0, 1,
            1, 0, 0, 0, 0, 0, 0, 0, 1,
            1, 0, 0, 0, 0, 1, 0, 0, 1,
            1, 0, 0, 0, 0, 0, 1, 0, 1,
            1, 0, 0, 0, 0, 0, 0, 1, 1,
            1, 0, 0, 0, 0, 0, 0, 0, 1,
            1, 1, 1, 1, 1, 1, 1, 1, 1
    };

    RayCastCallback callback = new RayCastCallback() {

        @Override
        public float reportRayFixture(Fixture fixture, Vector2 point, Vector2 normal, float fraction) {

            if ( fraction < Menu.this.closestFraction ) {
                collisionPoint = point;
                //System.out.format("Colpoint: %.2f %.2f with %.2f %.2f\n", collisionPoint.x,collisionPoint.y, normal.x, normal.y);
                closestFraction = fraction;
                System.out.println(point);
                System.out.println(normal + "\n");
                distance.add(new Tuple<Double, Vector2>((double) fraction, new Vector2(normal)));
                return fraction;
            }
            return 1;
        }
    };


    public void createField() {
        int size = 50;
        BodyDef groundBodyDef;
        for(int i = 0; i < width; i++) {
            for(int j = 0; j < height; j++) {
                if(field[i + (width*j)] == 0)
                    continue;

                groundBodyDef = new BodyDef();
                groundBodyDef.position.set(new Vector2((size / PPM) * (i+0.5f), (size / PPM) * (height-j-0.5f)));

                Body groundBody = world.createBody(groundBodyDef);

                PolygonShape groundBox = new PolygonShape();

                groundBox.setAsBox(size / (2*PPM), size / (2*PPM));
                groundBody.createFixture(groundBox, 0.0f);

                groundBox.dispose();
            }
            System.out.print('\n');
        }
    }

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
        groundBox.setAsBox(5 / PPM, 5 / PPM);

        // Create a fixture definition to apply our shape to
        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = groundBox;
        fixtureDef.density = 0.0f; // 0.0
        fixtureDef.friction = 0.0f; // 0.2
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

        world = new World(new Vector2(0,0), true);
        b2dr = new Box2DDebugRenderer();
        img = new Texture("badlogic.jpg");
        //createBody(BodyDef.BodyType.DynamicBody, 100, 300);
        player = new Player(150, 150, this.world);
        //createGround();
        createField();
    }

    private void updateRaycast() {

        distance.clear();
        double degree = Math.toDegrees(player.getAngle());
        degree -= rays/2f;
        /*
        float x = player.getBody().getPosition().x;
        float y = player.getBody().getPosition().y;
        double angle = player.getAngle();

        x = (float)Math.cos(angle)*(x*4);
        y = (float)Math.sin(angle)*(y*4);

        //createBody(BodyDef.BodyType.StaticBody, (int) (x * PPM), (int) (y*PPM));

        world.rayCast(callback, player.getBody().getPosition().x,
                player.getBody().getPosition().y,
                x,
                y);*/
        System.out.println("start");

        for (double i = degree; i <= degree+(rays/2f); i++) {
            closestFraction = 1.0f;
            float x = player.getBody().getPosition().x;
            float y = player.getBody().getPosition().y;
            double angle = Math.toRadians(i);
            x = (float)Math.cos(angle)*(x*4);
            y = (float)Math.sin(angle)*(y*4);

            int size = distance.size();
            world.rayCast(callback, player.getBody().getPosition().x,
                    player.getBody().getPosition().y,
                    x,
                    y);
            if(distance.size() == size) {
                distance.add(new Tuple<Double, Vector2>(99999d, new Vector2(0 ,0)));
            }
        }

    }

    @Override
    public void handleInput(float dt) {
            if(MyInput.isPressed(MyInput.BUTTON_SPACE)) {
                /*
                distance.clear();
                double degree = Math.toDegrees(player.getAngle());
                degree -= rays/2f;

                for (double i = degree; i <= degree+(rays/2f); i++) {
                    closestFraction = 1f;

                    double x = player.getBody().getPosition().x;
                    double y = player.getBody().getPosition().y;
                    x =  Math.cos(i)*(x*5);
                    y = Math.sin(i)*(y+5);

                    Vector2 pos = new Vector2(player.getBody().getPosition());
                    pos.x = player.getBody().getPosition().x + (6f/PPM)*(float)Math.cos(player.getAngle());
                    pos.y = player.getBody().getPosition().y + (6f/PPM)*(float)Math.sin(player.getAngle());

                    world.rayCast(callback, pos, new Vector2((float) x,(float) y));
                }*/

                double degree = Math.toDegrees(player.getAngle());
                degree -= rays/2f;
                for (double i = degree; i <= degree+rays; i++) {
                    float x = player.getBody().getPosition().x;
                    float y = player.getBody().getPosition().y;
                    double angle = player.getAngle();
                    x = (float)Math.cos(angle)*(x+0.5f);
                    y = (float)Math.sin(angle)*(y+0.5f);

                    //createBody(BodyDef.BodyType.StaticBody, (int) (x*50), (int) (y*50));
                    //createBody(BodyDef.BodyType.StaticBody, (int)(player.getBody().getPosition().x * PPM), (int)(player.getBody().getPosition().y * PPM));
                }

                //System.out.println(distance.size());
                /*
                double x = player.getBody().getPosition().x;
                x =  Math.cos(player.getAngle())*(x*5);
                double y = player.getBody().getPosition().y;
                y = Math.sin(player.getAngle())*(y+5);

                world.rayCast(callback, player.getBody().getPosition(), new Vector2((float) x,(float) y));*/


            }
        if(MyInput.isDown(MyInput.BUTTON_ESCAPE)) {
            gsm.setState(GameStateManager.EXIT);
        }

    }

    @Override
    public void update(float dt) {
        player.update(dt);
        float closestFraction = 5.0f;

        handleInput(dt);
        updateRaycast();

        render();
    }

    @Override
    public void render() {
        Gdx.gl.glClearColor(81 / 255f, 72 / 255f, 72 / 255f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        sb.begin();
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        shapeRenderer.setColor(Color.RED);


        int k = distance.size();
        for(Tuple<Double, Vector2> v: distance) {
            Double t = v.getX();
            if(v.getY().x != 0 || v.getY().y == 1.0f) {
                shapeRenderer.setColor(Color.RED);
                shapeRenderer.rect(50 + (k * 10), (float) (t * 100) + PPM, 10, (float) Math.max((200 - 2 * PPM * t), 0));
            }else {
                shapeRenderer.setColor(Color.ORANGE);
                shapeRenderer.rect(50 + (k * 10), (float) (t * 100) + PPM, 10, (float) Math.max((200 - 2 * PPM * t), 0));
            }
            k--;
        }

        /*
        float x = player.getBody().getPosition().x;
        float y = player.getBody().getPosition().y;
        double angle = player.getAngle();
        x = PPM * (float)Math.cos(angle)+(x*PPM);
        y = PPM * (float)Math.sin(angle)+(y*PPM);
        shapeRenderer.circle(PPM * player.getBody().getPosition().x, PPM * player.getBody().getPosition().y, 6f);

        //System.out.println(x + " vs " + player.getBody().getPosition().x * PPM);

        shapeRenderer.setColor(Color.GREEN);
        shapeRenderer.circle(x, y, 6f);

        shapeRenderer.setColor(Color.YELLOW);
        shapeRenderer.circle(PPM * player.getBody().getPosition().x, PPM * player.getBody().getPosition().y, 6f);

        shapeRenderer.line(player.getBody().getPosition().x * PPM, player.getBody().getPosition().y * PPM, x, y);
        */

        double degree = Math.toDegrees(player.getAngle());
        degree -= rays/2f;
        for (double i = degree; i <= degree+rays; i++) {
            float x = player.getBody().getPosition().x;
            float y = player.getBody().getPosition().y;
            double angle = Math.toRadians(i);
            x = PPM * (float)Math.cos(angle)+(x*PPM);
            y = PPM * (float)Math.sin(angle)+(y*PPM);

            shapeRenderer.line(player.getBody().getPosition().x * PPM, player.getBody().getPosition().y * PPM, x, y);
        }

        shapeRenderer.end();
        //sb.draw(img, img_x, img_y);
        sb.end();


        b2dr.render(world, cam.combined);
        world.step(STEP, 6, 2);
    }

    @Override
    public void dispose() {
        img.dispose();
        world.dispose();
        b2dr.dispose();
        player.dispose();
    }
}
