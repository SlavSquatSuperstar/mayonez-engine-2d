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

    @Test
    void readWholeShader() {
        try (var input = FilePath.fromFilename(shaderPath).openInputStream()) {
            var shaderSource = TextIOUtils.readText(input);
            var stageSources = ShaderParser.splitShaderSource(shaderSource);
            var stages = Arrays.stream(stageSources)
                    .map(ShaderParser::parseShaderStage)
                    .filter(Objects::nonNull)
                    .toList();

            assertEquals(2, stages.size());
            assertEquals(ShaderType.VERTEX, stages.get(0).getType());
            assertEquals(ShaderType.FRAGMENT, stages.get(1).getType());
        } catch (IOException e) {
            fail();
        }
    }

}
