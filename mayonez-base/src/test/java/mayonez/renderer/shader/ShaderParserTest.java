package mayonez.renderer.shader;

import org.junit.jupiter.api.*;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for the {@link mayonez.renderer.shader.ShaderParser} class.
 *
 * @author SlavSquatSuperstar
 */
class ShaderParserTest {

    private static final String fragmentPath = "testassets/shaders/test1.frag";
    private static final String vertexPath = "testassets/shaders/test1.vert";
    private static final String messyFragmentPath = "testassets/shaders/test2.frag";
    private static final String messyVertexPath = "testassets/shaders/test2.vert";

    @Test
    void readVertexShader() {
        try {
            var stage = ShaderParser.parseShaderStage(vertexPath);

            assertNotNull(stage);
            assertEquals(ShaderType.VERTEX, stage.getType());
        } catch (Exception e) {
            fail();
        }
    }

    @Test
    void readFragmentShader() {
        try {
            var stage = ShaderParser.parseShaderStage(fragmentPath);

            assertNotNull(stage);
            assertEquals(ShaderType.FRAGMENT, stage.getType());
        } catch (Exception e) {
            fail();
        }
    }

    @Test
    void readMessyVertexShader() {
        try {
            var stage = ShaderParser.parseShaderStage(messyVertexPath);

            assertNotNull(stage);
            assertEquals(ShaderType.VERTEX, stage.getType());
        } catch (Exception e) {
            fail();
        }
    }

    @Test
    void readMessyFragmentShader() {
        try {
            var stage = ShaderParser.parseShaderStage(messyFragmentPath);

            assertNotNull(stage);
            assertEquals(ShaderType.FRAGMENT, stage.getType());
        } catch (Exception e) {
            fail();
        }
    }

}
