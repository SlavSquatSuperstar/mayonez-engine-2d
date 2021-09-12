package slavsquatsuperstar.fileio;

import slavsquatsuperstar.mayonez.Logger;

import java.io.IOException;
import java.io.InputStream;
import java.util.Objects;

/**
 * An image file used by this program.
 *
 * @author SlavSquatSuperstar
 */
public class Texture extends Asset {

    private byte[] imageData;

    public Texture(String filename, AssetType type) {
        super(filename, type);
        Assets.setAsset(filename, this);
        readImage();
    }

    public Texture(String filename) {
        this(filename, AssetType.LOCAL);
    }

    public void readImage() {
        try (InputStream in = inputStream()) {
            imageData = Objects.requireNonNull(in).readAllBytes();
        } catch (IOException e) {
            Logger.warn("Could not read image \"%s\".", getFilename());
        } catch (NullPointerException e) {
            Logger.warn("Texture file \"%s\" does not exist.", getFilename());
        }
    }

    public byte[] getImageData() {
        return imageData;
    }
}
