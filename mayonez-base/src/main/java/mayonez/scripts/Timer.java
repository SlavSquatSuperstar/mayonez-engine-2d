package mayonez.scripts;

import mayonez.*;

/**
 * Automatically counts down in real time as the game updates and can be reset. All timer
 * components will start counting down once the scene is started. To pause the timer until
 * later, call {@code setEnabled(false)} and re-enable the timer later.
 *
 * @author SlavSquatSuperstar
 */
public class Timer extends Script {

    private float duration; // max value
    private float value; // current value
    private boolean started; // if started counting

    /**
     * Create a timer that counts down for the given duration.
     *
     * @param duration how long to count for until ready, in seconds
     */
    public Timer(float duration) {
        this(duration, duration);
    }

    /**
     * Create a timer that counts down for the given duration and set its starting value.
     *
     * @param duration   how long to count for until ready, in seconds
     * @param startValue where the timer should begin
     */
    public Timer(float duration, float startValue) {
        this.duration = duration;
        value = startValue;
    }

    // Game Loop methods

    @Override
    protected void start() {
        started = true;
    }

    @Override
    protected void update(float dt) {
        if (started) value -= dt;
    }

    // Getters and Setters

    /**
     * How long the timer counts until it is ready.
     *
     * @return the count duration
     */
    public float getDuration() {
        return duration;
    }

    /**
     * How long until the timer is ready.
     *
     * @return the remaining value
     */
    public float getValue() {
        return value;
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

    /**
     * Set the timer's count duration and reset its value.
     *
     * @param duration how long to count for
     */
    public void setDuration(float duration) {
        this.duration = duration;
        reset();
    }

    /**
     * Start or pause the timer.
     *
     * @param started if the timer is counting
     */
    public void setStarted(boolean started) {
        this.started = started;
    }

    @Override
    public String toString() {
        return String.format("Timer (%.4f/%.4f sec)", value, duration);
    }
}
