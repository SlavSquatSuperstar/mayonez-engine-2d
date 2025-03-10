package mayonez.math;

import org.junit.jupiter.api.*;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for the {@link mayonez.math.RandomVariable} class.
 *
 * @author SlavSquatSuperstar
 */
class RandomVariableTest {

    @Test
    void cdfValuesCorrect() {
        var outcomes = List.of("A", "B", "C", "D", "E");
        var weights = List.of(1f, 2f, 3f, 4f, 5f);
        var cdfs = new float[] {1f, 3f, 6f, 10f, 15f};
        var maxCdf = 15f;
        testCdfValues(outcomes, weights, cdfs, maxCdf);
    }

    @Test
    void cdfValuesHasZeroesCorrect() {
        var outcomes = List.of("A", "B", "C", "D", "E");
        var weights = List.of(1f, 2f, 0f, 3f, 0f);
        var cdfs = new float[] {1f, 3f, 3f, 6f, 6f};
        var maxCdf = 6f;
        testCdfValues(outcomes, weights, cdfs, maxCdf);
    }

    @Test
    void cdfValuesMissingWeightsCorrect() {
        var outcomes = List.of("A", "B", "C", "D", "E");
        var weights = List.of(1f, 2f, 3f);
        var cdfs = new float[] {1f, 3f, 6f, 6f, 6f};
        var maxCdf = 6f;
        testCdfValues(outcomes, weights, cdfs, maxCdf);
    }

    @Test
    void cdfValuesExtraWeightsCorrect() {
        var outcomes = List.of("A", "B", "C", "D", "E");
        var weights = List.of(1f, 2f, 3f, 4f, 5f, 6f, 7f);
        var cdfs = new float[] {1f, 3f, 6f, 10f, 15f};
        var maxCdf = 15f;
        testCdfValues(outcomes, weights, cdfs, maxCdf);
    }

    private static void testCdfValues(
            List<?> outcomes, List<Float> weights, float[] cdfs, float maxCdf
    ) {
        var rand = new RandomVariable<>(outcomes, weights);
        assertArrayEquals(cdfs, rand.getCDFValues());
        assertEquals(maxCdf, rand.getMaxCDFValue());
    }

}
