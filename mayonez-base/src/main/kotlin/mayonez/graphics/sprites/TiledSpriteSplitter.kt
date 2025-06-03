package mayonez.graphics.sprites

import mayonez.assets.image.*
import mayonez.math.*
import kotlin.math.*

/**
 * Reads equally-sized sub-sprites from a sprite sheet, with optional spacing
 * between sprites, starting from the top left and reading row by row.
 *
 * @return the sprite regions
 */
class TiledSpriteSplitter(
    /**
     * The dimensions of the sprite sheet texture, in pixels.
     */
    private val sheetSize: Vec2,

    /**
     * The dimensions of each sprite, in pixels.
     */
    private val spriteSize: Vec2,

    /**
     * The padding between sprites along each dimension, in pixels.
     */
    private val spacing: Vec2,

    /**
     * The maximum number of sprites to split from the sprite sheet.
     */
    private val maxSprites: Int,

    /**
     * The pixel origin of the first sprite.
     */
    private val spriteStart: Vec2,

    /**
     * The direction to read the next sprite.
     */
    private val readDirection: Vec2
) : SpriteSplitter {

    /**
     * The distance to the next sprite origin, in pixels.
     */
    private val spriteMove: Vec2 = spriteSize + spacing

    override fun getSpriteRegions(): Array<ImageRegion?> {
        var shape = getSheetShape()
        val cols = shape.x.toInt()
        val rows = shape.y.toInt()
        val spriteMove = spriteMove * readDirection

        val regions = arrayOfNulls<ImageRegion>(maxSprites)
        for (y in 0..<rows) {
            for (x in 0..<cols) {
                val spriteIdx = y * cols + x
                // Check index doesn't go out of bounds
                if (spriteIdx >= maxSprites) return regions

                val spriteOrigin = spriteStart +
                        (Vec2(x.toFloat(), y.toFloat()) * spriteMove)
                regions[spriteIdx] = ImageRegion(spriteOrigin, spriteSize)
            }
        }
        return regions
    }

    /**
     * Get the number of sprites on this sprite sheet in each dimension.
     *
     * @return the sheet shape
     */
    private fun getSheetShape(): Vec2 {
        val shape1 = sheetSize / spriteMove
        val shape2 = (sheetSize + spacing) / spriteMove

        // Check if extra spacing on right/bottom
        val cols = max(shape1.x, shape2.x)
        val rows = max(shape1.y, shape2.y)
        return Vec2(cols, rows)
    }

}
