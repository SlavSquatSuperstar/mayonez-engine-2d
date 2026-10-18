package mayonez.util;

import org.junit.jupiter.api.*;

import java.util.ConcurrentModificationException;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for {@link mayonez.util.BufferedList} class.
 *
 * @author SlavSquatSuperstar
 */
class BufferedListTest {

    private BufferedList<Integer> nums;
    private static final int SIZE = 10;

    @BeforeEach
    void setUpList() {
        nums = new BufferedList<>(SIZE * 2);
        for (int i = 0; i < SIZE; i++) {
            nums.addUnbuffered(i);
        }
    }

    @Test
    void addingBufferedWhileLoopingDoesNotThrow() {
        assertDoesNotThrow(() -> nums.forEach(n -> nums.addBuffered(n * 10)));
        nums.processBuffer();
        assertEquals(SIZE * 2, nums.size());
    }

    @Test
    void addingUnbufferedWhileLoopingThrows() {
        assertThrows(ConcurrentModificationException.class,
                () -> nums.forEach(n -> nums.addUnbuffered(n * 10)));
    }

    @Test
    void removingBufferedWhileLoopingDoesNotThrow() {
        assertDoesNotThrow(() -> nums.forEach(nums::removeBuffered));
        nums.processBuffer();
        assertEquals(0, nums.size());
    }

    @Test
    void removingUnbufferedWhileLoopingThrows() {
        assertThrows(ConcurrentModificationException.class,
                () -> nums.forEach(nums::removeUnbuffered));
    }

    @Test
    void clearingListAlsoClearsBuffer() {
        nums.forEach(n -> nums.addBuffered(n * 10));
        nums.clear();
        nums.processBuffer();
        assertEquals(0, nums.size());
    }

    @Test
    void modifyingCallbackBufferOnAddDoesNotThrow() {
        nums.forEach(n -> nums.addBuffered(n * 10, () -> nums.addBuffered(n * 100)));
        assertDoesNotThrow(() -> nums.processBuffer());
        assertEquals(SIZE * 3,  nums.size());
    }

    @Test
    void modifyingCallbackBufferOnRemoveDoesNotThrow() {
        nums.forEach(n -> nums.removeBuffered(n , () -> nums.removeBuffered(n)));
        assertDoesNotThrow(() -> nums.processBuffer());
        assertEquals(0,  nums.size());
    }

}