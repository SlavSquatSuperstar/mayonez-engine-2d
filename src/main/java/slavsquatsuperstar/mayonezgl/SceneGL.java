package slavsquatsuperstar.mayonezgl;

import org.joml.Vector2f;
import slavsquatsuperstar.mayonez.Scene;
import slavsquatsuperstar.mayonezgl.renderer.CameraGL;

public class SceneGL extends Scene {

    protected CameraGL camera;

    public SceneGL(String name, int cellSize) {
        super(name, 1080, 720, cellSize);
        camera = new CameraGL(new Vector2f());
    }

    public CameraGL getCamera() {
        return camera;
    }
}
