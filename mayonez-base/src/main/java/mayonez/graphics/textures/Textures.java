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
     * Retrieves the asset at the given path as a AWT or GL texture based on
     * the current engine instance.
     *
     * @param path the texture location
     * @return a texture
     */
    public static Texture getTexture(String path) {
        return Mayonez.getUseGL() ? getGLTexture(path) : getJTexture(path);
    }

    /**
     * Retrieves the asset at the given path as a
     * {@link mayonez.graphics.textures.GLTexture}.
     *
     * @param path the texture location
     * @return a texture
     */
    public static GLTexture getGLTexture(String path) {
        return Assets.getAsset(path, GLTexture.class);
    }

    /**
     * Retrieves the asset at the given path as a
     * {@link mayonez.graphics.textures.JTexture}.
     *
     * @param path the texture location
     * @return a texture
     */
    public static JTexture getJTexture(String path) {
        return Assets.getAsset(path, JTexture.class);
    }

}
