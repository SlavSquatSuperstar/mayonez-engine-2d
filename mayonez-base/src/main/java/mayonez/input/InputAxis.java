package mayonez.input;

/**
 * Stores two opposite inputs and produces a value based on which are activated.
 *
 * @author SlavSquatSuperstar
 */
@FunctionalInterface
public interface InputAxis {
    /**
     * Get the value of this axis, 1 if the positive input is active, -1 if the
     * negative input is active, and 0 if neither input is active.
     *
     * @return the axis value, 1, -1, or 0
     */
    int value();
}
