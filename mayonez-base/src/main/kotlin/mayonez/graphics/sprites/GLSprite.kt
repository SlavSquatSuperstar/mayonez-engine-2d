package mayonez.graphics.sprites

import mayonez.graphics.*
import mayonez.graphics.textures.*
import mayonez.math.*
import mayonez.math.shapes.*
import mayonez.renderer.gl.*


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
) : Sprite(), GLQuad {

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

    // Renderable Methods

    override fun getVertexPositions(): Array<out Vec2?>? {
        // Render sprite at object center and rotate according to object
        // Background sprite will not have scale but will use spriteXf instead
        val objXf = transform.combine(getSpriteTransform())
        return Rectangle(objXf.position, objXf.scale, objXf.rotation).vertices
    }

}