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
            if (i >= weights.size()) {
                cdfValues[i] = lastCDF;
                continue;
            }
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
