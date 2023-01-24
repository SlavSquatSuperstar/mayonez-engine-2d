package mayonez.graphics;

import mayonez.SceneManager;
import mayonez.annotations.EngineType;
import mayonez.annotations.UsesEngine;
import mayonez.math.FloatMath;
import mayonez.math.Vec2;
import org.joml.Matrix4f;
import org.joml.Quaternionf;
import org.joml.Vector3f;
import org.joml.Vector4f;

/**
 * A scene camera for the GL engine.
 *
 * @author SlavSquatSuperstar
 */
@UsesEngine(EngineType.GL)
public final class GLCamera extends Camera {

    // GL Renderer Fields
    private Matrix4f projMatrix, invProjMatrix; // coordinates from camera's perspective
    private Matrix4f viewMatrix, invViewMatrix; // normalized device screen coordinates
    private float nearPlane = -100f; // closest object visible
    private float farPlane = 100f; // farthest object visible
    private float zPosition = 0f; // how far forward/back the camera is

    public GLCamera(Vec2 screenSize, float sceneScale) {
        super(screenSize, sceneScale);
        projMatrix = new Matrix4f();
        viewMatrix = new Matrix4f();
        invProjMatrix = new Matrix4f();
        invViewMatrix = new Matrix4f();
    }

    // Camera Methods

    @Override
    public Vec2 toWorld(Vec2 screen) {
        var flippedPos = new Vec2(screen.x, screenSize.y - screen.y); // Mirror y
        var worldPos = flippedPos.div(screenSize).mul(2f).sub(new Vec2(1f)).div(sceneScale);
        var temp = new Vector4f(worldPos.x, worldPos.y, 0f, 0f);
        temp.mul(getInverseProjection()).mul(getInverseView());
        return new Vec2(temp.x, temp.y).add(getPosition());
    }


    // Projection Matrix Methods

    public Matrix4f getProjectionMatrix() {
        var projSize = screenSize.div(getZoom());
        projMatrix.setOrtho(0, projSize.x, 0, projSize.y, nearPlane, farPlane);
        projMatrix.invert(invProjMatrix);
        return projMatrix;
    }

    public Matrix4f getInverseProjection() {
        return invProjMatrix;
    }

    // View Matrix Methods

    public Matrix4f getViewMatrix() {
        // Translate view matrix
        var cameraFront = new Vector3f(0, 0, -1);
        var cameraUp = new Vector3f(0, 1, 0);
        var offset = getOffset();
        viewMatrix.setLookAt(new Vector3f(offset.x, offset.y, zPosition), cameraFront.add(offset.x, offset.y, 0f), cameraUp);

        // Rotate view matrix
        var cameraPos = getPosition().mul(SceneManager.getCurrentScene().getScale());
        var rotation = new Quaternionf();
        rotation.rotationAxis(FloatMath.toRadians(-getRotation()), 0, 0, 1);
        viewMatrix.rotateAround(rotation, cameraPos.x, cameraPos.y, 0);

        viewMatrix.invert(invViewMatrix);
        return viewMatrix;
    }

    public Matrix4f getInverseView() {
        return invViewMatrix;
    }

}
