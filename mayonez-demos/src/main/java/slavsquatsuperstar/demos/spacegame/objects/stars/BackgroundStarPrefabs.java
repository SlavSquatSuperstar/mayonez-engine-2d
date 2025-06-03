package slavsquatsuperstar.demos.spacegame.objects.stars;

import mayonez.math.*;
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
        var spectralTypes = PrefabUtils.getObjectsFromFile(
                "assets/spacegame/data/stars/star_spectral_types.csv",
                StarSpectralType::new
        );
        var luminosityTypes = PrefabUtils.getObjectsFromFile(
                "assets/spacegame/data/stars/star_luminosity_types.csv",
                StarLuminosityType::new
        );
        var luminosityToSpectra = PrefabUtils.getObjectsFromFile(
                "assets/spacegame/data/stars/star_luminosity_to_spectra.csv",
                StarLuminosityToSpectra::new
        );

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
        var spectrum = SPECTRAL_TYPE_GENERATORS.get(luminosity).getRandomOutcome();

        var position = SpaceGameScene.getRandomPosition();
        var radius = luminosity.getRandomRadius();
        var temp = spectrum.getRandomTemp();
        var brightness = luminosity.getRandomBrightness();
        return new BackgroundStar(position, radius, temp, brightness);
    }

}
