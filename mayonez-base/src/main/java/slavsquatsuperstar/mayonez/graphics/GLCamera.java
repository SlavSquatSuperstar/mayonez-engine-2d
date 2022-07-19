package slavsquatsuperstar.mayonez.graphics;

import org.joml.Matrix4f;
import org.joml.Vector2f;
import org.joml.Vector3f;
import slavsquatsuperstar.math.Vec2;
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

    private Matrix4f projectionMatrix, viewMatrix;
    private Vector2f position;
    public float nearPlane = 0f;
    public float farPlane = 100f;
    public float zPosition = 0f;

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
        viewMatrix.identity();
        viewMatrix.lookAt(new Vector3f(position.x, position.y, zPosition),
                cameraFront.add(position.x, position.y, 0),
                cameraUp);
        return viewMatrix;
    }

    public Matrix4f getProjectionMatrix() {
        return projectionMatrix;
    }

    @Override
    public Vec2 getOffset() {
        return new Vec2(position);
    }

    @Override
    public void setOffset(Vec2 offset) {
        this.position = offset.toJOML();
    }
}
