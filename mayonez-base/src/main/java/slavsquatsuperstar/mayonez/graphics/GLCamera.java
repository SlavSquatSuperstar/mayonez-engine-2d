package slavsquatsuperstar.mayonez.graphics;

import org.joml.Matrix4f;
import org.joml.Vector2f;
import org.joml.Vector3f;
import slavsquatsuperstar.math.Vec2;
import slavsquatsuperstar.mayonez.GameObject;
import slavsquatsuperstar.mayonez.Preferences;
import slavsquatsuperstar.mayonez.annotations.EngineType;
import slavsquatsuperstar.mayonez.annotations.UsesEngine;

/**
 * A scene camera for the GL engine.
 *
 * @author SlavSquatSuperstar
 */
@UsesEngine(EngineType.GL)
public final class GLCamera implements Camera {

    // Renderer
    private Matrix4f projectionMatrix, viewMatrix;
    private Vector2f position;
    public float nearPlane = 0f;
    public float farPlane = 100f;
    public float zPosition = 0f;

    // Camera Movement
    private CameraMode mode = CameraMode.FIXED;
    private GameObject subject = null;

    public GLCamera(Vector2f position) {
        this.position = position;
        projectionMatrix = new Matrix4f();
        viewMatrix = new Matrix4f();
        adjustProjection();
    }

    public void adjustProjection() {
        projectionMatrix.identity();
        projectionMatrix.ortho(0, Preferences.getScreenWidth(), 0, Preferences.getScreenHeight(), nearPlane, farPlane);
    }

    public Matrix4f getViewMatrix() {
        Vector3f cameraFront = new Vector3f(0, 0, -1);
        Vector3f cameraUp = new Vector3f(0, 1, 0);
        Vector2f offset = getOffset().toJOML();
        viewMatrix.identity();
        viewMatrix.lookAt(new Vector3f(offset.x, offset.y, zPosition),
                cameraFront.add(offset.x, offset.y, 0),
                cameraUp);
        return viewMatrix;
    }

    public Matrix4f getProjectionMatrix() {
        return projectionMatrix;
    }

    // Camera Position Methods

    @Override
    public Vec2 getPosition() {
        return new Vec2(position);
    }

    @Override
    public void setPosition(Vec2 position) {
        this.position = position.toJOML();
    }

    @Override
    public Vec2 getOffset() {
        return new Vec2(position);
    }

    // Camera Mode Methods

    @Override
    public CameraMode getMode() {
        return mode;
    }

    @Override
    public GLCamera setMode(CameraMode mode) {
        this.mode = mode;
        return this;
    }

    @Override
    public GameObject getSubject() {
        return subject;
    }

    @Override
    public GLCamera setSubject(GameObject subject) {
        this.subject = subject;
        return this;
    }

}
