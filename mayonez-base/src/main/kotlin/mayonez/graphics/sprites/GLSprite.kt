package mayonez.graphics.sprites

import mayonez.GameObject
import mayonez.SceneManager
import mayonez.annotations.EngineType
import mayonez.annotations.UsesEngine
import mayonez.graphics.Color
import mayonez.graphics.Colors
import mayonez.graphics.DrawPrimitive
import mayonez.graphics.GLRenderable
import mayonez.graphics.RenderBatch
import mayonez.io.image.GLTexture
import mayonez.math.Vec2
import mayonez.physics.shapes.Rectangle

/**
 * A component that draws an image at a [GameObject]'s position using the GL engine.
 *
 * @author SlavSquatSuperstar
 */
@UsesEngine(EngineType.GL)
class GLSprite private constructor(private val texture: GLTexture?, color: Color?) : Sprite(color),
    GLRenderable {

    /**
     * Create a new GLSprite that renders an entire texture.
     *
     * @param texture a texture
     */
    internal constructor(texture: GLTexture) : this(texture, Colors.WHITE)

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
        val objXf = this.transform.combine(this.spriteTransform)
        val color = this.color.toGL()
        val texCoords = this.getTexCoords()
        val texID = batch.addTexture(this.texture)

        // Render sprite at object center and rotate according to object
        val sprVertices = Rectangle.rectangleVertices(Vec2(0f), Vec2(1f), objXf.rotation)
        for (i in sprVertices.indices) {
            // sprite_pos = (obj_pos + vert_pos * obj_scale) * world_scale
            val sprPos = (objXf.position + (sprVertices[i] * objXf.scale)) * SceneManager.currentScene.scale
            batch.pushVec2(sprPos)
            batch.pushVec4(color)
            batch.pushVec2(texCoords[i])
            batch.pushInt(texID)
        }
    }

    // Sprite Methods

    override fun getBatchSize(): Int = RenderBatch.MAX_SPRITES

    override fun getPrimitive(): DrawPrimitive = DrawPrimitive.SPRITE

    fun getTexCoords(): Array<Vec2> = texture?.texCoords ?: GLTexture.DEFAULT_TEX_COORDS

    override fun getTexture(): GLTexture? = texture

    override fun getZIndex(): Int = gameObject.zIndex

    override fun copy(): GLSprite = if (texture == null) GLSprite(color) else GLSprite(texture)
}