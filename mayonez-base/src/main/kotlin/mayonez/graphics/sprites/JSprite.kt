package mayonez.graphics.sprites

import mayonez.graphics.*
import mayonez.graphics.renderer.awt.*
import mayonez.graphics.textures.*
import mayonez.math.shapes.Rectangle
import java.awt.*

/**
 * Draws a [JTexture] using the AWT engine. This class should not be
 * directly instantiated. Instead, call [Sprites.createSprite]. See
 * [Sprite] for more information.
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

    override val imageWidth: Int = texture?.imageData?.width ?: 0

    override val imageHeight: Int = texture?.imageData?.height ?: 0

    // Sprite Methods

    override fun render(g2: Graphics2D) {
        if (texture != null) {
            texture!!.draw(g2, transform, getSpriteTransform(), color, scene.scale)
        } else {
            scene.debugDraw.fillShape(
                Rectangle(transform.position, transform.scale), color
            )
        }
    }

    // Sprite Color Methods

    override fun getColor(): MColor = color

    /**
     * Set the color of this sprite, or recolors the current texture. This
     * method should be used sparingly, since it creates a new image in memory
     * per draw call.
     *
     * @param color the color
     */
    override fun setColor(color: MColor?) {
        this.color = color ?: DEFAULT_COLOR
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
