package slavsquatsuperstar.mayonez;

import slavsquatsuperstar.mayonez.components.scripts.DragAndDrop;
import slavsquatsuperstar.mayonez.components.scripts.KeepInScene;

// TODO set keep in scene, to parent?
public class Camera extends Script {

    private float width, height; // in world units
    private int minX, minY, maxX, maxY;
    private GameObject subject;

    public Camera(int sceneWidth, int sceneHeight) {
        width = (float) Preferences.SCREEN_WIDTH / Preferences.TILE_SIZE;
        height = (float) Preferences.SCREEN_HEIGHT / Preferences.TILE_SIZE;
        minX = 0;
        minY = 0;//-28; // account for the bar on top of the window
        maxX = sceneWidth;
        maxY = sceneHeight;
    }

    // Static (Factory) Methods

    /**
     * Creates a container {@link GameObject} to hold a Camera object
     *
     * @param camera the camera instance
     * @return the object
     */
    public static GameObject createCameraObject(Camera camera) {
        return new GameObject("Camera", new Vector2()) {
            @Override
            protected void init() {
                addComponent(camera);
                addComponent(new DragAndDrop("right mouse", true));
                // Keep camera inside scene and add camera collider
                addComponent(new KeepInScene(camera.minX, camera.minY, camera.maxX, camera.maxY, KeepInScene.Mode.STOP));
            }

            // Don't want to get rid of the camera!
            @Override
            public void destroy() {
            }

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
        // Follow subject (Set position to subject position)
        if (subject != null) {
            parent.setX((subject.getX()));
            parent.setY((subject.getY()));
        }
    }

    // Getters and setters

    public float getOffsetX() {
        return parent.getX();
    }

    public float getOffsetY() {
        return parent.getY();
    }

    public Vector2 getMin() {
        return new Vector2(getOffsetX() - width * 0.5f, getOffsetY() - height * 0.5f);
    }

    /**
     * Toggles the camera's ability to stay within the scene bounds.
     *
     * @param enabled if the camera should stay inside the scene
     * @return the camera
     */
    public Camera setKeepInScene(boolean enabled) {
        parent.getComponent(KeepInScene.class).setEnabled(enabled);
        return this;
    }

    /**
     * Sets a subject for this Camera to follow, or enables free camera movement.
     *
     * @param subject A {@link GameObject} in the scene. Set to null to disable subject following.
     * @return the camera
     */
    public Camera setSubject(GameObject subject) {
        this.subject = subject;
        parent.getComponent(DragAndDrop.class).setEnabled(subject == null);
        if (subject != null)
            setKeepInScene(subject.getComponent(KeepInScene.class) != null);
        return this;
    }
}
