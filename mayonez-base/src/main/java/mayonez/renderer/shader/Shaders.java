package mayonez.renderer.shader;

import mayonez.application.*;
import mayonez.assets.*;

import java.util.*;

/**
 * The default OpenGL .glsl shaders that come with the program.
 *
 * @author SlavSquatSuperstar
 */
@UsesBackend(Backend.GL)
public final class Shaders {

    public static final Shader DEFAULT_SHADER =
            getShaderFromFile("assets/shaders/default.json");
    public static final Shader UI_SHADER = DEFAULT_SHADER;
    public static final Shader DEBUG_SHADER =
            getShaderFromFile("assets/shaders/debug.json");
    public static final Shader CIRCLE_SHADER =
            getShaderFromFile("assets/shaders/circle.json");
    public static final Shader ELLIPSE_SHADER =
            getShaderFromFile("assets/shaders/ellipse.json");

    private Shaders() {
    }

    private static Shader getShaderFromFile(String filename) {
        var shader = Objects.requireNonNull(Assets.getAsset(filename, Shader.class));
        shader.createShader();
        return shader;
    }

}
