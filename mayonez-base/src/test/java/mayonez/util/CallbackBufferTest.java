package mayonez.util;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for {@link mayonez.util.CallbackBufferTest} class.
 *
 * @author SlavSquatSuperstar
 */
class CallbackBufferTest {

    private List<Integer> nums;
    private CallbackBuffer callbackBuffer;
    private static final int SIZE = 10;

    @BeforeEach
    void setUpList() {
        nums = new ArrayList<>();
        callbackBuffer = new CallbackBuffer();
    }

    @Test
    void executeAllCallbacksSuccess() {
        for (int i = 0; i < SIZE; i++) {
            int num = i;
            callbackBuffer.add(() -> nums.add(num));
        }

        callbackBuffer.executeCallbacks();
        assertEquals(SIZE, nums.size());
        assertEquals(0, callbackBuffer.size());
    }

    @Test
    void executeSomeCallbacksSuccess() {
        int limit = 5;
        for (int i = 0; i < SIZE; i++) {
            int num = i;
            callbackBuffer.add(() -> nums.add(num));
        }

        callbackBuffer.executeCallbacks(limit);
        assertEquals(limit, nums.size());
        assertEquals(limit, callbackBuffer.size());
    }
}