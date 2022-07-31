package slavsquatsuperstar.mayonez.io;

import slavsquatsuperstar.mayonez.Logger;
import slavsquatsuperstar.mayonez.annotations.UsesEngine;
import slavsquatsuperstar.mayonez.annotations.EngineType;

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
@UsesEngine(EngineType.AWT)
public class JTexture extends Asset {

    private BufferedImage image;

    public JTexture(String filename) {
        super(filename);
        readImage();
    }

    public void readImage() {
        try (InputStream in = inputStream()) {
            image = ImageIO.read(new ByteArrayInputStream(Objects.requireNonNull(in).readAllBytes()));
            Logger.debug("I/O: Loaded image \"%s\"", getFilename());
        } catch (IOException e) {
            Logger.error("I/O: Could not read image \"%s\"", getFilename());
        } catch (NullPointerException e) {
            Logger.error("I/O: Texture file \"%s\" not found", getFilename());
        }
    }

    public BufferedImage getImage() {
        return image;
    }
}
