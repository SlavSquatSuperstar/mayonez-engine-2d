package slavsquatsuperstar.demos.geometrydash.ui;

import mayonez.*;
import mayonez.graphics.*;
import mayonez.graphics.sprites.*;
import mayonez.graphics.textures.*;
import mayonez.io.*;
import mayonez.math.*;
import mayonez.physics.colliders.*;
import mayonez.scripts.mouse.*;
import mayonez.util.*;

/**
 * A user interface button that perform an action when clicked.
 *
 * @author SlavSquatSuperstar
 */
public class UIButton extends GameObject {

    // Constants
    private static final Texture BUTTON_BASE_TEXTURE;
    private static final Color UNPRESSED_COLOR, PRESSED_COLOR;

    // Image Fields
    private final Sprite baseSprite;
    private final Texture icon;

    // UI Fields
    private UICanvas container;
    private boolean selected;

    static {
        BUTTON_BASE_TEXTURE = Assets.getTexture("assets/textures/geometrydash/button_base.png");
        UNPRESSED_COLOR = Colors.WHITE;
        PRESSED_COLOR = new Color(111, 111, 111);
    }

    public UIButton(String name, Transform transform, Texture icon) {
        super(name, transform);
        setZIndex(2);

        this.icon = icon;
        baseSprite = Sprites.createSprite(BUTTON_BASE_TEXTURE);
    }

    @Override
    protected void init() {
        selected = false;
        addComponent(new BoxCollider(new Vec2(1, 1)).setTrigger(true));
        addComponent(baseSprite);
        addComponent(Sprites.createSprite(icon)
                .setSpriteTransform(Transform.scaleInstance(new Vec2(0.8f))));
        addComponent(new MouseInputScript() {
            @Override
            public void onMouseDown() {
                setSelected(!selected);
            }
        });
    }

    public void onSelect() {
        container.onElementSelected(this);
        baseSprite.setColor(PRESSED_COLOR);
    }

    public void onDeselect() {
        container.onElementDeselected(this);
        baseSprite.setColor(UNPRESSED_COLOR);
    }

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
        if (selected) onSelect();
        else onDeselect();
    }

    public UIButton setContainer(UICanvas container) {
        this.container = container;
        return this;
    }

    public Texture getIcon() {
        return icon;
    }

}
