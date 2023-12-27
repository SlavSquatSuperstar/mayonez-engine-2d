package mayonez.graphics.renderer.gl

import mayonez.graphics.*
import mayonez.graphics.debug.*
import mayonez.graphics.renderer.*
import org.lwjgl.opengl.GL11.*

/**
 * Draws all user interface elements onto the screen using LWJGL's OpenGL
 * library.
 *
 * @author SlavSquatSuperstar
 */
@UsesEngine(EngineType.GL)
internal class GLUIRenderer : GLRenderer("assets/shaders/ui.glsl"),
    UIRenderer {

    // Renderer Parameters
    private val lineStyle: LineStyle = LineStyle.QUADS

    // Renderer Objects
    private val objects: MutableList<GLRenderable> = ArrayList() // Drawable objects

    // Scene Renderer Methods

    override fun addUIElement(r: Renderable?) {
        if (r is GLRenderable) objects.add(r)
    }

    override fun removeUIElement(r: Renderable?) {
        if (r is GLRenderable) objects.remove(r)
    }

    // Renderer Methods

    override fun clear() {
        super.clear()
        objects.clear()
    }

    override fun preRender() {
        shader.bind()
        shader.uploadMat4("uProjection", viewport.projectionMatrix)
        shader.uploadIntArray("uTextures", textureSlots)
        setGLProperties()
    }

    private fun setGLProperties() {
        // Note: complex transparent shapes don't work very well
        glEnable(GL_BLEND)
        glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA)
        glEnable(GL_LINE_SMOOTH)
        when (lineStyle) {
            LineStyle.SINGLE -> glLineWidth(DebugDraw.DEFAULT_STROKE_SIZE)
            LineStyle.MULTIPLE -> glLineWidth(1f)
            else -> return
        }
    }

    override fun createBatches() {
        objects.sortBy { it.zIndex }
        objects.filter { it.isEnabled }
            .forEach { it.pushToBatch(it.getAvailableBatch()) }
    }

    override fun postRender() {
        super.postRender()
        glDisable(GL_LINE_SMOOTH)
        glDisable(GL_BLEND)
    }

}
