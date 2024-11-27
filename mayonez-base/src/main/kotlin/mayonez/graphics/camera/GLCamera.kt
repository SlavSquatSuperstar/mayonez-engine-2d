package mayonez.graphics.camera

import mayonez.application.*
import mayonez.graphics.*
import mayonez.math.*
import org.joml.*

/**
 * A scene camera for the GL engine.
 *
 * Sources:
 * - https://learnopengl.com/Getting-started/Camera
 * - https://www.opengl-tutorial.org/beginners-tutorials/tutorial-3-matrices/
 * - https://www.khronos.org/opengl/wiki/Viewing_and_Transformations
 * - https://gamedevbeginner.com/how-to-zoom-a-camera-in-unity-3-methods-with-examples/
 *
 * @author SlavSquatSuperstar
 */
@UsesEngine(EngineType.GL)
class GLCamera(screenSize: Vec2) : Camera(screenSize) {

    // Matrix Fields
    private val viewMatrix: Matrix4f = Matrix4f()
    private val inverseView: Matrix4f = Matrix4f()
    private val projectionMatrix: Matrix4f = Matrix4f()
    private val inverseProjection: Matrix4f = Matrix4f()

    // Plane Fields
    private val nearPlane = -100f // closest object visible
    private val farPlane = 100f // farthest object visible
    private val cameraZ = 0f // how far forward/back the camera is

    // Camera Methods

    override fun getScreenOffset(): Vec2 = screenCenter - (screenSize * 0.5f)

    // Matrix Methods

    /**
     * The view matrix of the camera, which transforms objects from world space
     * into camera view space.
     */
    override fun getViewMatrix(): Matrix4f {
        viewMatrix
            .translateView(screenOffset, cameraZ)
            .rotateView(-rotation, screenCenter, cameraZ)
            .zoomView(zoom, screenCenter, cameraZ)
            .zoomView(cameraScale, Vec2(), cameraZ)
            .invert(inverseView)
        return viewMatrix
    }

    /**
     * The projection matrix of the camera, which transforms objects from view
     * space into normalized screen space.
     */
    override fun getProjectionMatrix(): Matrix4f {
//        GL11.glViewport()
        projectionMatrix
            .setOrtho(0f, screenSize.x, 0f, screenSize.y, nearPlane, farPlane)
            .invert(inverseProjection)
        return projectionMatrix
    }

    // Matrix Helper Methods

    private fun Matrix4f.translateView(offset: Vec2, cameraZ: Float): Matrix4f {
        val cameraFront = Vector3f(0f, 0f, -1f)
        val cameraUp = Vector3f(0f, 1f, 0f)
        return this.setLookAt(
            Vector3f(offset.x, offset.y, cameraZ),
            cameraFront.add(offset.x, offset.y, 0f), cameraUp
        )
    }

    private fun Matrix4f.rotateView(angle: Float, cameraPos: Vec2, cameraZ: Float): Matrix4f {
        val rotation = Quaternionf()
        rotation.rotationAxis(MathUtils.toRadians(angle), 0f, 0f, 1f)
        return this.rotateAround(rotation, cameraPos.x, cameraPos.y, cameraZ)
    }

    private fun Matrix4f.zoomView(zoom: Float, cameraPos: Vec2, cameraZ: Float): Matrix4f {
        // Orthographic zoom
        // TODO try perspective zoom
        return this.scaleAround(zoom, zoom, 1f, cameraPos.x, cameraPos.y, cameraZ)
    }

    // Screen to World Methods

    override fun toWorldPosition(screenPos: Vec2): Vec2 {
        // Divide the raw screen coordinates by the window scaling
        val windowPos = getClipPos(screenPos / WindowProperties.getWindowScaling())
        return getViewPos(windowPos) + position
    }

    /** Normalize screen position into clip space. */
    private fun getClipPos(screenPos: Vec2): Vec2 {
        val flipped = Vec2(screenPos.x, screenSize.y - screenPos.y) // Mirror y
        return (flipped / screenSize * 2f) - Vec2(1f)
    }

    /** Transform clip position into camera view space. */
    private fun getViewPos(clipPos: Vec2): Vec2 {
        val view = Vector4f(clipPos.x, clipPos.y, 0f, 0f)
            .mul(inverseProjection).mul(inverseView)
        return Vec2(view.x, view.y)
    }

    override fun toWorldDisplacement(screenDisp: Vec2): Vec2 {
        val flippedDisp = Vec2(screenDisp.x, -screenDisp.y)
        return flippedDisp.mul(invCameraScale)
    }

}