package slavsquatsuperstar.mayonez.io;

/**
 * An image file used by this program.
 *
 * @author SlavSquatSuperstar
 */
public abstract class Texture extends Asset {

    public Texture(String filename) {
        super(filename);
        readImage();
    }

    /**
     * Creates a texture from an image file. Called during creation.
     */
    protected abstract void readImage();

    public abstract int getWidth();

    public abstract int getHeight();

}
