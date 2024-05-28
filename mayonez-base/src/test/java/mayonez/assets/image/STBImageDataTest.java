package mayonez.assets.image;

import org.junit.jupiter.api.*;

import static mayonez.assets.image.ImageTestConstants.*;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for the {@link mayonez.assets.image.STBImageData} class.
 *
 * @author SlavSquatSuperstar
 */
class STBImageDataTest {

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

    private static STBImageData getImage(String filename) {
        try {
            return new STBImageData(filename);
        } catch (Exception e) {
            return fail("Could not read image");
        }
    }

}
