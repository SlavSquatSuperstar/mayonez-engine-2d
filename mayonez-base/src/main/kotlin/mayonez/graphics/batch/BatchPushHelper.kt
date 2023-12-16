package mayonez.graphics.batch

import mayonez.*
import mayonez.graphics.*
import mayonez.math.*
import mayonez.math.shapes.*

/**
 * Helps push textures to a render batch.
 *
 * @author SlavSquatSuperstar
 */
object BatchPushHelper {

    /** Adds a sprite's vertex data to a render batch. */
    @JvmStatic
    fun RenderBatch.pushTexture(
        transform: Transform, color: MColor, texCoords: Array<Vec2>, texID: Int,
        sceneScale: Float
    ) {
        val glColor = color.toGL()

        // Render sprite at object center and rotate according to object
        val sprRect = Rectangle(
            transform.position * sceneScale, transform.scale * sceneScale, transform.rotation
        )
        val sprVertices = sprRect.vertices
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