package mayonez.assets.image;

import mayonez.graphics.*;
import org.junit.jupiter.api.*;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for the {@link mayonez.assets.image.STBImageData} class.
 *
 * @author SlavSquatSuperstar
 */
class STBImageDataTest extends ImageTestUtils {

    // Has Alpha

    @Test
    void transparentPngHasAlpha() {
        var image = getImage(TRANSPARENT_PNG);
        testImageSizeAndChannels(image, true);
    }

    @Test
    void opaquePngHasNoAlpha() {
        var image = getImage(OPAQUE_PNG);
        testImageSizeAndChannels(image, false);
    }

    @Test
    void opaqueJpgHasNoAlpha() {
        var image = getImage(OPAQUE_JPG);
        testImageSizeAndChannels(image, false);
    }

    // Get Pixel

    @Test
    void transparentPngGetPixelCorrect() {
        var image = getImage(TRANSPARENT_PNG);
        testPixelColors(image, TEST_COLORS, TEST_COORDS, 128);
    }

    @Test
    void opaquePngGetPixelCorrect() {
        var image = getImage(OPAQUE_PNG);
        testPixelColors(image, TEST_COLORS, TEST_COORDS, 255);
    }

    @Test
    void opaqueJpgGetPixelCorrect() {
        var image = getImage(OPAQUE_JPG);
        testPixelColors(image, TEST_COLORS, TEST_COORDS, 255);
    }

    // Set Pixel

    @Test
    void transparentPngCanSetPixelAlpha() {
        var image = getImage(TRANSPARENT_PNG);
        image.setPixelColor(0, 0, Color.grayscale(0, 128));
        assertEquals(Color.grayscale(0, 128), image.getPixelColor(0, 0));
    }

    @Test
    void opaquePngCannotSetPixelAlpha() {
        var image = getImage(OPAQUE_PNG);
        image.setPixelColor(0, 0, Color.grayscale(0, 128));
        assertEquals(Colors.BLACK, image.getPixelColor(0, 0));
    }

    @Test
    void opaqueJpgCannotSetPixelAlpha() {
        var image = getImage(OPAQUE_JPG);
        image.setPixelColor(0, 0, Color.grayscale(0, 128));
        assertEquals(Colors.BLACK, image.getPixelColor(0, 0));
    }

    // Sub-Image

    @Test
    void subImagePixelsCorrect() {
        var image = getImage(TRANSPARENT_PNG);
        testSubImagePixels(image, TEST_COLORS);
    }

    private static STBImageData getImage(String filename) {
        try {
            return new STBImageData(filename);
        } catch (Exception e) {
            return fail("Could not read image");
        }
    }

}
