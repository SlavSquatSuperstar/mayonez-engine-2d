package mayonez.graphics.sprites

import mayonez.graphics.*
import mayonez.graphics.renderer.*
import mayonez.graphics.textures.*
import mayonez.math.shapes.Rectangle
import java.awt.*
import java.awt.image.*

/**
 * Draws a [JTexture] using the AWT engine. This class should not be directly
 * instantiated. Instead, call [Sprites.createSprite]. See [Sprite] for more information.
 *
 * @author SlavSquatSuperstar
 */
@UsesEngine(EngineType.AWT)
internal class JSprite private constructor(
    private var texture: JTexture?, private var color: MColor
) : Sprite(), JRenderable {

    /**
     * Creates a new JSprite that renders an entire texture.
     *
     * @param texture a texture
     */
    constructor(texture: JTexture?) : this(texture, DEFAULT_COLOR)

    /**
     * Creates a new JSprite that only renders a color.
     *
     * @param color a color
     */
    constructor(color: MColor?) : this(null, color ?: DEFAULT_COLOR)

    // Sprite Properties

    private val image: BufferedImage?
        get() = texture?.image

    override val imageWidth: Int = image?.width ?: 0

    override val imageHeight: Int = image?.height ?: 0

    // Sprite Methods

    override fun render(g2: Graphics2D) {
        if (texture != null) {
            texture!!.draw(g2, transform, getSpriteTransform(), scene.scale)
        } else {
            scene.debugDraw.fillShape(
                Rectangle(transform.position, transform.scale), color
            )
        }
    }

    // Sprite Color Methods

    override fun getColor(): MColor = color

    /**
     * Set the color of this sprite, or recolors the current texture. Caution: Due to the way
     * AWT stores images, this permanently alters the texture until the program is closed!
     *
     * @param color the color
     */
    override fun setColor(color: MColor?) {
        this.color = color ?: DEFAULT_COLOR
        recolorImage(this.color)
    }

    private fun recolorImage(color: MColor) {
        for (y in 0..<imageWidth) {
            for (x in 0..<imageHeight) {
                val pixelColor = getPixelColor(x, y)
                val combinedColor = pixelColor.combine(color)
                setPixelColor(x, y, combinedColor)
            }
        }
    }

    /**
     * Get the pixel's RBGA color on this sprite's stored texture at the
     * specific coordinates.
     */
    private fun getPixelColor(x: Int, y: Int): MColor {
        return if (image == null) Colors.WHITE else MColor(image!!.getRGB(x, y))
    }

    /**
     * Set the pixel's RBGA color on this sprite's stored texture at the
     * specific coordinates.
     */
    private fun setPixelColor(x: Int, y: Int, color: MColor) {
        image?.setRGB(x, y, color.getRGBAValue())
    }

    // Sprite Methods

    override fun getTexture(): JTexture? {
        return texture
    }

    override fun setTexture(texture: Texture?) {
        if (texture is JTexture) {
            this.texture = texture
        }
    }

    override fun copy(): JSprite = JSprite(texture, color)

}
