package mayonez.renderer.batch

import mayonez.graphics.*
import mayonez.math.*

/**
 * Helps push objects to a render batch.
 *
 * @author SlavSquatSuperstar
 */
object BatchPushHelper {

    /** Adds a sprite's vertex data to a render batch. */
    @JvmStatic
    fun RenderBatch.pushSprite(
        sprVertices: Array<Vec2>, color: MColor, texCoords: Array<Vec2>, texID: Int
    ) {
        for (i in sprVertices.indices) {
            this.pushVertex(sprVertices[i], color.toGL(), texCoords[i], texID)
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

}