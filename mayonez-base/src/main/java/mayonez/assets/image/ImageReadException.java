package mayonez.assets.image;

/**
 * An exception occurring when the program cannot successfully read an image file.
 *
 * @author SlavSquatSuperstar
 */
class ImageReadException extends Exception {
    ImageReadException(String message) {
        super(message);
    }
}
