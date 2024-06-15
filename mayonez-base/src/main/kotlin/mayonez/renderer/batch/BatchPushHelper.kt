package mayonez.renderer.batch

import mayonez.graphics.*
import mayonez.math.*

/**
 * Helps push textures to a render batch.
 *
 * @author SlavSquatSuperstar
 */
object BatchPushHelper {

    /** Adds a sprite's vertex data to a render batch. */
    @JvmStatic
    fun RenderBatch.pushTexture(
        sprVertices: Array<Vec2>, color: MColor, texCoords: Array<Vec2>, texID: Int
    ) {
        // Render sprite at object center and rotate according to object
        val glColor = color.toGL()
        for (i in sprVertices.indices) {
            this.pushVertex(sprVertices[i], glColor, texCoords[i], texID)
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