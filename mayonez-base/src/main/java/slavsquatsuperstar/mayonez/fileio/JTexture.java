package slavsquatsuperstar.mayonez.fileio;

import slavsquatsuperstar.mayonez.Logger;
import slavsquatsuperstar.mayonez.graphics.Engine;
import slavsquatsuperstar.mayonez.graphics.EngineType;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Objects;

/**
 * An image file used by this program.
 *
 * @author SlavSquatSuperstar
 */
@Engine(EngineType.AWT)
public class JTexture extends Asset {

    private BufferedImage image;

    public JTexture(String filename, AssetType type) {
        super(filename, type);
        Assets.setAsset(filename, this);
        readImage();
    }

    public JTexture(String filename) {
        this(filename, AssetType.LOCAL);
    }

    public void readImage() {
        try (InputStream in = inputStream()) {
            image = ImageIO.read(new ByteArrayInputStream(Objects.requireNonNull(in).readAllBytes()));
        } catch (IOException e) {
            Logger.warn("Could not read image \"%s\".", getFilename());
        } catch (NullPointerException e) {
            Logger.warn("Texture file \"%s\" does not exist.", getFilename());
        }
    }

    public BufferedImage getImage() {
        return image;
    }
}
