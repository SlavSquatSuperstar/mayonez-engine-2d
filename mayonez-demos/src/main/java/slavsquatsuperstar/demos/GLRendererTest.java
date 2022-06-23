package slavsquatsuperstar.demos;

import org.joml.Vector2f;
import slavsquatsuperstar.math.MathUtils;
import slavsquatsuperstar.math.Vec2;
import slavsquatsuperstar.mayonez.GameObject;
import slavsquatsuperstar.mayonez.Mayonez;
import slavsquatsuperstar.mayonez.Scene;
import slavsquatsuperstar.mayonez.Transform;
import slavsquatsuperstar.mayonez.fileio.Assets;
import slavsquatsuperstar.mayonez.fileio.GLTexture;
import slavsquatsuperstar.mayonez.graphics.GLCamera;
import slavsquatsuperstar.mayonez.graphics.GLSprite;
import slavsquatsuperstar.mayonez.graphics.GLSpriteSheet;
import slavsquatsuperstar.mayonez.physics2d.Rigidbody2D;
import slavsquatsuperstar.mayonez.physics2d.colliders.BoxCollider2D;
import slavsquatsuperstar.mayonez.scripts.DragAndDrop;
import slavsquatsuperstar.mayonez.scripts.KeyMovement;
import slavsquatsuperstar.mayonez.scripts.MoveMode;

import java.util.ArrayList;
import java.util.List;

public class GLRendererTest extends Scene {

    private GLCamera camera;
    private GLSpriteSheet sprites;
    private List<GameObject> enemies;

    public GLRendererTest() {
        super("LWJGL Test Scene", 1080, 720, 32);
        camera = new GLCamera(new Vector2f());
        enemies = new ArrayList<>();
        setGravity(new Vec2());
    }

    @Override
    public void init() {
        addObject(new GameObject("Red Square", new Transform(new Vec2(6, 6), new Vec2(4, 4)), -2) {
            @Override
            protected void init() {
                addComponent(new GLSprite(Assets.getAsset("assets/textures/blend_red.png", GLTexture.class)));
            }
        });

        addObject(new GameObject("Green Square", new Transform(new Vec2(9, 6), new Vec2(4, 4)), 2) {
            @Override
            protected void init() {
                addComponent(new GLSprite(Assets.getAsset("assets/textures/blend_green.png", GLTexture.class)));
            }
        });

        // Load resources
        sprites = new GLSpriteSheet("assets/textures/spritesheet.png", 16, 16, 26, 0);

        addObject(new GameObject("Mario", new Transform(
                new Vec2(20, 6), new Vec2(2, 2)
        )) {
            @Override
            protected void init() {
                addComponent(sprites.getSprite(0));
                addComponent(new BoxCollider2D(new Vec2(0.8f, 1)));
                addComponent(new Rigidbody2D(1f).setFixedRotation(true));
                addComponent(new KeyMovement(MoveMode.POSITION, 20));
            }
        }.setZIndex(1));

        for (int i = 0; i < 8; i++) addObject(createObject("Goomba", 14));
    }

    private GameObject createObject(String name, int spriteIndex) {
        return new GameObject(name, new Vec2(MathUtils.random(0, getWidth()), MathUtils.random(0, getHeight()))) {
            @Override
            protected void init() {
                addComponent(sprites.getSprite(spriteIndex));
                addComponent(new BoxCollider2D(new Vec2(0.8f, 1)));
                addComponent(new Rigidbody2D(1f).setFixedRotation(true));
                addComponent(new DragAndDrop("left mouse"));
            }

            @Override
            public void onCollision(GameObject other) {
                destroy();
            }
        };
    }

    @Override
    public GLCamera getCamera() {
        return camera;
    }

    public static void main(String[] args) {
        Mayonez.setUseGL(true);
        Mayonez.setScene(new GLRendererTest());
        Mayonez.start();
    }

}
