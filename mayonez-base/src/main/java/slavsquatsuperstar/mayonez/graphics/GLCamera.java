package slavsquatsuperstar.mayonez.graphics;

import org.joml.Matrix4f;
import org.joml.Vector2f;
import org.joml.Vector3f;
import slavsquatsuperstar.math.Vec2;
import slavsquatsuperstar.mayonez.GameObject;
import slavsquatsuperstar.mayonez.annotations.EngineType;
import slavsquatsuperstar.mayonez.annotations.UsesEngine;

/**
 * A scene camera for the GL engine.
 *
 * @author SlavSquatSuperstar
 */
@UsesEngine(EngineType.GL)
public final class GLCamera implements Camera {

    // Camera Fields

    private Vec2 position, screenSize;
    private float sceneScale;

    // Renderer Fields

    private Matrix4f projectionMatrix, viewMatrix;
    private float nearPlane = 0f;
    private float farPlane = 100f;
    private float zPosition = 0f;

    // Camera Movement
    private CameraMode mode = CameraMode.FIXED;
    private GameObject subject = null;

    public GLCamera(Vec2 position, Vec2 screenSize, float sceneScale) {
        this.position = position;
        this.screenSize = screenSize;
        this.sceneScale = sceneScale;
        projectionMatrix = new Matrix4f();
        viewMatrix = new Matrix4f();
        adjustProjection();
    }

    public void adjustProjection() {
        projectionMatrix.identity();
        projectionMatrix.ortho(0, screenSize.x, 0, screenSize.y, nearPlane, farPlane);
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
        if (mode == CameraMode.FOLLOW) return subject.transform.position;
        return new Vec2(position);
    }

    @Override
    public void setPosition(Vec2 position) {
        this.position = position;
    }

    @Override
    public Vec2 getOffset() {
        return getPosition().mul(sceneScale).sub(screenSize.mul(0.5f));
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
        setMode(CameraMode.FOLLOW);
        return this;
    }

    @Override
    public String toString() {
        return "GL Camera";
    }
}
