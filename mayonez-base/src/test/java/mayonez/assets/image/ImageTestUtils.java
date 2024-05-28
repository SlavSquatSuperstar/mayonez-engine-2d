package mayonez.assets.image;

import mayonez.graphics.*;
import mayonez.math.*;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Constant values and helper functions for the {@link mayonez.assets.image} tests.
 *
 * @author SlavSquatSuperstar
 */
final class ImageTestUtils {

    // Numbers
    static final int IMAGE_LENGTH = 16;

    // Filenames
    static final String TRANSPARENT_PNG = "testassets/images/squares-transparent.png";
    static final String OPAQUE_PNG = "testassets/images/squares-opaque.png";
    static final String OPAQUE_JPG = "testassets/images/squares-opaque.jpg";

    private ImageTestUtils() {}

    static void testPixelColors(BaseImageData image, Color[] colors, Vec2[] coords, int alpha) {
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

}
