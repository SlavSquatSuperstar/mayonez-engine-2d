package mayonez.renderer.shader;

import mayonez.assets.*;
import mayonez.graphics.*;

import java.util.*;

/**
 * The default OpenGL .glsl shaders that come with the program.
 *
 * @author SlavSquatSuperstar
 */
@UsesEngine(EngineType.GL)
public final class Shaders {

    // TODO read shader names from file
    public static final Shader DEFAULT_SHADER =
            getShaderFromFile("assets/shaders/default.glsl");
    public static final Shader DEBUG_SHADER =
            getShaderFromFile("assets/shaders/debug.glsl");
    public static final Shader CIRCLE_SHADER =
            getShaderFromFile("assets/shaders/circle.glsl");
    public static final Shader UI_SHADER =
            getShaderFromFile("assets/shaders/ui.glsl");

    private Shaders() {
    }

    private static Shader getShaderFromFile(String filename) {
        return Objects.requireNonNull(Assets.getAsset(filename, Shader.class));
    }

}
