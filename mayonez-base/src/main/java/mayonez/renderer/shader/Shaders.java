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
            getShaderFromFiles(new String[] {
                    "assets/shaders/default.vert",
                    "assets/shaders/default_330.frag"
            });
    public static final Shader DEBUG_SHADER =
            getShaderFromFile("assets/shaders/debug.glsl");
    // TODO draw circles with ellipses if not too many
    public static final Shader CIRCLE_SHADER =
            getShaderFromFile("assets/shaders/circle.glsl");
    public static final Shader ELLIPSE_SHADER =
            getShaderFromFile("assets/shaders/ellipse.glsl");
    public static final Shader UI_SHADER =
            getShaderFromFile("assets/shaders/ui.glsl");

    private Shaders() {
    }

    private static Shader getShaderFromFile(String filename) {
        return Objects.requireNonNull(Assets.getAsset(filename, Shader.class));
    }

    private static Shader getShaderFromFiles(String[] filenames) {
        return new Shader(filenames);
    }

}
