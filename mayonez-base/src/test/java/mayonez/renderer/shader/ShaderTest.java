package mayonez.renderer.shader;

import org.junit.jupiter.api.*;

import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for the {@link mayonez.renderer.shader.Shader} class.
 *
 * @author SlavSquatSuperstar
 */
class ShaderTest {

    // TODO read bad JSON missing stage
    // TODO missing files

    @Test
    void readShaderWithBothStages() {
        var shader = new Shader("testassets/shaders/test_all_stages.json");
        shader.readShader();
        var stages = shader.getStages();

        // Check stages
        assertNotNull(stages);
        assertEquals(
                List.of(ShaderType.VERTEX, ShaderType.FRAGMENT),
                stages.stream().map(ShaderStage::getType).toList()
        );

        // Check uniforms
        assertEquals(Set.of(
                "uTransform", "uTransformMultiplier", "uTextures", "uTexID"
        ), shader.getUniformLocations().keySet());
    }

    @Test
    void readShaderWithNoVertex() {
        var shader = new Shader("testassets/shaders/test_no_frag.json");
        assertThrows(ShaderException.class, shader::readShader);
    }

    @Test
    void readShaderWithNoFragment() {
        var shader = new Shader("testassets/shaders/test_no_vert.json");
        assertThrows(ShaderException.class, shader::readShader);
    }

    @Test
    void readShaderWithNoStages() {
        var shader = new Shader("testassets/shaders/test_no_stages.json");
        assertThrows(ShaderException.class, shader::readShader);
    }

    @Test
    void readShaderWithMissingFiles() {
        var shader = new Shader("testassets/shaders/test_missing_files.json");
        assertThrows(ShaderException.class, shader::readShader);
    }

    @Test
    void parseUniformsOnly() {
        var stage = new ShaderStage(
                "testassets/shaders/parse_uniforms.glsl", ShaderType.FRAGMENT
        );
        stage.readSource();
        assertEquals(Set.of(
                "uTexID", "uTextures1", "uTextures2", "_uTexID", "uTextures3", "uTextures4"
        ), stage.getUniforms());
    }

}
