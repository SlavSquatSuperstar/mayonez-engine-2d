package mayonez.renderer.gl

import mayonez.graphics.*
import mayonez.graphics.debug.*
import mayonez.graphics.font.*
import mayonez.renderer.*
import org.lwjgl.opengl.GL11.glLineWidth

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
    private val objects: MutableList<Renderable> = ArrayList()

    // Scene Renderer Methods

    override fun addUIElement(r: Renderable?) {
        if (r.isAccepted()) objects.add(r!!)
    }

    override fun removeUIElement(r: Renderable?) {
        if (r.isAccepted()) objects.remove(r!!)
    }

    // Renderer Methods

    override fun clear() {
        super.clear()
        objects.clear()
    }

    override fun preRender() {
        super.preRender()

        // Upload uniforms
        shader.uploadMat4("uProjection", viewport.projectionMatrix)
        shader.uploadIntArray("uTextures", textureSlots)

        // Set GL Properties
        when (lineStyle) {
            LineStyle.SINGLE -> glLineWidth(DebugDraw.DEFAULT_STROKE_SIZE)
            LineStyle.MULTIPLE -> glLineWidth(1f)
            else -> return
        }
    }

    override fun createBatches() {
        // Sort objects
        objects.sortBy { it.zIndex }

        // Process and push objects
        objects.filter { it.isEnabled }
            .forEach { it.pushToBatch() }
    }

    // Helper Functions

    private fun Renderable?.isAccepted(): Boolean {
        return (this is GLRenderable) || (this is TextLabel)
    }

    private fun Renderable.pushToBatch() {
        when (this) {
            is GLRenderable -> this.pushToBatch(this.getAvailableBatch())
            is TextLabel -> this.glyphSprites.forEach { glyph ->
                glyph.pushToBatch(glyph.getAvailableBatch())
            }
        }
    }

}
