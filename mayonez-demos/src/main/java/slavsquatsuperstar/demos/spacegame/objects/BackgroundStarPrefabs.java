package slavsquatsuperstar.demos.spacegame.objects;

import mayonez.math.Random;
import slavsquatsuperstar.demos.spacegame.PrefabUtils;
import slavsquatsuperstar.demos.spacegame.SpaceGameScene;

import java.util.*;

/**
 * Creates prefab background star objects.
 *
 * @author SlavSquatSuperstar
 */
public final class BackgroundStarPrefabs {

    // Constants
    private static final RandomVariable<StarSpectralType> SPECTRAL_TYPE_GENERATOR;
    private static final RandomVariable<StarLuminosityType> LUMINOSITY_TYPE_GENERATOR;

    static {
        // Read CSV files
        var spectralTypes = PrefabUtils
                .getRecordsFromFile("assets/spacegame/data/star_spectral_types.csv")
                .stream().map(StarSpectralType::new).toList();

        var luminosityTypes = PrefabUtils
                .getRecordsFromFile("assets/spacegame/data/star_luminosity_types.csv")
                .stream().map(StarLuminosityType::new).toList();

        // Create random variables
        var spectralTypeWeights = spectralTypes.stream()
                .map(StarSpectralType::weight).toList();
        SPECTRAL_TYPE_GENERATOR = new RandomVariable<>(spectralTypes, spectralTypeWeights);

        var luminosityTypeWeights = luminosityTypes.stream()
                .map(StarLuminosityType::weight).toList();
        LUMINOSITY_TYPE_GENERATOR = new RandomVariable<>(luminosityTypes, luminosityTypeWeights);
    }

    private BackgroundStarPrefabs() {
    }

    // Create Prefab Methods

    public static BackgroundObject createRandomStar() {
        var luminosity = LUMINOSITY_TYPE_GENERATOR.getRandomOutcome();
        var position = SpaceGameScene.getRandomPosition();
        var radius = luminosity.getRandomRadius();
        var temp = SPECTRAL_TYPE_GENERATOR.getRandomOutcome().getRandomTemp();
        var brightness = luminosity.getRandomBrightness();
        return new BackgroundStar(position, radius, temp, brightness);
    }

    // Helper Class

    private static class RandomVariable<T> {

        private final List<T> outcomes;
        private final int numOutcomes;
        private float[] cdfValues;
        private float maxCDFValue;

        public RandomVariable(List<T> outcomes, List<Float> weights) {
            this.outcomes = outcomes;
            numOutcomes = outcomes.size();
            calculateCDFValues(weights);
        }

        private void calculateCDFValues(List<Float> weights) {
            var lastCDF = 0f;
            cdfValues = new float[numOutcomes];
            for (int i = 0; i < numOutcomes; i++) {
                var pmf = weights.get(i); // Not technically a pmf since can be >1
                var cdf = lastCDF + pmf;
                cdfValues[i] = cdf;
                lastCDF = cdf;
            }
            maxCDFValue = lastCDF;
        }

        public T getRandomOutcome() {
            var invCDF = Random.randomFloat(0f, maxCDFValue);
            for (int i = 0; i < numOutcomes; i++) {
                // Return the category with the smallest cdf still greater than the probability
                if (invCDF < cdfValues[i]) return outcomes.get(i);
            }
            return outcomes.get(numOutcomes - 1); // Should always return
        }

    }

}
