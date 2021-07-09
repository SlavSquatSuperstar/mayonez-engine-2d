package slavsquatsuperstar.mayonez;

import slavsquatsuperstar.mayonez.components.scripts.DragAndDrop;

// TODO set keep in scene, to parent?
public class Camera extends Script {

    private int width, height;
    private int minX, minY, maxX, maxY;
    private float lastMx, lastMy;
    private GameObject subject;

    public Camera(int sceneWidth, int sceneHeight) {
        width = Preferences.SCREEN_WIDTH;
        height = Preferences.SCREEN_HEIGHT;
        minX = 0;
        minY = 0;//-28; // account for the bar on top of the window
        maxX = sceneWidth - width;
        maxY = sceneHeight - height;
    }

    // Static (Factory) Methods
    /**
     * Creates a container {@link GameObject} to hold a Camera Object
     *
     * @param camera an Camera instance
     * @return the object
     */
    public static GameObject createCameraObject(Camera camera) {
        return new GameObject("Camera", new Vector2()) {
            @Override
            protected void init() {
                addComponent(camera);
                addComponent(new DragAndDrop("right mouse", true)); // Keep camera inside scene
//                addComponent(new KeepInScene(0, 0, scene.getWidth(), scene.getHeight(), KeepInScene.Mode.STOP));
                // add camera collider/trigger
            }

            // Don't want to get rid of the camera!
            @Override
            public void destroy() { }

            @Override
            public boolean isDestroyed() {
                return false;
            }
        };
    }

    @Override
    public void update(float dt) {
        // Reset camera position with double click
        if (Game.mouse().buttonDown("right mouse") && Game.mouse().clicks() == 2) {
            parent.setX(0);
            parent.setY(0);
        }
        // Follow subject (Set camera's center equal to subject's center)
        if (subject != null) {
            parent.setX((subject.getX()) - width / 2f);
            parent.setY((subject.getY()) - height / 2f);
        }
    }

    public float getX() {
        return parent.getX();
    }

    public float getY() {
        return parent.getY();
    }

    /**
     * Sets a subject for this Camera to follow, or disables subject following.
     *
     * @param subject a {@link GameObject} in the scene
     */
    public void setSubject(GameObject subject) {
        this.subject = subject;
        parent.keepInScene = subject.keepInScene;
    }
}
