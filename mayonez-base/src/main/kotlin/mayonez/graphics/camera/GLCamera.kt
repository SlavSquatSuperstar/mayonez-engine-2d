package mayonez.graphics.camera

import mayonez.*
import mayonez.annotations.*
import mayonez.math.*
import org.joml.*

/**
 * A scene camera for the GL engine.
 *
 * @author SlavSquatSuperstar
 */
@UsesEngine(EngineType.GL)
class GLCamera(screenSize: Vec2?, sceneScale: Float) : Camera(screenSize, sceneScale) {

    // Matrix Fields
    private val projectionMatrix: Matrix4f = Matrix4f() // coordinates from camera's perspective
    private val inverseProjection: Matrix4f = Matrix4f()
    private val viewMatrix: Matrix4f = Matrix4f() // normalized device screen coordinates
    private val inverseView: Matrix4f = Matrix4f()

    // Plane Fields
    private val nearPlane = -100f // closest object visible
    private val farPlane = 100f // farthest object visible
    private val zPosition = 0f // how far forward/back the camera is

    // Camera Methods

    override fun toWorld(screen: Vec2): Vec2 {
        // Divide the raw screen coordinates by the window scaling
        return getViewPos(getClipPos(screen / Mayonez.windowScale)) + position
    }

    /** Normalize screen position into clip space. */
    private fun getClipPos(screen: Vec2): Vec2 {
        val flipped = Vec2(screen.x, screenSize.y - screen.y) // Mirror y
        return ((flipped / screenSize * 2f) - Vec2(1f)) / sceneScale
    }

    /** Transform clip position into camera view space. */
    private fun getViewPos(clip: Vec2): Vec2 {
        val view = Vector4f(clip.x, clip.y, 0f, 0f)
            .mul(inverseProjection).mul(inverseView)
        return Vec2(view.x, view.y)
    }

    // Matrix Properties

    /**
     * The projection matrix of the camera, which transforms objects from view
     * space into normalized screen space.
     */
    fun getProjectionMatrix(): Matrix4f {
        val projSize = screenSize.div(zoom)
        projectionMatrix.setOrtho(0f, projSize.x, 0f, projSize.y, nearPlane, farPlane)
        projectionMatrix.invert(inverseProjection)
        return projectionMatrix
    }

    /**
     * The view matrix of the camera, which transforms objects from world space
     * into camera view space.
     */
    fun getViewMatrix(): Matrix4f {
        translateViewMatrix()
        rotateViewMatrix()
        viewMatrix.invert(inverseView)
        return viewMatrix
    }

    private fun rotateViewMatrix() {
        val cameraPos = position * SceneManager.currentScene.scale
        val rotation = Quaternionf()
        rotation.rotationAxis(FloatMath.toRadians(-getRotation()), 0f, 0f, 1f)
        viewMatrix.rotateAround(rotation, cameraPos.x, cameraPos.y, 0f)
    }

    private fun translateViewMatrix() {
        val cameraFront = Vector3f(0f, 0f, -1f)
        val cameraUp = Vector3f(0f, 1f, 0f)
        val offset = screenOffset
        viewMatrix.setLookAt(
            Vector3f(offset.x, offset.y, zPosition),
            cameraFront.add(offset.x, offset.y, 0f), cameraUp
        )
    }

}