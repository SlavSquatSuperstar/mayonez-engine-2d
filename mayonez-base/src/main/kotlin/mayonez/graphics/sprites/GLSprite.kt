package mayonez.graphics.sprites

import mayonez.graphics.*
import mayonez.graphics.batch.*
import mayonez.graphics.renderer.*
import mayonez.graphics.textures.*
import mayonez.math.*
import mayonez.math.shapes.*


/**
 * Draws a [GLTexture] using the GL engine. This class should not be
 * directly instantiated. Instead, call [Sprites.createSprite]. See
 * [Sprite] for more information.
 *
 * @author SlavSquatSuperstar
 */
@UsesEngine(EngineType.GL)
internal class GLSprite private constructor(
    private var texture: GLTexture?, private var color: Color
) : Sprite(), GLRenderable {

    /**
     * Create a new GLSprite that renders an entire texture.
     *
     * @param texture a texture
     */
    internal constructor(texture: GLTexture?) : this(texture, DEFAULT_COLOR)

    /**
     * Create a new GLSprite that only renders a color.
     *
     * @param color a color
     */
    internal constructor(color: Color?) : this(null, color ?: DEFAULT_COLOR)

    // Sprite Properties

    override val imageWidth: Int = texture?.width ?: 0

    override val imageHeight: Int = texture?.height ?: 0

    // Sprite Methods

    override fun getColor(): Color = color

    override fun setColor(color: Color?) {
        this.color = color ?: DEFAULT_COLOR
    }

    override fun getTexture(): GLTexture? = texture

    override fun setTexture(texture: Texture?) {
        this.texture = texture as? GLTexture
    }

    override fun copy(): GLSprite = GLSprite(texture, color)

    // Renderer Methods

    /**
     * Pushes a sprite's vertices and texture to a render batch.
     *
     * @param batch the batch
     */
    override fun pushToBatch(batch: RenderBatch) {
        // TODO common push sprite function
        // Add sprite vertex data
        val objXf = transform.combine(getSpriteTransform())
        val color = this.color.toGL()
        val texCoords = getTexCoords()
        val texID = batch.addTextureAndGetID(texture)

        // Background sprite will not have scale but will use spriteXf instead
        val sceneScale = gameObject?.scene?.scale ?: 1f

        // Render sprite at object center and rotate according to object
        val sprRect = Rectangle(
            objXf.position * sceneScale, objXf.scale * sceneScale, objXf.rotation
        )
        val sprVertices = sprRect.vertices
        for (i in sprVertices.indices) {
            batch.pushVertex(sprVertices[i], color, texCoords[i], texID)
        }
    }

    private fun RenderBatch.pushVertex(
        vertex: Vec2, color: GLColor, texPos: Vec2, texID: Int
    ) {
        pushVec2(vertex)
        pushVec4(color)
        pushVec2(texPos)
        pushInt(texID)
    }

    // Renderable Methods

    override fun getBatchSize(): Int = RenderBatch.MAX_SPRITES

    override fun getPrimitive(): DrawPrimitive = DrawPrimitive.SPRITE

    fun getTexCoords(): Array<Vec2> {
        return texture?.texCoords ?: GLTexture.DEFAULT_TEX_COORDS
    }

}