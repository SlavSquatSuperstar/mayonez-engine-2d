package slavsquatsuperstar.demos.spacegame.objects.stars;

import mayonez.util.Record;

import java.util.*;

/**
 * Defines what combinations of spectral classes and luminosity classes can occur.
 *
 * @author SlavSquatSuperstar
 */
class StarLuminosityToSpectra {

    private final String luminosityName;
    private final String[] spectralNames;
    private final boolean weightedSpectra;

    StarLuminosityToSpectra(Record record) {
        this.luminosityName = record.getString("luminosity");
        this.spectralNames = record.getString("spectra").split(",");
        this.weightedSpectra = record.getBoolean("weighted_spectra");
    }

    StarLuminosityType getLuminosity(List<StarLuminosityType> luminosityTypes) {
        // Search luminosity types with name
        return luminosityTypes.stream()
                .filter(type -> type.type().equals(luminosityName))
                .findFirst().orElse(null);
    }

    List<StarSpectralType> getSpectra(List<StarSpectralType> spectralTypes) {
        // Search spectral types for names
        var names = Arrays.asList(spectralNames);
        return spectralTypes.stream()
                .filter(type -> names.contains(type.type()))
                .toList();
    }

    BackgroundStarPrefabs.RandomVariable<StarSpectralType> getSpectralGenerator(
            List<StarSpectralType> spectralTypes
    ) {
        var outcomes = getSpectra(spectralTypes);

        List<Float> weights;
        if (weightedSpectra) {
            weights = outcomes.stream()
                    .map(StarSpectralType::weight).toList();
        } else {
            weights = outcomes.stream()
                    .map(type -> 1f).toList();
        }

        return new BackgroundStarPrefabs.RandomVariable<>(outcomes, weights);
    }

}
