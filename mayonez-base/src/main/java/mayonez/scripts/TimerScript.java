package mayonez.scripts;

import mayonez.*;

/**
 * Automatically counts down in real time as the game updates and detects once the
 * time has reached zero with {@link #isReady}. The timer starts counting when the
 * scene starts and can be reset with {@link #reset}. To pause/resume the timer, call
 * {@link #setStarted}.
 *
 * @author SlavSquatSuperstar
 */
public class TimerScript extends Script {

    private float duration; // max value
    private float value; // current value
    private boolean started; // if started counting

    /**
     * Create a timer that counts down for the given duration.
     *
     * @param duration how long to count for until ready, in seconds
     */
    public TimerScript(float duration) {
        this(duration, duration);
    }

    /**
     * Create a timer that counts down for the given duration and set its starting
     * value.
     *
     * @param duration   how long to count for until ready, in seconds
     * @param startValue where the timer should begin
     */
    public TimerScript(float duration, float startValue) {
        this.duration = duration;
        value = startValue;
        started = false;
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
     * How long in seconds until the timer is ready.
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
     * How long the timer counts in seconds until it is ready.
     *
     * @return the count duration
     */
    public float getDuration() {
        return duration;
    }

    /**
     * Set the timer's count duration in seconds and reset its value.
     *
     * @param duration how long to count for
     */
    public void setDuration(float duration) {
        this.duration = duration;
        reset();
    }

    /**
     * Whether the timer is started and should count down, true by default.
     *
     * @return if the timer is paused
     */
    public boolean isStarted() {
        return started;
    }

    /**
     * Control whether the timer is should count down.
     *
     * @param started if the timer should count
     */
    public void setStarted(boolean started) {
        this.started = started;
    }

    @Override
    public String toString() {
        return String.format("Timer (%.4f/%.4f sec)", value, duration);
    }

}
