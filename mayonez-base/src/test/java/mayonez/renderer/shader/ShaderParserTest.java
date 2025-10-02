package mayonez.renderer.shader;

import mayonez.assets.*;
import mayonez.assets.text.*;
import org.junit.jupiter.api.*;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for the {@link mayonez.renderer.shader.ShaderParser} class.
 *
 * @author SlavSquatSuperstar
 */
class ShaderParserTest {

    private static final String shaderPath = "testassets/shaders/test1.glsl";
    private static final String fragmentPath = "testassets/shaders/test1.frag";
    private static final String vertexPath = "testassets/shaders/test1.vert";
    private static final String messyShaderPath = "testassets/shaders/test2.glsl";
    private static final String messyFragmentPath = "testassets/shaders/test2.frag";
    private static final String messyVertexPath = "testassets/shaders/test2.vert";

    @Test
    void readWholeShader() {
        try (var input = FilePath.fromFilename(shaderPath).openInputStream()) {
            var shaderSource = TextIOUtils.readText(input);
            var stages = ShaderParser.parseShaderStages(shaderSource);

            assertEquals(2, stages.size());
            assertEquals(ShaderType.VERTEX, stages.get(0).getType());
            assertEquals(ShaderType.FRAGMENT, stages.get(1).getType());
        } catch (Exception e) {
            fail();
        }
    }

    @Test
    void readVertexShader() {
        try (var input = FilePath.fromFilename(vertexPath).openInputStream()) {
            var shaderSource = TextIOUtils.readText(input);
            var stage = ShaderParser.parseShaderStage(shaderSource);

            assertNotNull(stage);
            assertEquals(ShaderType.VERTEX, stage.getType());
        } catch (Exception e) {
            fail();
        }
    }

    @Test
    void readFragmentShader() {
        try (var input = FilePath.fromFilename(fragmentPath).openInputStream()) {
            var shaderSource = TextIOUtils.readText(input);
            var stage = ShaderParser.parseShaderStage(shaderSource);

            assertNotNull(stage);
            assertEquals(ShaderType.FRAGMENT, stage.getType());
        } catch (Exception e) {
            fail();
        }
    }

    @Test
    void readMessyWholeShader() {
        try (var input = FilePath.fromFilename(messyShaderPath).openInputStream()) {
            var shaderSource = TextIOUtils.readText(input);
            var stages = ShaderParser.parseShaderStages(shaderSource);

            assertNotNull(stages);
            assertEquals(2, stages.size());
            assertEquals(ShaderType.VERTEX, stages.get(0).getType());
            assertEquals(ShaderType.FRAGMENT, stages.get(1).getType());
        } catch (Exception e) {
            fail();
        }
    }

    @Test
    void readMessyVertexShader() {
        try (var input = FilePath.fromFilename(messyVertexPath).openInputStream()) {
            var shaderSource = TextIOUtils.readText(input);
            var stage = ShaderParser.parseShaderStage(shaderSource);

            assertNotNull(stage);
            assertEquals(ShaderType.VERTEX, stage.getType());
        } catch (Exception e) {
            fail();
        }
    }

    @Test
    void readMessyFragmentShader() {
        try (var input = FilePath.fromFilename(messyFragmentPath).openInputStream()) {
            var shaderSource = TextIOUtils.readText(input);
            var stage = ShaderParser.parseShaderStage(shaderSource);

            assertNotNull(stage);
            assertEquals(ShaderType.FRAGMENT, stage.getType());
        } catch (Exception e) {
            fail();
        }
    }

}
