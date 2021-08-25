package slavsquatsuperstar.mayonezgl;

import org.joml.Vector2f;
import slavsquatsuperstar.mayonezgl.renderer.CameraGL;

public class SceneGL {

    public final String name;
    private boolean running = false;
    protected CameraGL camera;

    public SceneGL(String name) {
        this.name = name;
        camera = new CameraGL(new Vector2f());
    }

    public void init() {}

    public void start() {
        if (!running) {
            running = true;
            init();
        }
    }

    public void update(float dt) {
        if (!running)
            return;
    }

    public void render() {
        if (!running)
            return;
    }

}
