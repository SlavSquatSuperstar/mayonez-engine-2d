package mayonez.graphics.sprites

import mayonez.*
import mayonez.graphics.renderer.*
import mayonez.graphics.textures.*
import mayonez.util.*

/**
 * A visual representation of a [mayonez.GameObject]. To instantiate a
 * sprite, use [Sprites.createSprite] or [Sprites.createSprite].
 *
 * @author SlavSquatSuperstar
 */
sealed class Sprite : Component(), Renderable {

    companion object {
        @JvmStatic
        protected val DEFAULT_COLOR: MColor = Colors.WHITE
    }

    // Sprite Properties

    private var spriteXf: Transform? = null

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
     * Set the color of this sprite, or recolors the current texture.
     *
     * @param color the color
     */
    abstract fun setColor(color: MColor?)

    /**
     * Get the sprite's transform in the parent object's local space.
     *
     * @return the sprite transform
     */
    fun getSpriteTransform(): Transform? {
        return spriteXf
    }

    /**
     * Set additional position, rotation, and size modifiers for the sprite.
     *
     * @param spriteXf the transform
     */
    fun setSpriteTransform(spriteXf: Transform?): Sprite {
        this.spriteXf = spriteXf
        return this
    }

    /**
     * Returns the texture this sprite draws.
     *
     * @return the texture, or null if drawing a color
     */
    abstract fun getTexture(): Texture?

    /**
     * Sets the texture this sprite draws.
     *
     * @param texture the new texture
     */
    abstract fun setTexture(texture: Texture?)

    // Renderable Methods

    final override fun getZIndex(): Int {
        return gameObject.zIndex
    }

    // Copy Methods

    /**
     * Returns a new sprite with the same image but not attached to any
     * [mayonez.GameObject].
     *
     * @return a copy of this image
     */
    abstract fun copy(): Sprite?

}