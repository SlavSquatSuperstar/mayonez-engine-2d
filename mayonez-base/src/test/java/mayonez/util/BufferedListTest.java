package mayonez.util;

import org.junit.jupiter.api.*;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for the {@link mayonez.util.BufferedList} class.
 *
 * @author SlavSquatSuperstar
 */
class BufferedListTest {

    @Test
    void addingWhileLoopingDoesNotThrow() {
        var list = new BufferedList<Integer>();
        for (int i = 0; i < 10; i++) {
            list.addUnbuffered(i);
        }

        assertDoesNotThrow(() -> list.forEach(n -> list.add(n * 10)));
        list.processBuffer();
        assertEquals(20, list.size());
    }

    @Test
    void removingWhileLoopingDoesNotThrow() {
        var list = new BufferedList<Integer>();
        for (int i = 0; i < 10; i++) {
            list.addUnbuffered(i);
        }

        assertDoesNotThrow(() -> list.forEach(list::remove));
        list.processBuffer();
        assertEquals(0, list.size());
    }

}