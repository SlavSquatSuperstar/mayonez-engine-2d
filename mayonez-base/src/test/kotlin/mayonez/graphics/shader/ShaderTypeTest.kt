package mayonez.graphics.shader

import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test

/**
 * Unit tests for the [mayonez.graphics.renderer.ShaderType] class.
 *
 * @author SlavSquatSuperstar
 */
internal class ShaderTypeTest {

    @Test
    fun getVertexTypeSuccess() {
        Assertions.assertEquals(ShaderType.VERTEX, ShaderType.findWithName("vErTeX"))
    }

    @Test
    fun getFragmentTypeSuccess() {
        Assertions.assertEquals(ShaderType.FRAGMENT, ShaderType.findWithName("FRagMEnt"))
    }

    @Test
    fun getInvalidTypeFails() {
        Assertions.assertThrows(IllegalArgumentException::class.java) { ShaderType.findWithName("Geometry") }
    }

}