package slavsquatsuperstar.mayonezgl;

public class SceneGL {

    final String name;
    private boolean running = false;

    public SceneGL(String name) {
        this.name = name;
    }

    public void init() {
    }

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
