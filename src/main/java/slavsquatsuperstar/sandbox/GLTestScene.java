package slavsquatsuperstar.sandbox;

import org.joml.Vector2f;
import org.joml.Vector4f;
import slavsquatsuperstar.math.Vec2;
import slavsquatsuperstar.mayonez.GameObject;
import slavsquatsuperstar.mayonezgl.GameGL;
import slavsquatsuperstar.mayonezgl.SceneGL;
import slavsquatsuperstar.mayonezgl.renderer.CameraGL;
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
//        texture = new SpriteGL("src/main/resources/assets/mario.png");

        // Set camera position
        camera = new CameraGL(new Vector2f(-250, -0));

        int xOffset = 10;
        int yOffset = 10;

        float totalWidth = (float) (600 - xOffset * 2);
        float totalHeight = (float) (300 - yOffset * 2);
        float sizeX = totalWidth / 100f;
        float sizeY = totalHeight / 100f;
        float padding = 0;

        for (int x = 0; x < 100; x++) {
            for (int y = 0; y < 100; y++) {
                float xPos = xOffset + (x * sizeX) + (padding * x);
                float yPos = yOffset + (y * sizeY) + (padding * y);
                addObject(new GameObject(String.format("Object (%d, %d)", x, y), new Vec2(xPos, yPos)) {
                    @Override
                    protected void init() {
                        transform.resize(new Vec2(sizeX, sizeY));
                        addComponent(new SpriteRenderer(new Vector4f(xPos / totalWidth, yPos / totalHeight, 0.5f, 1)));
                    }
                });
            }
        }
    }

}
