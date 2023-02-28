package mayonez.scripts;

import mayonez.*;
import mayonez.math.*;

/**
 * A script that tracks a quantity, manually increments it, and can be reset.
 *
 * @author SlavSquatSuperstar
 */
public class Counter extends Script {

    private Interval interval; // min and max
    private float value; // current value

    /**
     * Create a counter with the given interval and set its starting value.
     *
     * @param min        the lowest value the counter should reach
     * @param max        the highest value the counter should reach
     * @param startValue where the counter should begin
     */
    public Counter(float min, float max, float startValue) {
        this.interval = new Interval(min, max);
        value = startValue;
    }

    // Getters and Setters

    /**
     * Counts up or down by the given value.
     *
     * @param increment now much to count
     */
    public void count(float increment) {
        value += increment;
    }

    public float getMin() {
        return interval.min;
    }

    public float getMax() {
        return interval.max;
    }

    public float getValue() {
        return value;
    }

    public void setValue(float value) {
        this.value = value;
    }

    /**
     * Set the counter's count interval.
     *
     * @param min the lower bound
     * @param max the upper bound
     */
    public void setInterval(float min, float max) {
        this.interval = new Interval(min, max);
    }

    /**
     * Whether the counter has count down to its min value.
     *
     * @return if the counter is ready
     */
    public boolean isAtMin() {
        return value <= interval.min;
    }

    /**
     * Whether the counter has count up to its max value.
     *
     * @return if the counter is ready
     */
    public boolean isAtMax() {
        return value >= interval.max;
    }

    /**
     * Reset the counter to its min value.
     */
    public void resetToMin() {
        value = interval.min;
    }

    /**
     * Reset the counter to its max value.
     */
    public void resetToMax() {
        value = interval.max;
    }

    @Override
    public String toString() {
        return String.format("Counter (%.4f-%.4f, %.4f )", value, interval.min, interval.max);
    }
}
