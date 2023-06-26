package slavsquatsuperstar.demos.geometrydash.ui;

import mayonez.GameObject;
import mayonez.Transform;
import mayonez.annotations.ExperimentalFeature;
import mayonez.graphics.sprites.SpriteSheet;
import mayonez.math.Vec2;
import slavsquatsuperstar.demos.geometrydash.components.PlaceBlock;

import java.util.ArrayList;
import java.util.List;

/**
 * A container for user interface elements.
 *
 * @author SlavSquatSuperstar
 */
@ExperimentalFeature
public class UICanvas extends GameObject {

    private final List<UIButton> elements;
    private final SpriteSheet icons;
    private PlaceBlock placeBlock;

    public UICanvas(String name, Transform transform, SpriteSheet icons) {
        super(name, transform);
        elements = new ArrayList<>();
        this.icons = icons;
    }

    @Override
    protected void init() {
        addComponent(placeBlock = new PlaceBlock());

//        int cols = 6;
        int rows = 2;

        for (var i = 0; i < icons.numSprites(); i++) {
            // 1 3 5 7  9 11
            // 2 4 6 8 10 12
            var x = 1.5f * (i / rows);
            var y = -1.5f * (i % rows);
            addElement(new UIButton(
                    "Button " + (i + 1), new Transform(new Vec2(x, y), 0f, new Vec2(1.25f)), icons.getTexture(i)
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
        placeBlock.setCursor(button.getIcon());
    }

    public void onElementDeselected(UIButton button) {
        placeBlock.setCursor(null);
    }
}
