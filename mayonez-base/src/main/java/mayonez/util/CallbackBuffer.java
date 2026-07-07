package mayonez.util;

import java.util.ArrayDeque;

/**
 * A double-ended queue of {@link Runnable} callback functions that can executed later.
 *
 * @author SlavSquatSuperstar
 */
public class CallbackBuffer extends ArrayDeque<Runnable> {

    public CallbackBuffer() {
    }

    public CallbackBuffer(int numElements) {
        super(numElements);
    }

    /**
     * Execute all queued callbacks in the order they were added.
     */
    public void executeCallbacks() {
        while (!isEmpty()) poll().run();
    }

    /**
     * Execute up to the given limit of queued callbacks in the order they were added.
     *
     * @param limit the maximum number of callbacks to execute.
     */
    public void executeCallbacks(int limit) {
        for (int i = 0; i < limit; i++) {
            if (!isEmpty()) poll().run();
        }
    }

}
