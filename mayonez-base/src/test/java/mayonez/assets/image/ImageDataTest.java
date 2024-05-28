package mayonez.assets.image;

import mayonez.graphics.*;
import org.junit.jupiter.api.*;

import static mayonez.assets.image.ImageTestConstants.*;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for the {@link mayonez.assets.image.ImageData} class.
 *
 * @author SlavSquatSuperstar
 */
class ImageDataTest {

    @Test
    void transparentPngHasAlpha() {
        var image = getImage(TRANSPARENT_PNG);
        assertEquals(IMAGE_LENGTH, image.getWidth());
        assertEquals(IMAGE_LENGTH, image.getHeight());
        assertEquals(RGBA_CHANNELS, image.getChannels());
        assertTrue(image.hasAlpha());
    }

    @Test
    void opaquePngHasNoAlpha() {
        var image = getImage(OPAQUE_PNG);
        assertEquals(IMAGE_LENGTH, image.getWidth());
        assertEquals(IMAGE_LENGTH, image.getHeight());
        assertEquals(RGB_CHANNELS, image.getChannels());
        assertFalse(image.hasAlpha());
    }

    @Test
    void opaqueJpgHasNoAlpha() {
        var image = getImage(OPAQUE_JPG);
        assertEquals(IMAGE_LENGTH, image.getWidth());
        assertEquals(IMAGE_LENGTH, image.getHeight());
        assertEquals(RGB_CHANNELS, image.getChannels());
        assertFalse(image.hasAlpha());
    }

    @Test
    void transparentPngGetPixelsCorrect() {
        var image = getImage(TRANSPARENT_PNG);
        assertEquals(new Color(255, 0, 0, 128),
                image.getPixelColor(0, 0));
    }

    @Test
    void opaquePngGetPixelsCorrect() {
        var image = getImage(OPAQUE_PNG);
        assertEquals(new Color(255, 0, 0, 255),
                image.getPixelColor(0, 0));
    }

    @Test
    void opaqueJpgGetPixelsCorrect() {
        var image = getImage(OPAQUE_JPG);
        assertEquals(new Color(254, 0, 0, 255),
                image.getPixelColor(0, 0));
    }

    private static ImageData getImage(String filename) {
        try {
            return new ImageData(filename);
        } catch (Exception e) {
            return fail("Could not read image");
        }
    }

}
