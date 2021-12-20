package slavsquatsuperstar.sandbox;

import slavsquatsuperstar.math.Vec2;
import slavsquatsuperstar.mayonez.GameObject;
import slavsquatsuperstar.mayonez.Transform;
import slavsquatsuperstar.mayonezgl.GameGL;
import slavsquatsuperstar.mayonezgl.SceneGL;
import slavsquatsuperstar.mayonezgl.SpriteSheetGL;

public class GLTestScene extends SceneGL {

    public GLTestScene() {
        super("LWJGL Test Scene");
    }

    public static void main(String[] args) {
        GameGL game = GameGL.instance();
        GameGL.setScene(new GLTestScene());
        GameGL.start();
    }

    @Override
    public void init() {
        // Load resources
        SpriteSheetGL sprites = new SpriteSheetGL("assets/textures/spritesheet.png", 16, 16, 26, 0);

        addObject(new GameObject("Mario", new Transform(
                new Vec2(300, 100), new Vec2(4, 4)
        )) {
            @Override
            protected void init() {
                addComponent(sprites.getSprite(0));
            }
        });

        addObject(new GameObject("Goomba", new Transform(
                new Vec2(700, 100), new Vec2(4, 4)
        )) {
            @Override
            protected void init() {
                addComponent(sprites.getSprite(14));
            }
        });
    }

}
