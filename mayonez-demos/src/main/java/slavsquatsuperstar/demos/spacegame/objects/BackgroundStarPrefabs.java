package slavsquatsuperstar.demos.spacegame.objects;

import mayonez.assets.*;
import mayonez.assets.text.*;
import mayonez.math.Random;
import slavsquatsuperstar.demos.spacegame.SpaceGameScene;

import java.util.*;

/**
 * Creates prefab background star objects.
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
    }

    private BackgroundStarPrefabs() {
    }

    // Create Prefab Methods

    public static BackgroundObject createRandomStar() {
        var position = SpaceGameScene.getRandomPosition();
        var radius = getRandomStarRadius();
        var temp = getRandomColorType().getRandomTemp();
        return new BackgroundStar(position, radius, temp);
    }

    private static float getRandomStarRadius() {
        float invCDF = Random.randomFloat(0f, 100f);
        if (invCDF < 60f) {
            return Random.randomFloat(0.01f, 0.04f); // Dwarf
        } else if (invCDF < 90f) {
            return Random.randomFloat(0.04f, 0.07f); // Giant
        } else {
            return Random.randomFloat(0.07f, 0.08f); // Supergiant
        }
    }

    private static StarSpectralType getRandomColorType() {
        var invCDF = Random.randomFloat(0f, TOTAL_WEIGHT);
        for (int i = 0; i < NUM_TYPES; i++) {
            // Return the category with the smallest cdf still greater than the probability
            if (invCDF < TYPE_CDFS[i]) return SPECTRAL_TYPES.get(i);
        }
        return SPECTRAL_TYPES.get(NUM_TYPES - 1); // Should always return
    }

}
