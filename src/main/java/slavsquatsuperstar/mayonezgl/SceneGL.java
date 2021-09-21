package slavsquatsuperstar.mayonezgl;

import org.joml.Vector2f;
import slavsquatsuperstar.mayonez.GameObject;
import slavsquatsuperstar.mayonez.Scene;
import slavsquatsuperstar.mayonezgl.renderer.CameraGL;

public class SceneGL extends Scene {

    protected CameraGL camera;

    public SceneGL(String name) {
        super(name, 1080, 720, 16);
        camera = new CameraGL(new Vector2f());
    }

    @Override
    protected void onAddObject(GameObject obj) {
        GameGL.getRenderer().addObject(obj);
    }

    public CameraGL getCamera() {
        return camera;
    }
}
