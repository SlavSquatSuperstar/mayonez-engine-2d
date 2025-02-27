package slavsquatsuperstar.demos.spacegame.objects.stars;

import mayonez.graphics.*;
import mayonez.math.*;
import mayonez.math.shapes.*;
import slavsquatsuperstar.demos.spacegame.objects.BackgroundObject;
import slavsquatsuperstar.demos.spacegame.objects.SpaceGameZIndex;

/**
 * A background star in outer space.
 *
 * @author SlavSquatSuperstar
 */
class BackgroundStar extends BackgroundObject {

    BackgroundStar(Vec2 position, float radius, int temperature, float brightness) {
        super(getStarShape(position, radius), getStarColor(temperature, brightness),
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

    /*
     * TODO: Brightness to RGB
     *
     * Angular Diam = Linear Diam / Distance
     * Relative Diam = Angular Diam * Focal Length
     *
     * Actual Color = (Luminosity / Distance^2) * Color
     * Mag vs Brightness: 1 step = (100)^1/5 times
     * Abs Mag vs App Mag: M = m - 5*log_10(d_pc) + 5
     * Abs Mag vs Lum: M = M_S - 2.5*log_10(L/L_S)
     * Lum vs Mass (Main-Seq): L/L_S = (M/M_S)^3.5
     *
     * https://en.wikipedia.org/wiki/Stellar_classification
     * https://en.wikipedia.org/wiki/Hertzsprung%E2%80%93Russell_diagram
     *
     * https://en.wikipedia.org/wiki/Magnitude_(astronomy)
     * https://en.wikipedia.org/wiki/Absolute_magnitude
     * https://en.wikipedia.org/wiki/Apparent_magnitude
     * https://en.wikipedia.org/wiki/Luminosity
     * https://en.wikipedia.org/wiki/Mass%E2%80%93luminosity_relation
     */
    private static Color getStarColor(int temperature, float brightness) {
        return getStarColor(temperature)
                .combine(Color.grayscale((int) (brightness * 255)));
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
