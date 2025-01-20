package mayonez.util;

import java.util.*;
import java.util.function.*;

/**
 * An ordered, modifiable list that uses an add/remove queue to prevent
 * {@link java.util.ConcurrentModificationException}s. Useful if the list needs
 * to be modified and iterated through simultaneously.
 *
 * @param <E> the type of element contained by this list
 * @author SlavSquatSuperstar
 */
public class BufferedList<E> {

    private final List<E> list;
    private final Queue<Runnable> buffer;

    public BufferedList() {
        list = new ArrayList<>();
        buffer = new LinkedList<>();
    }

    // Add/Remove Methods

    /**
     * Add an element directly to the list later without putting it in the buffer.
     * This action should only be performed is the list is not being iterated through.
     *
     * @param elem the element to add
     */
    public void addUnbuffered(E elem) {
        list.add(elem);
    }

    /**
     * Add an element to this list later and put it in the buffer.
     *
     * @param elem the element to add
     */
    public void add(E elem) {
        buffer.offer(() -> list.add(elem));
    }

    /**
     * Add an element to this list later and put it in the buffer with
     * additional instructions.
     *
     * @param elem    the element to remove
     * @param doLater what to do after adding the element
     */
    public void add(E elem, Runnable doLater) {
        buffer.offer(() -> {
            list.add(elem);
            doLater.run();
        });
    }

    /**
     * Remove an element directly from the list later without putting it in the buffer.
     * This action should only be performed is the list is not being iterated through.
     *
     * @param elem the element to add
     */
    public void removeUnbuffered(E elem) {
        list.remove(elem);
    }

    /**
     * Remove an element from this list later and put it in the buffer.
     *
     * @param elem the element to remove
     */
    public void remove(E elem) {
        buffer.offer(() -> list.remove(elem));
    }

    /**
     * Remove an element from this list later and put it in the buffer with
     * additional instructions.
     *
     * @param elem    the element to remove
     * @param doLater what to do after removing the element
     */
    public void remove(E elem, Runnable doLater) {
        buffer.offer(() -> {
            list.remove(elem);
            doLater.run();
        });
    }

    /**
     * Add or remove all objects pending in the buffer until the buffer is empty.
     */
    public void processBuffer() {
        while (!buffer.isEmpty()) {
            buffer.poll().run();
        }
    }

    /**
     * Remove all objects from the list and clear the buffer.
     */
    public void clear() {
        list.clear();
        buffer.clear();
    }

    // Collection Methods

    /**
     * Get an immutable list containing all the elements already in the list.
     * Does not include elements in the buffer.
     *
     * @return the list copy
     */
    public List<E> copy() {
        return List.copyOf(list);
    }

    /**
     * Find an element already in the list matching the given search criteria.
     * Does not search for elements in the buffer.
     *
     * @param predicate the search query
     * @return the element, or null if not present
     */
    public E find(Predicate<? super E> predicate) {
        return list.stream().filter(predicate).findFirst().orElse(null);
    }

    /**
     * Perform an action for each element already in the list. Does not affect
     * elements in the buffer.
     *
     * @param action the action to perform
     */
    public void forEach(Consumer<? super E> action) {
        list.forEach(action);
    }

    /**
     * Get the number of elements already in the list. Does not count elements
     * in the buffer.
     *
     * @return the size
     */
    public int size() {
        return list.size();
    }

}
