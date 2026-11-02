package mayonez.graphics.ui;

import mayonez.Node;
import org.jspecify.annotations.Nullable;

import java.util.*;

/**
 * A user interface element that stores and manages the position and size of
 * other {@link UIElement}s according to a layout. Containers may store
 * renderable components or other containers. All elements in a container are
 * also child nodes of that container. Adding or removing UI elements to a
 * container will refresh the container's layout.
 *
 * @author SlavSquatSuperstar
 */
public abstract class UIContainer extends UIElement {

    protected final List<UIElement> elements;
    private boolean started; // Don't arrange until all elements added
    // TODO dirty flag or child events instead

    public UIContainer() {
        elements = new ArrayList<>();
        started = false;
    }

    // Node Methods

    @Override
    protected void start() {
        started = true;
        arrangeElements();
    }

    @Override
    protected void onEnable(boolean enabled) {
        elements.forEach(e -> e.setEnabled(enabled));
    }

    @Override
    protected void onVisible(boolean visible) {
        elements.forEach(e -> e.setVisible(visible));
    }

    @Override
    protected void onDestroy() {
        elements.clear();
        started = false;
    }

    // Layout Methods

    /**
     * Rearrange all child element positions and sizes. This method will also be recursively called
     * for any child containers.
     */
    protected abstract void arrangeElements();

    // Container Methods

    /**
     * Get the child elements of this container in the order they were added.
     *
     * @return the elements
     */
    public List<UIElement> getElements() {
        return elements;
    }

    /**
     * Get the child element at the specified index.
     *
     * @param index the element index
     * @return the element
     */
    public UIElement getElement(int index) {
        return elements.get(index);
    }

    /**
     * Get the number of child elements in this container.
     *
     * @return the count
     */
    public int numElements() {
        return elements.size();
    }

    /**
     * Add a UI element from this container and refresh the layout.
     *
     * @param element the element
     */
    public void addElement(UIElement element) {
        if (element == null) return;
        super.addChild(element);
        element.setContainer(this);
        elements.add(element);
        if (started) arrangeElements();
    }

    /**
     * Remove a UI element from this container and refresh the layout.
     *
     * @param element the element
     */
    public void removeElement(UIElement element) {
        if (element == null) return;
        super.removeChild(element);
        // UIElement destroy sets container to null
        elements.remove(element);
        if (started) arrangeElements();
    }

    /**
     * Remove all UI elements from this container.
     */
    public void clearElements() {
        elements.forEach(this::removeChild);
        elements.clear();
    }

}
