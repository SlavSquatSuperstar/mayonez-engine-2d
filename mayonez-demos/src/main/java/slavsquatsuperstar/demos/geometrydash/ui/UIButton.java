package slavsquatsuperstar.demos.geometrydash.ui;

import mayonez.GameObject;
import mayonez.Transform;
import mayonez.annotations.ExperimentalFeature;
import mayonez.graphics.sprites.Sprite;
import mayonez.graphics.sprites.SpriteSheet;
import mayonez.graphics.textures.JTexture;
import mayonez.graphics.textures.Texture;
import mayonez.math.Vec2;
import mayonez.physics.colliders.BoxCollider;
import mayonez.scripts.movement.MouseScript;

/**
 * A user interface button that perform an action when clicked.
 *
 * @author SlavSquatSuperstar
 */
@ExperimentalFeature
public class UIButton extends GameObject {

    private static final SpriteSheet buttons;
    private static final Texture offButton, onButton;

    private UICanvas container;
    private boolean selected = false;
    private final Texture icon;
    private Sprite onSprite;

    static {
        buttons = SpriteSheet.create("assets/textures/geometrydash/buttons.png", 60, 60, 2, 2);
        offButton = buttons.getTexture(0);
        onButton = buttons.getTexture(1);
    }

    public UIButton(String name, Transform transform, Texture icon) {
        super(name, transform);
        this.icon = icon;
        setZIndex(2);
    }

    @Override
    protected void init() {
        addComponent(new BoxCollider(new Vec2(1, 1)).setTrigger(true));
        addComponent(Sprite.create(offButton));
        addComponent(onSprite = Sprite.create(onButton).setEnabled(false));
        addComponent(Sprite.create(icon).setSpriteTransform(Transform.scaleInstance(new Vec2(0.8f)))); // no way to scale though
        addComponent(new MouseScript() {
            @Override
            public void onMouseDown() {
                selected = !selected;
                if (selected) onSelect();
                else onDeselect();
            }
        });
    }

    public void onSelect() {
        container.onElementSelected(this);
        onSprite.setEnabled(selected);
    }

    public void onDeselect() {
        container.onElementDeselected(this);
        onSprite.setEnabled(selected);
    }

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }

    public UIButton setContainer(UICanvas container) {
        this.container = container;
        return this;
    }

    public JTexture getIcon() {
        return (JTexture) icon;
    }
}
