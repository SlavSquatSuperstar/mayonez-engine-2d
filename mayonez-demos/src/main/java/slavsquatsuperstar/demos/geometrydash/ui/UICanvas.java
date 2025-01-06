package slavsquatsuperstar.demos.geometrydash.ui;

import mayonez.*;
import mayonez.graphics.sprites.*;
import mayonez.math.*;
import slavsquatsuperstar.demos.geometrydash.ZIndex;

import java.util.*;

/**
 * A container for user interface elements.
 *
 * @author SlavSquatSuperstar
 */
public class UICanvas extends GameObject {

    // Constants
    private static final SpriteSheet BLOCK_ICONS = Sprites.createSpriteSheet(
            "assets/geometrydash/textures/blocks.png",
            42, 42, 12, 2
    );
    private static final int NUM_COLS = 6;
    private static final float BUTTON_SPACING = 1.5f;

    // Canvas Fields
    private final List<UIButton> elements;
    private PlaceBlock placeBlock;

    public UICanvas(String name, Transform transform) {
        super(name, transform);
        setZIndex(ZIndex.UI);
        elements = new ArrayList<>();
    }

    @Override
    protected void init() {
        addComponent(placeBlock = new PlaceBlock());
        for (var i = 0; i < BLOCK_ICONS.numSprites(); i++) {
            addButton(i);
        }
    }

    private void addButton(int i) {
        // 0 1 2 3  4 5
        // 6 7 8 9 10 11
        var x = BUTTON_SPACING * (i % NUM_COLS);
        var y = -BUTTON_SPACING * (int) ((float) i / NUM_COLS);
        addElement(new UIButton(
                "Button " + (i + 1),
                new Transform(new Vec2(x, y), 0f, new Vec2(1.25f)),
                BLOCK_ICONS.getTexture(i)
        ));
    }

    public void addElement(UIButton elem) {
        elem.transform.set(elem.transform.combine(this.transform));
        elements.add(elem.setContainer(this));
        getScene().addObject(elem);
    }

    public void onElementSelected(UIButton button) {
        elements.forEach(btn -> { // deselect other buttons
            if (!btn.equals(button)) btn.setSelected(false);
        });
        placeBlock.setCursorTexture(button.getIcon());
    }

    public void onElementDeselected(UIButton button) {
        placeBlock.setCursorTexture(null);
    }

}
