package mayonez.graphics.textures;

/**
 * An exception occurring when the program cannot successfully read a
 * texture file.
 *
 * @author SlavSquatSuperstar
 */
class TextureException extends RuntimeException {
    TextureException(String message) {
        super(message);
    }
}
