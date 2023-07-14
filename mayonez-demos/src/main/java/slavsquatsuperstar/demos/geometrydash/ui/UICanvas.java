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

    private final SpriteSheet buttonIcons;
    private final List<UIButton> elements;
    private PlaceBlock placeBlock;

    public UICanvas(String name, Transform transform, SpriteSheet buttonIcons) {
        super(name, transform);
        this.buttonIcons = buttonIcons;
        elements = new ArrayList<>();
    }

    @Override
    protected void init() {
        addComponent(placeBlock = new PlaceBlock());

        final var rows = 2;
        for (var i = 0; i < buttonIcons.numSprites(); i++) {
            // 1 3 ... 11
            // 2 4 ... 12
            var x = 1.5f * (i / rows);
            var y = -1.5f * (i % rows);
            addElement(new UIButton(
                    "Button " + (i + 1), new Transform(new Vec2(x, y), 0f, new Vec2(1.25f)), buttonIcons.getTexture(i)
            ));
        }
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
