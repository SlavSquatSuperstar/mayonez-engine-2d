package mayonez.graphics;

import mayonez.annotations.EngineType;
import mayonez.annotations.UsesEngine;
import mayonez.math.Vec2;
import org.joml.Matrix4f;
import org.joml.Vector2f;
import org.joml.Vector3f;

/**
 * A scene camera for the GL engine.
 *
 * @author SlavSquatSuperstar
 */
@UsesEngine(EngineType.GL)
public final class GLCamera extends Camera {

    // Renderer Fields
    private Matrix4f projMatrix, invProjMatrix; // coordinates from camera's perspective
    private Matrix4f viewMatrix, invViewMatrix; // normalized device screen coordinates
    private float nearPlane = 0f; // closest object visible
    private float farPlane = 100f; // farthest object visible
    private float zPosition = 0f;

    public GLCamera(Vec2 screenSize, float sceneScale) {
        super(screenSize, sceneScale);
        projMatrix = new Matrix4f();
        viewMatrix = new Matrix4f();
        invProjMatrix = new Matrix4f();
        invViewMatrix = new Matrix4f();
        adjustProjection();
    }

    @Override
    public void update(float dt) {
        if (getSubject() != null) transform.position.set(getSubject().transform.position);
    }

    // Projection Matrix Methods

    public void adjustProjection() {
        projMatrix.identity();
        projMatrix.ortho(0, screenSize.x, 0, screenSize.y, nearPlane, farPlane);
        projMatrix.invert(invProjMatrix);
    }

    public Matrix4f getProjectionMatrix() {
        return projMatrix;
    }

    public Matrix4f getInverseProjection() {
        return invProjMatrix;
    }

    // View Matrix Methods

    public Matrix4f getViewMatrix() {
        Vector3f cameraFront = new Vector3f(0, 0, -1);
        Vector3f cameraUp = new Vector3f(0, 1, 0);
        Vector2f offset = getOffset().toJOML();
        viewMatrix.identity();
        viewMatrix.lookAt(new Vector3f(offset, zPosition), cameraFront.add(offset.x, offset.y, 0), cameraUp);
        viewMatrix.invert(invViewMatrix);
        return viewMatrix;
    }

    public Matrix4f getInverseView() {
        return invViewMatrix;
    }

}
