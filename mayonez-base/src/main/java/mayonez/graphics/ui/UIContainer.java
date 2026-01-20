package mayonez.graphics.ui;

import java.util.*;

/**
 * A user interface element that stores and manages the position and size of other elements
 * according to a layout. Containers may store renderable components or other containers.
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

    // Component Methods

    @Override
    protected void start() {
        started = true;
        arrangeElements();
    }

    @Override
    protected void onDestroy() {
        clearElements();
        started = false;
    }

    @Override
    protected void onEnable() {
        elements.forEach(e -> e.setEnabled(true));
    }

    @Override
    protected void onDisable() {
        elements.forEach(e -> e.setEnabled(false));
        // TODO disabling twice due to component + element children
    }

    // Layout Methods

    /**
     * Rearrange all child element positions and sizes. This method will also be recursively called
     * for any child containers.
     */
    protected abstract void arrangeElements();

    // Container Methods

    /**
     * Gets the child elements in the order they were added.
     *
     * @return the elements
     */
    public List<UIElement> getElements() {
        return elements;
    }

    /**
     * Gets the child element at the specified index.
     *
     * @param index the element index
     * @return the element
     */
    public UIElement getElement(int index) {
        return elements.get(index);
    }

    /**
     * Gets the number of child elements in this container.
     *
     * @return the count
     */
    public int numElements() {
        return elements.size();
    }

    /**
     * Adds a UI element to this container and refreshes the layout. If this container has been
     * added to a {@link mayonez.GameObject}, then the element will also be added to that object
     * .
     *
     * @param element the element
     */
    public void addElement(UIElement element) {
        if (gameObject != null) {
            gameObject.addComponent(element);
        }
        elements.add(element);
        if (started) arrangeElements();
    }

    /**
     * Removes a UI element from this container and refreshes the layout.
     *
     * @param element the element
     */
    public void removeElement(UIElement element) {
        // TODO destroy element
        elements.remove(element);
        if (started) arrangeElements();
    }

    /**
     * Removes all UI elements from this container.
     */
    public void clearElements() {
        // TODO destroy elements
        elements.clear();
    }

}
