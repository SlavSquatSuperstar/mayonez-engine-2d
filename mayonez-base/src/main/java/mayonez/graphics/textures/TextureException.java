package mayonez.graphics.textures;

/**
 * An exception occurring when the program cannot successfully create an
 * OpenGL texture.
 *
 * @author SlavSquatSuperstar
 */
class TextureException extends RuntimeException {
    TextureException(String message) {
        super(message);
    }
}
