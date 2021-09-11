package slavsquatsuperstar.sandbox;

import org.joml.Vector2f;
import slavsquatsuperstar.math.Vec2;
import slavsquatsuperstar.mayonez.GameObject;
import slavsquatsuperstar.mayonez.Transform;
import slavsquatsuperstar.mayonezgl.GameGL;
import slavsquatsuperstar.mayonezgl.SceneGL;
import slavsquatsuperstar.mayonezgl.renderer.CameraGL;
import slavsquatsuperstar.mayonezgl.renderer.SpriteGL;
import slavsquatsuperstar.mayonezgl.renderer.SpriteRenderer;

public class GLTestScene extends SceneGL {

    public GLTestScene() {
        super("LWJGL Test Scene");
    }

    public static void main(String[] args) {
        GameGL game = GameGL.instance();
        GameGL.setScene(new GLTestScene());
        game.start();
    }

    @Override
    public void init() {
        // Load resources
//        texture = new SpriteGL("assets/mario.png");
        SpriteGL texture1 = new SpriteGL("src/main/resources/assets/textures/mario.png");
        SpriteGL texture2 = new SpriteGL("src/main/resources/assets/textures/goomba.png");
        camera = new CameraGL(new Vector2f(-250, -0));

        addObject(new GameObject("Test Object 1", new Transform(
                new Vec2(100, 100), new Vec2(256, 256)
        )) {
            @Override
            protected void init() {
                addComponent(new SpriteRenderer(texture1));
            }
        });

        addObject(new GameObject("Test Object 2", new Transform(
                new Vec2(400, 100), new Vec2(256, 256)
        )) {
            @Override
            protected void init() {
                addComponent(new SpriteRenderer(texture2));
            }
        });
    }

}
