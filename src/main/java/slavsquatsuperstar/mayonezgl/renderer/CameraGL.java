package slavsquatsuperstar.mayonezgl.renderer;

import org.joml.Matrix4f;
import org.joml.Vector2f;
import org.joml.Vector3f;
import slavsquatsuperstar.mayonez.Preferences;

public class CameraGL {

    private Matrix4f projectionMatrix, viewMatrix;
    public Vector2f position;
    public float nearPlane = 0f;
    public float farPlane = 100f;
    public float zPosition;

    public CameraGL(Vector2f position) {
        this.position = position;
        this.projectionMatrix = new Matrix4f();
        this.viewMatrix = new Matrix4f();
        adjustProjection();
    }

    public void adjustProjection() {
        projectionMatrix.identity();
        projectionMatrix.ortho(0, Preferences.SCREEN_WIDTH, 0, Preferences.SCREEN_HEIGHT, nearPlane, farPlane);
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

//    Vec2 position;
//
//    private static Vector2f toJoml(Vec2 v) {
//        return new Vector2f(v.x, v.y);
//    }

}
