package mayonez.graphics.renderer

import org.junit.jupiter.api.*
import org.junit.jupiter.api.Assertions.*

/**
 * Unit tests for the [mayonez.graphics.renderer.ShaderType] class.
 *
 * @author SlavSquatSuperstar
 */
internal class ShaderTypeTest {

    @Test
    fun getVertexTypeSuccess() {
        assertEquals(ShaderType.VERTEX, ShaderType.findWithName("vErTeX"))
    }

    @Test
    fun getFragmentTypeSuccess() {
        assertEquals(ShaderType.FRAGMENT, ShaderType.findWithName("FRagMEnt"))
    }

    @Test
    fun getInvalidTypeFails() {
        assertThrows(IllegalArgumentException::class.java) { ShaderType.findWithName("Geometry") }
    }

}