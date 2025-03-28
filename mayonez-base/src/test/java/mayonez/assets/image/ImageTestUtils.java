package mayonez.assets.image;

import mayonez.graphics.*;
import mayonez.math.*;
import org.junit.jupiter.api.*;

import static mayonez.assets.image.ImageData.*;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Constant values and helper functions for the {@link mayonez.assets.image} tests.
 *
 * @author SlavSquatSuperstar
 */
abstract class ImageTestUtils {

    // Image Fields
    static final int IMAGE_LENGTH = 16;
    static Color[] TEST_COLORS;
    static Vec2[] TEST_COORDS;

    // Image Filenames
    static final String TRANSPARENT_PNG = "testassets/images/squares-transparent.png";
    static final String OPAQUE_PNG = "testassets/images/squares-opaque.png";
    static final String OPAQUE_JPG = "testassets/images/squares-opaque.jpg";

    @BeforeAll
    static void setup() {
        TEST_COLORS = new Color[]{
                new Color(255, 0, 0), new Color(0, 255, 0),
                new Color(0, 0, 255), Color.grayscale(255)
        };
        TEST_COORDS = getImageCoords(IMAGE_LENGTH);
    }

    // Helper Methods

    static Vec2[] getImageCoords(int imageLength) {
        return new Vec2[]{
                new Vec2(0, 0), new Vec2(imageLength - 1, 0),
                new Vec2(0, imageLength - 1), new Vec2(imageLength - 1, imageLength - 1)
        };
    }

    static void testImageSizeAndChannels(ImageData image, boolean alpha) {
        assertEquals(IMAGE_LENGTH, image.getWidth());
        assertEquals(IMAGE_LENGTH, image.getHeight());
        if (alpha) {
            assertEquals(RGBA_CHANNELS, image.getChannels());
            assertTrue(image.hasAlpha());
        } else {
            assertEquals(RGB_CHANNELS, image.getChannels());
            assertFalse(image.hasAlpha());
        }
    }

    static void testPixelColors(ImageData image, Color[] colors, Vec2[] coords, int alpha) {
        for (int i = 0; i < colors.length; i++) {
            var coord = coords[i];
            assertColorsRoughlyEqual(new Color(colors[i], alpha), image.getPixelColor((int) coord.x, (int) coord.y));
        }
    }

    static void assertColorsRoughlyEqual(Color expected, Color actual) {
        assertEquals(expected.getRed(), actual.getRed(), 1);
        assertEquals(expected.getGreen(), actual.getGreen(), 1);
        assertEquals(expected.getBlue(), actual.getBlue(), 1);
        assertEquals(expected.getAlpha(), actual.getAlpha(), 1);
    }

    static void testSubImagePixels(ImageData image, Color[] testColors) {
        var subImageLength = IMAGE_LENGTH / 2;
        var region = new ImageRegion(new Vec2(4), new Vec2(subImageLength));
        var subImage = image.getSubImageData(region);
        assertNotNull(subImage);

        var subImageCoords = getImageCoords(subImageLength);
        testPixelColors(subImage, testColors, subImageCoords, 128);
    }

}
