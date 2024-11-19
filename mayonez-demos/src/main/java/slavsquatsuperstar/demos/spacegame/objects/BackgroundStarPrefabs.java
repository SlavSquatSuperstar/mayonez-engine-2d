package slavsquatsuperstar.demos.spacegame.objects;

import mayonez.assets.*;
import mayonez.assets.text.*;
import mayonez.graphics.*;
import mayonez.math.Random;
import mayonez.math.*;
import mayonez.math.shapes.*;

import java.util.*;

/**
 * A background star in outer space.
 *
 * @author SlavSquatSuperstar
 */
public final class BackgroundStarPrefabs {

    // Constants

    private static final CSVFile TYPE_DATA;
    private static final List<StarSpectralType> SPECTRAL_TYPES;
    private static final int NUM_TYPES;
    private static final float[] TYPE_CDFS;
    private static final float TOTAL_WEIGHT;

    static {
        // Read CSV file
        TYPE_DATA = Assets.getAsset("assets/spacegame/data/star_spectral_types.csv",
                CSVFile.class);
        if (TYPE_DATA == null) SPECTRAL_TYPES = Collections.emptyList();
        else SPECTRAL_TYPES = TYPE_DATA.readCSV().stream()
                .map(StarSpectralType::new).toList();
        NUM_TYPES = SPECTRAL_TYPES.size();

        // Create CDF table
        var lastCDF = 0f;
        TYPE_CDFS = new float[NUM_TYPES];
        for (int i = 0; i < NUM_TYPES; i++) {
            var pmf = SPECTRAL_TYPES.get(i).weight();
            var cdf = lastCDF + pmf;
            TYPE_CDFS[i] = cdf;
            lastCDF = cdf;
        }
        TOTAL_WEIGHT = lastCDF;
        System.out.println(Arrays.toString(TYPE_CDFS));
    }

    private BackgroundStarPrefabs() {
    }

    // Create Prefab Methods

    public static BackgroundObject createBackgroundStar(
            Vec2 position, float radius, int temperature
    ) {
        // "LOD" system for sides vs radius
        Shape shape;
        if (radius < 0.02f) {
            shape = new Rectangle(position, new Vec2(radius * 2f));
        } else if (radius < 0.04f) {
            shape = new Polygon(position, 8, radius);
        } else if (radius < 0.06f) {
            shape = new Polygon(position, 16, radius);
        } else {
            shape = new Circle(position, radius);
        }
        return new BackgroundObject(
                shape, getStarColor(temperature), SpaceGameZIndex.BACKGROUND_STAR
        );
    }

    public static StarSpectralType getRandomColorType() {
        var invCDF = Random.randomFloat(0f, TOTAL_WEIGHT);
        for (int i = 0; i < NUM_TYPES; i++) {
            // Return the category with the smallest cdf still greater than the probability
            if (invCDF < TYPE_CDFS[i]) return SPECTRAL_TYPES.get(i);
        }
        return SPECTRAL_TYPES.get(NUM_TYPES - 1); // Should always return
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

        if (temp2 <= 66) {
            red = 255;
        } else {
            red = (int) (330 * Math.pow(temp2 - 60, -0.13));
        }

        if (temp2 <= 66) {
            green = (int) (100 * Math.log(temp2) - 161);
        } else {
            green = (int) (288 * Math.pow(temp2 - 60, -0.076));
        }

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
