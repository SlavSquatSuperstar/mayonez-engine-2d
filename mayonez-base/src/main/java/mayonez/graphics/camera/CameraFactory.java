package mayonez.graphics.camera;

import mayonez.*;
import mayonez.physics.colliders.*;
import mayonez.scripts.*;

/**
 * A factory class that constructs {@link mayonez.graphics.camera.Camera} objects
 * depending on the engine type.
 *
 * @author SlavSquatSuperstar
 */
public final class CameraFactory {

    private CameraFactory() {
    }

    /**
     * Creates a new {@link mayonez.graphics.camera.Camera} object with the given
     * scene scale.
     *
     * @param sceneScale many pixels correspond to one world unit in the scene
     * @return the game engine
     */
    public static Camera createCamera(float sceneScale) {
        if (Mayonez.getUseGL()) {
            return new GLCamera(Mayonez.getScreenSize(), sceneScale);
        } else {
            return new JCamera(Mayonez.getScreenSize(), sceneScale);
        }
    }

    /**
     * Creates a container {@link mayonez.GameObject} to hold a scene's Camera script.
     *
     * @param camera the camera instance
     * @return the camera object
     */
    public static GameObject createCameraObject(Camera camera) {
        return new GameObject("Camera") {
            @Override
            protected void init() {
                addTag("Ignore Collisions");
                addComponent(camera);
                addComponent(camera.setDragAndDropScript(new CameraDragAndDrop("right mouse")
                        .setEnabled(false)));
                // Keep camera inside scene and add camera collider (does not work well with rotations)
                addComponent(new BoxCollider(camera.screenSize.div(camera.sceneScale))
                        .setTrigger(true));
                addComponent(camera.setKeepInSceneScript(new KeepInScene(KeepInScene.Mode.STOP)
                        .setEnabled(false)));
            }

            // Don't want to get rid of the camera!
            @Override
            public boolean isDestroyed() {
                return false;
            }

            @Override
            public void destroy() {
            }
        };
    }

}
