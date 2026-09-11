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

    private static final Set<String> UNIFORMS = Set.of(
            "uTransform", "uTransformMultiplier",
            "uTexture", "uUseTexture"
    );

    // TODO read bad JSON missing stage
    // TODO missing files

    @Test
    void readShaderWithCleanWhiteSpace() {
        var shader = new Shader("testassets/shaders/test1.json");
        shader.readShader();
        var stages = shader.getStages();

        // Check stages
        assertNotNull(stages);
        assertEquals(
                List.of(ShaderType.VERTEX, ShaderType.FRAGMENT),
                stages.stream().map(ShaderStage::getType).toList()
        );

        // Check uniforms
        assertEquals(UNIFORMS, shader.getUniformLocations().keySet());
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
