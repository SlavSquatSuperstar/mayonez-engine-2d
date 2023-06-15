package mayonez.graphics.renderer;

import org.junit.jupiter.api.*;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for the {@link mayonez.graphics.renderer.ShaderType} class.
 *
 * @author SlavSquatSuperstar
 */
class ShaderTypeTest {

    @Test
    void getVertexTypeSuccess() {
        assertEquals(ShaderType.VERTEX, ShaderType.getType("vErTeX"));
    }

    @Test
    void getFragmentTypeSuccess() {
        assertEquals(ShaderType.FRAGMENT, ShaderType.getType("FRagMEnt"));
    }

    @Test
    void getInvalidTypeFails() {
        assertThrows(IllegalArgumentException.class, () -> ShaderType.getType("Geometry"));
    }

}