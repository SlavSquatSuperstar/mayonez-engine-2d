package mayonez.graphics.renderer

import mayonez.graphics.renderer.ShaderType.Companion.getType
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test

/**
 * Unit tests for the [mayonez.graphics.renderer.ShaderType] class.
 *
 * @author SlavSquatSuperstar
 */
internal class ShaderTypeTest {

    @Test
    fun getVertexTypeSuccess() {
        assertEquals(ShaderType.VERTEX, getType("vErTeX"))
    }

    @Test
    fun getFragmentTypeSuccess() {
        assertEquals(ShaderType.FRAGMENT, getType("FRagMEnt"))
    }

    @Test
    fun getInvalidTypeFails() {
        assertThrows(IllegalArgumentException::class.java) { getType("Geometry") }
    }

}