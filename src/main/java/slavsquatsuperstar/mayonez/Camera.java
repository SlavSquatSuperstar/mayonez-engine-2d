package slavsquatsuperstar.mayonez;

import slavsquatsuperstar.mayonez.components.scripts.DragAndDrop;
import slavsquatsuperstar.mayonez.components.scripts.KeepInScene;

public class Camera extends Script {

    private float width, height; // in world units
    private int minX, minY, maxX, maxY;
    private GameObject subject;

    public Camera(int sceneWidth, int sceneHeight, int cellSize) {
        width = (float) Preferences.SCREEN_WIDTH / cellSize;
        height = (float) Preferences.SCREEN_HEIGHT / cellSize;
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
        return new GameObject("Camera", camera.getHalfSize()) {
            @Override
            protected void init() {
                addComponent(camera);
                addComponent(new DragAndDrop("right mouse", true) {
                    // Reset camera position with double click
                    @Override
                    public void onMouseDown() {
                        if (Game.mouse().clicks() == 2) {
                            camera.setOffset(new Vector2(0, 0));
                        }
                    }
                }.setEnabled(false));
                // Keep camera inside scene and add camera collider
                addComponent(new KeepInScene(camera.minX, camera.minY, camera.maxX, camera.maxY, KeepInScene.Mode.STOP));
            }

            // Don't want to get rid of the camera!
            @Override
            public final void destroy() {}

            @Override
            public final boolean isDestroyed() {
                return false;
            }
        };
    }

    @Override
    public void update(float dt) {
        // Follow subject (Set position to subject position)
        if (subject != null)
            transform.position.set(subject.transform.position);
    }

    // Getters and setters

    public Vector2 getOffset() {
        return transform.position.sub(getHalfSize());
    }

    public void setOffset(Vector2 offset) {
        transform.position.set(offset.add(getHalfSize()));
    }

    public Vector2 getHalfSize() {
        return new Vector2(width, height).div(2);
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
