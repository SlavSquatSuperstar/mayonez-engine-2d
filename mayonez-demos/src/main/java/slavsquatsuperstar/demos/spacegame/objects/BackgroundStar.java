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
     * Converts a color temperature to its approximate RGB color.
     * <br>
     * Source: <a href="https://tannerhelland.com/2012/09/18/convert-temperature-rgb-algorithm-code.html">
     * Tanner Helland</a>
     *
     * @param temperature the start temperature, in Kelvins
     */
    private static Color getStarColor(int temperature) {
        int temp2 = temperature / 100;
        int red, green, blue;

        // Red, Green
        if (temp2 <= 66) {
            red = 255;
            green = (int) (100 * Math.log(temp2) - 161);
        } else {
            red = (int) (330 * Math.pow(temp2 - 60, -0.13));
            green = (int) (288 * Math.pow(temp2 - 60, -0.076));
        }

        // Blue
        if (temp2 >= 66) {
            blue = 255;
        } else if (temp2 <= 19) {
            blue = 0;
        } else {
            blue = (int) (139 * Math.log(temp2 - 10) - 305);
        }

        return new Color(red, green, blue);
    }

}
