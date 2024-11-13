package mayonez.util;

import org.junit.jupiter.api.*;

import static mayonez.util.Bitmask.*;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for {@link mayonez.util.Bitmask} class.
 *
 * @author SlavSquatSuperstar
 */
class BitmaskTest {

    private static final int ALT_BITS = 0xaaaaaaaa; // 0b1010…1010

    @Test
    void getBitsCorrect() {
        var mask = new Bitmask(ALT_BITS);
        for (int i = 0; i < NUM_BITS; i++) {
            if (i % 2 == 0) {
                assertFalse(mask.getBit(i)); // All even bits false
            } else {
                assertTrue(mask.getBit(i)); // All odd bits true
            }
        }
    }

    @Test
    void setBitsFromTrueCorrect() {
        var mask = new Bitmask(Bitmask.ALL_TRUE);
        for (int i = 0; i < NUM_BITS; i++) {
            // Even bits false, odd bits true
            mask.setBit(i, i % 2 != 0);
        }
        assertEquals(ALT_BITS, mask.getValue());
    }

    @Test
    void setBitsFromFalseCorrect() {
        var mask = new Bitmask(Bitmask.ALL_FALSE);
        for (int i = 0; i < NUM_BITS; i++) {
            // Even bits false, odd bits true
            mask.setBit(i, i % 2 != 0);
        }
        assertEquals(ALT_BITS, mask.getValue());
    }

    @Test
    void setAllBitsTrueCorrect() {
        var mask = new Bitmask(ALT_BITS);
        mask.setAllTrue();
        for (int i = 0; i < NUM_BITS; i++) {
            // All bits true
            assertTrue(mask.getBit(i));
        }
    }

    @Test
    void setAllBitsFalseCorrect() {
        var mask = new Bitmask(ALT_BITS);
        mask.setAllFalse();
        for (int i = 0; i < NUM_BITS; i++) {
            // All bits false
            assertFalse(mask.getBit(i));
        }
    }

}