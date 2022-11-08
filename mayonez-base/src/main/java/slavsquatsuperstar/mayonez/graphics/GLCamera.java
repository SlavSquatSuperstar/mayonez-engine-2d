package slavsquatsuperstar.mayonez.graphics;

import org.joml.Matrix4f;
import org.joml.Vector2f;
import org.joml.Vector3f;
import slavsquatsuperstar.mayonez.math.Vec2;
import slavsquatsuperstar.mayonez.annotations.EngineType;
import slavsquatsuperstar.mayonez.annotations.UsesEngine;

/**
 * A scene camera for the GL engine.
 *
 * @author SlavSquatSuperstar
 */
@UsesEngine(EngineType.GL)
public final class GLCamera extends Camera {

    // Renderer Fields
    private Matrix4f projectionMatrix, inverseProjection; // coordinates from camera's perspective
    private Matrix4f viewMatrix, inverseView; // normalized device screen coordinates
    private float nearPlane = 0f; // closest object visible
    private float farPlane = 100f; // farthest object visible
    private float zPosition = 0f;

    public GLCamera(Vec2 screenSize, float sceneScale) {
        super(screenSize, sceneScale);
        projectionMatrix = new Matrix4f();
        viewMatrix = new Matrix4f();
        inverseProjection = new Matrix4f();
        inverseView = new Matrix4f();
        adjustProjection();
    }

    @Override
    public void update(float dt) {
        if (getSubject() != null) transform.position.set(getSubject().transform.position);
    }

    // Renderer Methods

    public void adjustProjection() {
        projectionMatrix.identity();
        projectionMatrix.ortho(0, screenSize.x, 0, screenSize.y, nearPlane, farPlane);
        projectionMatrix.invert(inverseProjection);
    }

    public Matrix4f getViewMatrix() {
        Vector3f cameraFront = new Vector3f(0, 0, -1);
        Vector3f cameraUp = new Vector3f(0, 1, 0);
        Vector2f offset = getOffset().toJOML();
        viewMatrix.identity();
        viewMatrix.lookAt(new Vector3f(offset, zPosition), cameraFront.add(offset.x, offset.y, 0), cameraUp);
        viewMatrix.invert(inverseView);
        return viewMatrix;
    }

    public Matrix4f getProjectionMatrix() {
        return projectionMatrix;
    }

    public Matrix4f getInverseProjection() {
        return inverseProjection;
    }

    public Matrix4f getInverseView() {
        return inverseView;
    }

}
