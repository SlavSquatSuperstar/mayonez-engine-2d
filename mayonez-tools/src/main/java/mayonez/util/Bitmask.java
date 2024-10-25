package mayonez.util;

/**
 * A bitmask (boolean array) represented by a 32-bit integer. Each bit represents a
 * boolean value (1 for true, 0 for false). Bits are numbered 0-31, from right to left.
 *
 * @author SlavSquatSuperstar
 */
public class Bitmask {

    // Constants

    /**
     * A bitmask with all bits true, equal to -1.
     */
    public static final int ALL_TRUE = -1;

    /**
     * A bitmask with all bits false, equal to 0.
     */
    public static final int ALL_FALSE = 0;

    /**
     * The number of bits inside the bitmask, equal to 32.
     */
    public static final int NUM_BITS = Integer.SIZE;

    // Fields

    private int value;

    public Bitmask() {
        this(ALL_TRUE);
    }

    public Bitmask(int value) {
        this.value = value;
    }

    // Bitmask Getters/Setters

    /**
     * Get the value of the bit at the given index (0-31, right to left)
     *
     * @param index the bit index
     * @return if the bit is 1
     */
    public boolean getBit(int index) {
        return ((value >> index) & 1) == 1;
    }

    /**
     * Set the value of the bit at the given index (0-31, right to left)
     *
     * @param index    the bit index
     * @param bitValue the bit value
     */
    public void setBit(int index, boolean bitValue) {
        var bitmask = 1 << index;
        if (bitValue) {
            value |= bitmask;
        } else {
            value &= ~bitmask;
        }
    }

    // Value Getters/Setters

    /**
     * Get the integer value of the bitmask.
     *
     * @return the bitmask value
     */
    public int getValue() {
        return value;
    }

    /**
     * Set the entire bitmask value.
     *
     * @param value the bitmask value
     */
    public void setValue(int value) {
        this.value = value;
    }

    /**
     * Set all bits in the bitmask as true.
     */
    public void setAllTrue() {
        value = ALL_TRUE;
    }

    /**
     * Set all bits in the bitmask as false.
     */
    public void setAllFalse() {
        value = ALL_FALSE;
    }

    @Override
    public String toString() {
        return "Bitmask (%b)".formatted(value);
    }
}
