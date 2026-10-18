package mayonez.util;

import java.util.*;
import java.util.function.*;

/**
 * An ordered, modifiable list that uses an add/remove queue to prevent
 * {@link java.util.ConcurrentModificationException}s if the list needs
 * to be modified and iterated through simultaneously. Note that the
 * default {@link java.util.List} methods are not overridden and the list
 * is modified with the {@code add/removeUnbuffered} and
 * {@code add/removeBuffered} methods. The {@link CallbackBuffer}
 * itself is also protected and may be appended to in a callback.
 *
 * @param <E> the type of element contained by this list
 * @author SlavSquatSuperstar
 */
public class BufferedList<E> extends ArrayList<E> {

    private final CallbackBuffer callbacks;

    public BufferedList() {
        super();
        callbacks = new CallbackBuffer();
    }

    public BufferedList(int initialCapacity) {
        super(initialCapacity);
        callbacks = new CallbackBuffer(initialCapacity);
    }

    // Add/Remove Methods

    /**
     * Add an element directly to the list now without putting it in the buffer
     * (same as {@link List#add}). This action should only be performed if the
     * list is not being iterated through.
     *
     * @param elem the element to add
     */
    public void addUnbuffered(E elem) {
        add(elem);
    }

    /**
     * Add an element to this list later and put it in the buffer.
     *
     * @param elem the element to add
     */
    public void addBuffered(E elem) {
        callbacks.offer(() -> add(elem));
    }

    /**
     * Add an element to this list later and put it in the buffer with
     * additional instructions.
     *
     * @param elem    the element to remove
     * @param andThen what to do after adding the element
     */
    public void addBuffered(E elem, Runnable andThen) {
        callbacks.offer(() -> {
            add(elem);
            andThen.run();
        });
    }

    /**
     * Remove an element directly from the list now without putting it in the buffer
     * (same as {@link List#remove}). This action should only be performed if the
     * list is not being iterated through.
     *
     * @param elem the element to remove
     */
    public void removeUnbuffered(E elem) {
        remove(elem);
    }

    /**
     * Remove an element from this list later and put it in the buffer.
     *
     * @param elem the element to remove
     */
    public void removeBuffered(E elem) {
        callbacks.offer(() -> remove(elem));
    }

    /**
     * Remove an element from this list later and put it in the buffer with
     * additional instructions.
     *
     * @param elem    the element to remove
     * @param andThen what to do after removing the element
     */
    public void removeBuffered(E elem, Runnable andThen) {
        callbacks.offer(() -> {
            remove(elem);
            andThen.run();
        });
    }

    /**
     * Add or remove all objects pending in the buffer until the buffer is empty.
     */
    public void processBuffer() {
        callbacks.executeCallbacks();
    }

    /**
     * Remove all objects from the list and clear the buffer.
     */
    @Override
    public void clear() {
        super.clear();
        callbacks.clear();
    }

}
