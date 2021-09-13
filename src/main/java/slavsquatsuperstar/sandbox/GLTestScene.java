package slavsquatsuperstar.sandbox;

import org.joml.Vector2f;
import slavsquatsuperstar.fileio.Assets;
import slavsquatsuperstar.math.Vec2;
import slavsquatsuperstar.mayonez.GameObject;
import slavsquatsuperstar.mayonez.Transform;
import slavsquatsuperstar.mayonezgl.GameGL;
import slavsquatsuperstar.mayonezgl.SceneGL;
import slavsquatsuperstar.mayonezgl.renderer.CameraGL;
import slavsquatsuperstar.mayonezgl.renderer.TextureGL;
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
        TextureGL texture1 = Assets.getAsset("assets/textures/mario.png", TextureGL.class);
        TextureGL texture2 = Assets.getAsset("assets/textures/goomba.png", TextureGL.class);
        camera = new CameraGL(new Vector2f(-250, -0));

        addObject(new GameObject("Test Object 1", new Transform(
                new Vec2(50, 100), new Vec2(128, 128)
        )) {
            @Override
            protected void init() {
                addComponent(new SpriteRenderer(texture1));
            }
        });

        addObject(new GameObject("Test Object 2", new Transform(
                new Vec2(450, 100), new Vec2(128, 128)
        )) {
            @Override
            protected void init() {
                addComponent(new SpriteRenderer(texture2));
            }
        });
    }

}
