package slavsquatsuperstar.sandbox;

import org.joml.Vector2f;
import slavsquatsuperstar.math.Vec2;
import slavsquatsuperstar.mayonez.GameObject;
import slavsquatsuperstar.mayonez.Transform;
import slavsquatsuperstar.mayonezgl.GameGL;
import slavsquatsuperstar.mayonezgl.SceneGL;
import slavsquatsuperstar.mayonezgl.SpriteSheetGL;
import slavsquatsuperstar.mayonezgl.renderer.CameraGL;

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
        SpriteSheetGL sprites = new SpriteSheetGL("assets/textures/spritesheet.png", 16, 16, 26, 0);
        camera = new CameraGL(new Vector2f(-250, -0));

        addObject(new GameObject("Test Object 1", new Transform(
                new Vec2(50, 100), new Vec2(128, 128)
        )) {
            @Override
            protected void init() {
                addComponent(sprites.getSprite(0));
            }
        });

        addObject(new GameObject("Test Object 2", new Transform(
                new Vec2(450, 100), new Vec2(128, 128)
        )) {
            @Override
            protected void init() {
                addComponent(sprites.getSprite(14));
            }
        });
    }

}
