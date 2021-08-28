package slavsquatsuperstar.mayonezgl;

import org.joml.Vector2f;
import slavsquatsuperstar.mayonez.GameObject;
import slavsquatsuperstar.mayonez.Scene;
import slavsquatsuperstar.mayonezgl.renderer.CameraGL;

public class SceneGL extends Scene {

    public final String name;
    protected CameraGL camera;

    public SceneGL(String name) {
        super(name);
        this.name = name;
        camera = new CameraGL(new Vector2f());
    }

    public void render() {}

    @Override
    protected void onAddObject(GameObject obj) {
        GameGL.getRenderer().addObject(obj);
    }

    public CameraGL getCamera() {
        return camera;
    }
}
