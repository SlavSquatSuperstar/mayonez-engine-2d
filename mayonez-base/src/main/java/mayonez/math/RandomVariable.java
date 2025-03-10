package mayonez.math;

import java.util.*;

/**
 * A categorical or discrete random variable with a finite set of outcomes.
 * Each outcome can be given a weight that determines how often it occurs
 * relative to the other outcomes.
 *
 * @param <T> the outcome type
 * @author SlavSquatSuperstar
 */
public class RandomVariable<T> {

    private final List<T> outcomes;
    private final int numOutcomes;
    private final float[] cdfValues;
    private final float maxCDFValue;

    public RandomVariable(List<T> outcomes, List<Float> weights) {
        this.outcomes = outcomes;
        numOutcomes = outcomes.size();
        cdfValues = calculateCDFValues(weights);
        maxCDFValue = cdfValues[numOutcomes - 1];
    }

    private float[] calculateCDFValues(List<Float> weights) {
        var lastCDF = 0f;
        var cdfValues = new float[numOutcomes];
        for (int i = 0; i < numOutcomes; i++) {
            if (i >= weights.size()) {
                cdfValues[i] = lastCDF;
                continue;
            }
            var pmf = weights.get(i); // Not technically a pmf since can be >1
            var cdf = lastCDF + pmf;
            cdfValues[i] = cdf;
            lastCDF = cdf;
        }
        return cdfValues;
    }

    public T getRandomOutcome() {
        var invCDF = Random.randomFloat(0f, maxCDFValue);
        for (int i = 0; i < numOutcomes; i++) {
            // Return the category with the smallest cdf still greater than the probability
            if (invCDF < cdfValues[i]) return outcomes.get(i);
        }
        return outcomes.get(numOutcomes - 1); // Should always return
    }

    float[] getCDFValues() {
        return cdfValues;
    }

    float getMaxCDFValue() {
        return maxCDFValue;
    }

}
