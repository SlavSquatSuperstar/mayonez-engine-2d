package slavsquatsuperstar.demos.spacegame.ui;

import mayonez.*;
import mayonez.graphics.*;
import mayonez.graphics.ui.*;
import mayonez.math.*;
import slavsquatsuperstar.demos.spacegame.objects.SpaceGameZIndex;

/**
 * Displays the health of a game object.
 *
 * @author SlavSquatSuperstar
 */
public class HealthBar extends Script {

    private final Vec2 position, size;
    private UIBox foregroundBox;

    public HealthBar(Vec2 position, Vec2 size) {
        this.position = position;
        this.size = size;
    }

    @Override
    public void init() {
        gameObject.setZIndex(SpaceGameZIndex.UI);

        var borderWidth = 7.5f;
        var outlineBox = new UIBox(position, size.add(new Vec2(borderWidth)), Colors.GRAY);
        gameObject.addComponent(outlineBox);

        var backgroundBox = new UIBox(position, size, Colors.RED) {
            @Override
            public int getZIndex() {
                return super.getZIndex() + 2; // display above outline
            }
        };
        gameObject.addComponent(backgroundBox);

        foregroundBox = new UIBox(position, size, Colors.GREEN) {
            @Override
            public int getZIndex() {
                return super.getZIndex() + 2; // display above background
            }
        };
        gameObject.addComponent(foregroundBox);
    }

    /**
     * Sets the fill value of this health bar, from left to right.
     *
     * @param healthPercent the percent health to display
     */
    public void setValue(float healthPercent) {
        // TODO UI set anchor
        var clamped = FloatMath.clamp(healthPercent, 0f, 1f);
        foregroundBox.setSize(size.mul(new Vec2(clamped, 1f)));
        foregroundBox.setPosition(position.sub(size.mul(new Vec2((1f - clamped) * 0.5f, 0f))));
    }

}
