package mayonez.graphics.ui;

import mayonez.math.*;

/**
 * Stores UI elements in a single row or column, with optional spacing. Elements may be of different sizes.
 *
 * @author SlavSquatSuperstar
 */
public class BoxContainer extends UIContainer {

    protected Vec2 position; // Position of first slot center
    protected int spacing;
    private boolean vertical;

    public BoxContainer(Vec2 position, int spacing, boolean vertical) {
        this.position = position;
        this.spacing = spacing;
        this.vertical = vertical;
    }

    // UI Element Methods

    @Override
    public Vec2 getPosition() {
        return position;
    }

    @Override
    public void setPosition(Vec2 position) {
        this.position = position;
        arrangeElements();
    }

    @Override
    public Vec2 getSize() {
        var elementWidths = elements.stream()
                .map(UIElement::getSize)
                .map(v -> v.x);
        var elementHeights = elements.stream()
                .map(UIElement::getSize)
                .map(v -> v.y);
        var totalSpacing = (numElements() - 1) * spacing;

        if (vertical) {
            var maxWidth = elementWidths
                    .max(Float::compare)
                    .orElse(0f);
            var totalHeight = elementHeights
                    .reduce(Float::sum)
                    .orElse(0f);
            return new Vec2(maxWidth, totalHeight + totalSpacing);
        } else {
            var maxHeight = elementHeights
                    .max(Float::compare)
                    .orElse(0f);
            var totalWidth = elementWidths
                    .reduce(Float::sum)
                    .orElse(0f)
                    + totalSpacing;
            return new Vec2(totalWidth + totalSpacing, maxHeight);
        }
    }

    @Override
    public void setSize(Vec2 size) {
        // Do nothing, set element sizes instead
    }

    // Layout Methods

    @Override
    protected void arrangeElements() {
        UIElement lastElem = null; // Position and size of last element

        for (int i = 0; i < numElements(); i++) {
            var elem = getElement(i);
            if (vertical) {
                float elemY;
                if (lastElem != null) {
                    elemY = lastElem.getPosition().y
                            - (lastElem.getSize().y * 0.5f + spacing + elem.getSize().y * 0.5f);
                    // Coords are right-handed but order is top to bottom
                } else {
                    elemY = position.y;
                }
                elem.setPosition(new Vec2(position.x, elemY));
            } else {
                float elemX;
                if (lastElem != null) {
                    elemX = lastElem.getPosition().x
                            + lastElem.getSize().x * 0.5f + spacing + elem.getSize().x * 0.5f;
                } else {
                    elemX = position.x;
                }
                elem.setPosition(new Vec2(elemX, position.y));
            }

            if (elem instanceof UIContainer container) {
                container.arrangeElements(); // Recursive arrange
            }
            lastElem = elem;
        }
    }

    // Container Methods

    /**
     * The minimum spacing between each child element.
     *
     * @return the spacing
     */
    public int getSpacing() {
        return spacing;
    }

    /**
     * Sets the spacing between each child element and readjusts all elements.
     *
     * @param spacing the spacing
     */
    public void setSpacing(int spacing) {
        this.spacing = spacing;
        arrangeElements();
    }

    /**
     * Whether the child elements are stacked horizontally or vertically.
     *
     * @return if vertical
     */
    public boolean isVertical() {
        return vertical;
    }

    /**
     * Sets the direction to stack the child elements and readjusts all elements.
     *
     * @param vertical if vertical
     */
    public void setVertical(boolean vertical) {
        this.vertical = vertical;
        arrangeElements();
    }

}
