package slavsquatsuperstar.demos.geometrydash.ui;

import mayonez.*;
import mayonez.graphics.sprites.*;
import mayonez.graphics.textures.*;
import mayonez.math.*;
import mayonez.physics.colliders.*;
import mayonez.scripts.mouse.*;
import slavsquatsuperstar.demos.geometrydash.ZIndex;

/**
 * A user interface button that perform an action when clicked.
 *
 * @author SlavSquatSuperstar
 */
public class UIButton extends GameObject {

    // Constants
    private static final SpriteSheet BUTTON_TEXTURES;
    private static final int UNPRESSED_FRAME, PRESSED_FRAME;

    // Image Fields
    private final Animator baseSprite;
    private final Texture icon;

    // UI Fields
    private UICanvas container;
    private boolean selected;

    static {
        BUTTON_TEXTURES = Sprites.createSpriteSheet(
                "assets/textures/geometrydash/buttons.png",
                60, 60, 2, 2
        );
        UNPRESSED_FRAME = 0;
        PRESSED_FRAME = 1;
    }

    public UIButton(String name, Transform transform, Texture icon) {
        super(name, transform);
        setZIndex(ZIndex.UI);

        this.icon = icon;
        baseSprite = new Animator(BUTTON_TEXTURES, 0f);
        baseSprite.setTimerEnabled(false);
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
        baseSprite.setFrame(PRESSED_FRAME);
    }

    public void onDeselect() {
        container.onElementDeselected(this);
        baseSprite.setFrame(UNPRESSED_FRAME);
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
