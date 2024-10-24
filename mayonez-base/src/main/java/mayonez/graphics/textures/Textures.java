package mayonez.graphics.textures;

import mayonez.*;
import mayonez.assets.*;

/**
 * A factory class that constructs {@link mayonez.graphics.textures.Texture} objects
 * depending on the run configuration.
 *
 * @author SlavSquatSuperstar
 */
public final class Textures {

    private Textures() {
    }

    // Texture Methods

    /**
     * Retrieves the asset at the given location as a AWT or GL texture based on the
     * current engine instance.
     *
     * @param filename the texture location
     * @return a texture
     */
    public static Texture getTexture(String filename) {
        return Mayonez.getUseGL() ? getGLTexture(filename) : getJTexture(filename);
    }

    /**
     * Retrieves the asset at the given location as a {@link mayonez.graphics.textures.GLTexture}.
     *
     * @param filename the texture location
     * @return a texture
     */
    public static GLTexture getGLTexture(String filename) {
        return Assets.getAsset(filename, GLTexture.class);
    }

    /**
     * Retrieves the asset at the given location as a {@link mayonez.graphics.textures.JTexture}.
     *
     * @param filename the texture location
     * @return a texture
     */
    public static JTexture getJTexture(String filename) {
        return Assets.getAsset(filename, JTexture.class);
    }

}
