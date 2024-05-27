package mayonez.assets.image;

import org.junit.jupiter.api.*;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for the {@link mayonez.assets.image.STBImageData} class.
 *
 * @author SlavSquatSuperstar
 */
class STBImageDataTest {

    private static final int IMAGE_LENGTH = 32;

    @Test
    void transparentPngHasAlpha() {
        var filename = "testassets/images/spaceship-transparent.png";
        try {
            var image = new STBImageData(filename);
            assertEquals(IMAGE_LENGTH, image.getWidth());
            assertEquals(IMAGE_LENGTH, image.getHeight());
            assertTrue(image.hasAlpha());
        } catch (Exception e) {
            fail();
        }
    }

    @Test
    void opaquePngHasNoAlpha() {
        var filename = "testassets/images/spaceship-opaque.png";
        try {
            var image = new STBImageData(filename);
            assertEquals(IMAGE_LENGTH, image.getWidth());
            assertEquals(IMAGE_LENGTH, image.getHeight());
            assertFalse(image.hasAlpha());
        } catch (Exception e) {
            fail();
        }
    }

    @Test
    void opaqueJpgHasNoAlpha() {
        var filename = "testassets/images/spaceship-opaque.jpg";
        try {
            var image = new STBImageData(filename);
            assertEquals(IMAGE_LENGTH, image.getWidth());
            assertEquals(IMAGE_LENGTH, image.getHeight());
            assertFalse(image.hasAlpha());
        } catch (Exception e) {
            fail();
        }
    }

}
