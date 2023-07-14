package slavsquatsuperstar.demos.geometrydash.ui;

import mayonez.*;
import mayonez.graphics.sprites.*;
import mayonez.math.*;
import slavsquatsuperstar.demos.geometrydash.components.PlaceBlock;

import java.util.*;

/**
 * A container for user interface elements.
 *
 * @author SlavSquatSuperstar
 */
public class UICanvas extends GameObject {

    // Constants
    private static final SpriteSheet BUTTON_ICONS = Sprites.createSpriteSheet(
            "assets/textures/geometrydash/blocks.png",
            42, 42, 12, 2
    );
    private static final int NUM_ROWS = 2;
    private static final float BUTTON_SPACING = 1.5f;

    // Canvas Fields
    private final List<UIButton> elements;
    private PlaceBlock placeBlock;

    public UICanvas(String name, Transform transform) {
        super(name, transform);
        elements = new ArrayList<>();
    }

    @Override
    protected void init() {
        addComponent(placeBlock = new PlaceBlock());
        for (var i = 0; i < BUTTON_ICONS.numSprites(); i++) {
            addButton(i);
        }
    }

    private void addButton(int i) {
        // 1 3 ... 11
        // 2 4 ... 12
        var x = BUTTON_SPACING * (i / NUM_ROWS);
        var y = -BUTTON_SPACING * (i % NUM_ROWS);
        addElement(new UIButton(
                "Button " + (i + 1),
                new Transform(new Vec2(x, y), 0f, new Vec2(1.25f)),
                BUTTON_ICONS.getTexture(i)
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
