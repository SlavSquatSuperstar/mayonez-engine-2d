package mayonez.scripts;

/**
 * Manually counts down in-game time and detects once the time has reached zero
 * with {@link #isReady}. The timer can be counted using {@link #countDown},
 * paused/resumed using {@link #setPaused}, and reset using {@link #reset}.
 *
 * @author SlavSquatSuperstar
 */
public class Timer {

    private float duration; // max value
    private float value; // current value
    private boolean paused; // if counting paused

    /**
     * Create a timer that counts down for the given duration.
     *
     * @param duration how long to count for until ready, in seconds
     */
    public Timer(float duration) {
        this(duration, duration);
    }

    /**
     * Create a timer that counts down for the given duration and set its starting
     * value.
     *
     * @param duration   how long to count for until ready, in seconds
     * @param startValue where the timer should begin
     */
    public Timer(float duration, float startValue) {
        this.duration = duration;
        value = startValue;
        paused = false;
    }

    // Game Loop methods

    /**
     * Count down the timer's value if it is not paused.
     *
     * @param time the amount of time that has passed
     */
    public void countDown(float time) {
        if (!paused) value -= time;
    }

    // Getters and Setters

    /**
     * How long the timer counts in seconds until it is ready.
     *
     * @return the count duration
     */
    public float getDuration() {
        return duration;
    }

    /**
     * Set the timer's count duration in seconds.
     *
     * @param duration how long to count for
     */
    public void setDuration(float duration) {
        this.duration = duration;
    }

    /**
     * How long in seconds until the timer is ready.
     *
     * @return the remaining value
     */
    public float getValue() {
        return value;
    }

    /**
     * Set the timer's remaining count value in seconds.
     *
     * @param value how much to set the value
     */
    public void setValue(float value) {
        this.value = value;
    }

    /**
     * Whether the timer is paused and should not count, false by default.
     *
     * @return if the timer is paused
     */
    public boolean isPaused() {
        return paused;
    }

    /**
     * Control whether the timer is paused.
     *
     * @param paused if the timer should pause
     */
    public void setPaused(boolean paused) {
        this.paused = paused;
    }

    /**
     * Whether the timer has count down to zero.
     *
     * @return if the timer is ready
     */
    public boolean isReady() {
        return value <= 0f;
    }

    /**
     * Reset the timer to its starting value.
     */
    public void reset() {
        value = duration;
    }

    @Override
    public String toString() {
        return String.format("Timer (%.4f/%.4f sec)", value, duration);
    }

}
