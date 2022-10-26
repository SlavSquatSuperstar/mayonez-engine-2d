package slavsquatsuperstar.demos.geometrydash.ui;

import slavsquatsuperstar.math.Vec2;
import slavsquatsuperstar.mayonez.GameObject;
import slavsquatsuperstar.mayonez.Transform;
import slavsquatsuperstar.mayonez.annotations.ExperimentalFeature;
import slavsquatsuperstar.mayonez.graphics.sprites.JSpriteSheet;
import slavsquatsuperstar.mayonez.input.MouseInput;
import slavsquatsuperstar.mayonez.io.JTexture;
import slavsquatsuperstar.mayonez.physics.colliders.BoxCollider;
import slavsquatsuperstar.mayonez.scripts.MouseScript;

import java.awt.*;

@ExperimentalFeature
public class UIButton extends GameObject {

    private static final JSpriteSheet buttons;
    private static final JTexture offButton, onButton;
    private final JTexture icon;

    private UICanvas container;
    private boolean selected = false;

    static {
        buttons = new JSpriteSheet("assets/textures/buttons.png", 60, 60, 2, 2);
        offButton = buttons.getTexture(0);
        onButton = buttons.getTexture(1);
    }

//    public UIButton(JTexture icon) {
//        this.icon = icon;
//    }

    public UIButton(String name, Transform transform, JTexture icon) {
        super(name, transform);
        this.icon = icon;
        setZIndex(2);
    }

    @Override
    protected void init() {
        addComponent(new BoxCollider(new Vec2(1, 1)).setTrigger(true));
        addComponent(new MouseScript() {
            @Override
            protected Vec2 getRawInput() {
                return MouseInput.getPosition();
            }

            @Override
            public void onMouseDown() {
                selected = !selected;
                if (selected) onSelect();
                else onDeselect();
            }
        });
    }

//    @Override
//    public void start() {
//        parent.addComponent(new BoxCollider(new Vec2(1, 1)).setTrigger(true));
//        parent.addComponent(new MouseScript() {
//            @Override
//            protected Vec2 getRawInput() {
//                return MouseInput.getPosition();
//            }
//
//            @Override
//            public void onMouseDown() {
//                enabled = !enabled;
//            }
//        });
//        parent.start();
//    }

    @Override
//    public void render(Graphics2D g2) {
    public void onUserRender(Graphics2D g2) {
        offButton.draw(g2, this.transform, new Transform(), getScene().getScale());
        if (selected) onButton.draw(g2, this.transform, new Transform(), getScene().getScale());
        icon.draw(g2, this.transform, Transform.scaleInstance(new Vec2(0.8f)), getScene().getScale());
    }

    public void onSelect() {
        container.onElementSelected(this);
    }

    public void onDeselect() {
        container.onElementDeselected(this);
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
        return icon;
    }
}
