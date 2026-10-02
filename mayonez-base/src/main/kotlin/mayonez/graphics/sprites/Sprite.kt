package mayonez.graphics.sprites

import mayonez.*
import mayonez.graphics.*
import mayonez.graphics.textures.*
import mayonez.renderer.*

/**
 * Draws a [Texture] at a [Node]'s position. To instantiate a sprite,
 * use [Sprites.createSprite]. See [Texture] for more information.
 *
 * @author SlavSquatSuperstar
 */
sealed class Sprite : Node(), Renderable {

    companion object {
        @JvmStatic
        protected val DEFAULT_COLOR: MColor = Colors.WHITE
    }

    // Sprite Properties

    /**
     * Get the width of this sprite's stored texture in pixels.
     *
     * @return the image width, or 0 if drawing a color
     */
    protected abstract val imageWidth: Int

    /**
     * Get the height of this sprite's stored texture in pixels.
     *
     * @return the image height, or 0 if drawing a color
     */
    protected abstract val imageHeight: Int

    // Getters and Setters

    /**
     * Get the color of this sprite, white by default if drawing a texture.
     *
     * @return the color
     */
    abstract fun getColor(): MColor

    /**
     * Set the color of this sprite, or recolor the current texture.
     *
     * @param color the color
     */
    abstract fun setColor(color: MColor?)

    /**
     * Get the texture this sprite draws.
     *
     * @return the texture, or null if drawing a color
     */
    abstract fun getTexture(): Texture?

    /**
     * Set the texture this sprite draws.
     *
     * @param texture the new texture
     */
    abstract fun setTexture(texture: Texture?)

    // Renderable Methods

    final override fun getZIndex(): Int = super.zIndex

    final override fun isInUI(): Boolean = false

}