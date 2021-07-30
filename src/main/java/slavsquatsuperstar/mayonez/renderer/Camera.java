package slavsquatsuperstar.mayonez.renderer;

import slavsquatsuperstar.mayonez.*;
import slavsquatsuperstar.mayonez.physics2d.primitives.AlignedBoxCollider2D;
import slavsquatsuperstar.mayonez.scripts.DragAndDrop;
import slavsquatsuperstar.mayonez.scripts.KeepInScene;

/**
 * The viewport into the world.
 *
 * @author SlavSquatSuperstar
 */
// TODO reference parent scripts
public class Camera extends Script {

    private float width, height; // In world units
    private int minX, minY, maxX, maxY;
    private GameObject subject;
    private Script keepInScene, dragAndDrop; // Reference to parent scripts

    public Camera(int sceneWidth, int sceneHeight, int cellSize) {
        width = (float) Preferences.SCREEN_WIDTH / cellSize;
        height = (float) Preferences.SCREEN_HEIGHT / cellSize;
        minX = 0;
        minY = 0; //(int) (-28f / cellSize); // account for the bar on top of the window
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
                // Allow camera to be moved with mouse
                addComponent(camera.dragAndDrop = new DragAndDrop("right mouse", true) {
                    // Reset camera position with double click
                    @Override
                    public void onMouseDown() {
                        if (Game.mouse().clicks() == 2) {
                            camera.setOffset(new Vector2(0, 0));
                        }
                    }
                }.setEnabled(false));
                // Keep camera inside scene and add camera collider
                addComponent(new AlignedBoxCollider2D(new Vector2(camera.width, camera.height)).setTrigger(true));
                addComponent(camera.keepInScene = new KeepInScene(camera.minX, camera.minY, camera.maxX, camera.maxY, KeepInScene.Mode.STOP));
            }

            // Don't want to get rid of the camera!
            @Override
            public final void destroy() {
            }

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
     * Toggles the camera's ability to be dragged with the mouse.
     *
     * @param enabled If mouse should move the camera. Set to true to disable subject following.
     * @return this object
     */
    public Camera enableFreeMovement(boolean enabled) {
        dragAndDrop.setEnabled(enabled);
        if (enabled && subject != null) {
            subject = null;
            Logger.log("Subject following for %s has been disabled because freecam was enabled.", this, dragAndDrop);
        }
        return this;
    }

    /**
     * Toggles the camera's ability to stay within the scene bounds.
     *
     * @param enabled if the camera should stay inside the scene
     * @return this object
     */
    public Camera enableKeepInScene(boolean enabled) {
        keepInScene.setEnabled(enabled);
        return this;
    }

    /**
     * Sets a subject for this Camera to follow, disabling free camera movement.
     *
     * @param subject A {@link GameObject} in the scene. Set to null to disable subject following.
     * @return this object
     */
    public Camera setSubject(GameObject subject) {
        this.subject = subject;
        if (subject != null) {
//            enableKeepInScene(subject.getComponent(KeepInScene.class) != null);
            if (dragAndDrop.isEnabled()) {
                dragAndDrop.setEnabled(false);
                Logger.log("Freecam for %s has been disabled because a subject was set.", this, dragAndDrop);
            }
        }
        return this;
    }
}
