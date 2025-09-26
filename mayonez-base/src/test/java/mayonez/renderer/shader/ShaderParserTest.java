package mayonez.renderer.shader;

import mayonez.assets.*;
import mayonez.assets.text.*;
import org.junit.jupiter.api.*;

import java.io.IOException;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for the {@link mayonez.renderer.shader.ShaderParser} class.
 *
 * @author SlavSquatSuperstar
 */
class ShaderParserTest {

    private static final String shaderPath = "testassets/shaders/test.glsl";
    private static final String fragmentPath = "testassets/shaders/test.frag";
    private static final String vertexPath = "testassets/shaders/test.vert";

    @Test
    void readWholeShader() {
        try (var input = FilePath.fromFilename(shaderPath).openInputStream()) {
            var shaderSource = TextIOUtils.readText(input);
            var stageSources = ShaderParser.splitShaderSource2(shaderSource);
            var stages = Arrays.stream(stageSources)
                    .map(ShaderParser::parseShaderStage2)
                    .filter(Objects::nonNull)
                    .toList();

            assertEquals(2, stages.size());
            assertEquals(ShaderType.VERTEX, stages.get(0).getType());
            assertEquals(ShaderType.FRAGMENT, stages.get(1).getType());
        } catch (IOException e) {
            fail();
        }
    }

    @Test
    void readVertexShader() {
        try (var input = FilePath.fromFilename(vertexPath).openInputStream()) {
            var shaderSource = TextIOUtils.readText(input);
            var stage = ShaderParser.parseShaderStage2(shaderSource);

            assertNotNull(stage);
            assertEquals(ShaderType.VERTEX, stage.getType());
        } catch (IOException e) {
            fail();
        }
    }

    @Test
    void readFragmentShader() {
        try (var input = FilePath.fromFilename(fragmentPath).openInputStream()) {
            var shaderSource = TextIOUtils.readText(input);
            var stage = ShaderParser.parseShaderStage2(shaderSource);

            assertNotNull(stage);
            assertEquals(ShaderType.FRAGMENT, stage.getType());
        } catch (IOException e) {
            fail();
        }
    }

}
