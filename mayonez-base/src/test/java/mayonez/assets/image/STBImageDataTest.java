package mayonez.assets.image;

import mayonez.graphics.*;
import mayonez.math.*;
import org.junit.jupiter.api.*;

import static mayonez.assets.image.BaseImageData.*;
import static mayonez.assets.image.ImageTestUtils.*;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for the {@link mayonez.assets.image.STBImageData} class.
 *
 * @author SlavSquatSuperstar
 */
class STBImageDataTest {

    private static Color[] TEST_COLORS;
    private static Vec2[] TEST_COORDS;

    @BeforeAll
    static void setup() {
        TEST_COLORS = new Color[]{
                new Color(255, 0, 0), new Color(0, 255, 0),
                new Color(0, 0, 255), Color.grayscale(255)
        };
        TEST_COORDS = new Vec2[]{
                new Vec2(0, 0), new Vec2(IMAGE_LENGTH - 1, 0),
                new Vec2(0, IMAGE_LENGTH - 1), new Vec2(IMAGE_LENGTH - 1, IMAGE_LENGTH - 1)
        };
    }

    // Has Alpha

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

    private static STBImageData getImage(String filename) {
        try {
            return new STBImageData(filename);
        } catch (Exception e) {
            return fail("Could not read image");
        }
    }

}
