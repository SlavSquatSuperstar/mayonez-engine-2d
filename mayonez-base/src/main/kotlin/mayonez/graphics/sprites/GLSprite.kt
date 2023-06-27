package mayonez.graphics.sprites

import mayonez.*
import mayonez.annotations.*
import mayonez.graphics.*
import mayonez.graphics.textures.*
import mayonez.math.*
import mayonez.math.shapes.*
import mayonez.util.*

/**
 * A component that draws an image at a [GameObject]'s position using the
 * GL engine.
 *
 * @author SlavSquatSuperstar
 */
@UsesEngine(EngineType.GL)
class GLSprite private constructor(private val texture: GLTexture?, color: Color?) : Sprite(),
    GLRenderable {

    private var color: Color = color ?: Colors.WHITE

    /**
     * Create a new GLSprite that renders an entire texture.
     *
     * @param texture a texture
     */
    internal constructor(texture: GLTexture?) : this(texture, Colors.WHITE)

    /**
     * Create a new GLSprite that only renders a color.
     *
     * @param color a color
     */
    internal constructor(color: Color?) : this(null, color)

    // Renderer Methods

    /**
     * Pushes a sprite's vertices and texture to a render batch.
     *
     * @param batch the batch
     */
    override fun pushToBatch(batch: RenderBatch) {
        // Add sprite vertex data
        val objXf = transform.combine(spriteTransform)
        val color = this.color.toGL()
        val texCoords = getTexCoords()
        val texID = batch.addTexture(texture)

        // Render sprite at object center and rotate according to object
        val sprVertices = Rectangle.rectangleVertices(Vec2(0f), Vec2(1f), objXf.rotation)
        for (i in sprVertices.indices) {
            // spr_pos = (obj_pos + vert_pos * obj_scale) * world_scale
            // can't use this.scene.scale as it will return null
            val vertex = (objXf.position + (sprVertices[i] * objXf.scale)) * SceneManager.currentScene.scale
            batch.pushVertex(vertex, color, texCoords[i], texID)
        }
    }

    private fun RenderBatch.pushVertex(vertex: Vec2, color: GLColor, texPos: Vec2, texID: Int) {
        pushVec2(vertex)
        pushVec4(color)
        pushVec2(texPos)
        pushInt(texID)
    }

    // Sprite/Renderable Methods

    override fun getBatchSize(): Int = RenderBatch.MAX_SPRITES

    override fun getColor(): Color = color

    override fun setColor(color: Color?) {
        TODO("Not yet implemented")
    }

    override fun getImageWidth(): Int = texture?.width ?: 0

    override fun getImageHeight(): Int = texture?.height ?: 0

    override fun getPrimitive(): DrawPrimitive = DrawPrimitive.SPRITE

    fun getTexCoords(): Array<Vec2> {
        return texture?.texCoords ?: GLTexture.DEFAULT_TEX_COORDS
    }

    override fun getTexture(): GLTexture? = texture

    override fun getZIndex(): Int = gameObject.zIndex

    override fun copy(): GLSprite {
        return if (texture == null) GLSprite(color) else GLSprite(texture)
    }

}