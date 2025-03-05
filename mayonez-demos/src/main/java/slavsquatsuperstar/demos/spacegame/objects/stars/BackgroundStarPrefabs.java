package slavsquatsuperstar.demos.spacegame.objects.stars;

import mayonez.math.Random;
import slavsquatsuperstar.demos.spacegame.PrefabUtils;
import slavsquatsuperstar.demos.spacegame.SpaceGameScene;
import slavsquatsuperstar.demos.spacegame.objects.BackgroundObject;

import java.util.*;

/**
 * Creates prefab background star objects.
 *
 * @author SlavSquatSuperstar
 */
public final class BackgroundStarPrefabs {

    private static final RandomVariable<StarLuminosityType> LUMINOSITY_TYPE_GENERATOR;
    private static final Map<StarLuminosityType, RandomVariable<StarSpectralType>>
            SPECTRAL_TYPE_GENERATORS;

    static {
        // Read CSV files
        var spectralTypes = PrefabUtils
                .getRecordsFromFile("assets/spacegame/data/stars/star_spectral_types.csv")
                .stream().map(StarSpectralType::new).toList();

        var luminosityTypes = PrefabUtils
                .getRecordsFromFile("assets/spacegame/data/stars/star_luminosity_types.csv")
                .stream().map(StarLuminosityType::new).toList();

        var luminosityToSpectra = PrefabUtils
                .getRecordsFromFile("assets/spacegame/data/stars/star_luminosity_to_spectra.csv")
                .stream().map(StarLuminosityToSpectra::new).toList();

        // Create random variables
        var luminosityTypeWeights = luminosityTypes.stream()
                .map(StarLuminosityType::weight).toList();
        LUMINOSITY_TYPE_GENERATOR = new RandomVariable<>(luminosityTypes, luminosityTypeWeights);

        SPECTRAL_TYPE_GENERATORS = new HashMap<>();
        luminosityToSpectra.forEach(
                obj -> {
                    var luminosity = obj.getLuminosity(luminosityTypes);
                    var generator = obj.getSpectralGenerator(spectralTypes);
                    SPECTRAL_TYPE_GENERATORS.put(luminosity, generator);
                }
        );
    }

    private BackgroundStarPrefabs() {
    }

    // Create Prefab Methods

    public static BackgroundObject createRandomStar() {
        var luminosity = LUMINOSITY_TYPE_GENERATOR.getRandomOutcome();
        var spectrum = SPECTRAL_TYPE_GENERATORS.get(luminosity)
                .getRandomOutcome();

        var position = SpaceGameScene.getRandomPosition();
        var radius = luminosity.getRandomRadius();
        var temp = spectrum.getRandomTemp();
        var brightness = luminosity.getRandomBrightness();
        return new BackgroundStar(position, radius, temp, brightness);
    }

    // Helper Class

    static class RandomVariable<T> {

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
