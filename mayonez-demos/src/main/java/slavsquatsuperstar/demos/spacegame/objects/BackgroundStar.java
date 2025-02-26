package slavsquatsuperstar.demos.spacegame.objects;

import mayonez.graphics.*;
import mayonez.math.*;
import mayonez.math.shapes.*;

/**
 * A background star in outer space.
 *
 * @author SlavSquatSuperstar
 */
public class BackgroundStar extends BackgroundObject {

    public BackgroundStar(Vec2 position, float radius, int temp) {
        super(getStarShape(position, radius), getStarColor(temp),
                SpaceGameZIndex.BACKGROUND_STAR);
    }

    // "LOD" system for sides vs radius
    private static Shape getStarShape(Vec2 position, float radius) {
        if (radius < 0.02f) {
            return new Rectangle(position, new Vec2(radius * 2f));
        } else if (radius < 0.04f) {
            return new Polygon(position, 8, radius);
        } else if (radius < 0.06f) {
            return new Polygon(position, 16, radius);
        } else {
            return new Circle(position, radius);
        }
    }

    /**
     * Converts a color temperature to its approximate RGB color. Note that this is the
     * effective black-body temperature, which may differ from the surface temperature.
     * <p>
     * Sources:
     * <ul>
     *     <li><a href="https://tannerhelland.com/2012/09/18/convert-temperature-rgb-algorithm-code.html">
     *         Tanner Helland - Convert temperature to RGB</a></li>
     *     <li><a href="http://www.vendian.org/mncharity/dir3/starcolor/">
     *         Vendian - What color are the stars?</a></li>
     *     <li><a href="http://www.vendian.org/mncharity/dir3/starcolor/details.html">
     *         Vendian - Star color details?</a></li>
     * </ul>
     * See Also:
     * <ul>
     *     <li><a href="https://en.wikipedia.org/wiki/Color_temperature">
     *         Wikipedia - Color temperature</a></li>
     *     <li><a href="https://en.wikipedia.org/wiki/Color_index">
     *         Wikipedia - Color index</a></li>
     * </ul>
     *
     * @param temperature the star's temperature, in Kelvins
     */
    private static Color getStarColor(int temperature) {
        float tempDiv100 = temperature / 100f;
        int red, green, blue;

        // Red, Green
        if (tempDiv100 <= 66) {
            red = 255;
            green = (int) (100 * Math.log(tempDiv100) - 161);
        } else {
            red = (int) (330 * Math.pow(tempDiv100 - 60, -0.13));
            green = (int) (288 * Math.pow(tempDiv100 - 60, -0.076));
        }

        // Blue
        if (tempDiv100 >= 66) {
            blue = 255;
        } else if (tempDiv100 <= 19) {
            blue = 0;
        } else {
            blue = (int) (139 * Math.log(tempDiv100 - 10) - 305);
        }

        return new Color(red, green, blue);
    }

}
