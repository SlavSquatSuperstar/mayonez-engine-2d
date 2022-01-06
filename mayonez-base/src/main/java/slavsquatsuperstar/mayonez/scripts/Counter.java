package slavsquatsuperstar.mayonez.scripts;

import slavsquatsuperstar.math.Range;
import slavsquatsuperstar.mayonez.Script;

public class Counter extends Script {

    // Counter Status
    private boolean started; // whether start counting
    private boolean countDown; // the count direction

    // Counter Parameters
    private final Range interval;
    private float increment = 0;
    private float value; // current value

    /**
     * Creates a counter that counts automatically in real time as the game updates.
     *
     * @param min       the minimum value the counter should reach
     * @param max       the maximum value the counter should reach
     * @param countDown whether to count down to 0 rather than up from 0
     */
    public Counter(float min, float max, boolean countDown) {
        interval = new Range(min, max);
        this.countDown = countDown;
        reset();
    }

    /**
     * Creates a counter that counts manually upon the user's input.
     *
     * @param min       the minimum value the counter should reach
     * @param max       the maximum value the counter should reach
     * @param increment how much to change the value each count
     */
    public Counter(float min, float max, boolean countdown, float increment) {
        this(min, max, countdown);
        this.increment = increment;
    }

    @Override
    public void start() {
        started = true;
    }

    @Override
    public void update(float dt) {
        if (started && increment == 0) { // auto count if increment not set
            if (countDown) value -= dt;
            else value += dt;
        }
    }

    // Getters and Setters

    public float getValue() {
        return value;
    }

    /**
     * Query whether the counter value has reached the target value. If counting up, returns true once value is greater
     * than or equal to max. If * counting down, returns true once value is less than or equal to min.
     *
     * @return if the counter is ready
     */
    public boolean isReady() {
        return countDown ? value <= interval.min : value >= interval.max;
    }

    public Counter setInitialValue(float value) {
        this.value = value;
        return this;
    }

    /**
     * Set the starting value of the counter, depending on the direction.
     */
    public void reset() {
        value = countDown ? interval.max : interval.min;
    }

    /**
     * Manually tick the counter by the defined increment.
     */
    public void count() {
        value += increment;
    }

    /**
     * Start or pause the counter.
     *
     * @param started if the counter should continue counting
     */
    public void setStarted(boolean started) {
        this.started = started;
    }

}
