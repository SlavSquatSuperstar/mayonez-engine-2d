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
    private val objects: MutableList<GLRenderable> = ArrayList() // Drawable objects
    private val textObjects: MutableList<TextLabel> = ArrayList() // Text objects

    // Scene Renderer Methods

    override fun addUIElement(r: Renderable?) {
        if (r is GLRenderable) objects.add(r)
        else if (r is TextLabel) textObjects.add(r)
    }

    override fun removeUIElement(r: Renderable?) {
        if (r is GLRenderable) objects.remove(r)
        else if (r is TextLabel) textObjects.remove(r)
    }

    // Renderer Methods

    override fun clear() {
        super.clear()
        objects.clear()
        textObjects.clear()
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
        // Push objects
        objects.sortBy { it.zIndex }
        objects.filter { it.isEnabled }
            .forEach { it.pushToBatch(it.getAvailableBatch()) }

        // Push text
        textObjects.sortBy { it.zIndex }
        textObjects.filter { it.isEnabled }
            .forEach {
                it.glyphSprites.forEach { glyph ->
                    glyph.pushToBatch(glyph.getAvailableBatch())
                }
            }
    }

}
