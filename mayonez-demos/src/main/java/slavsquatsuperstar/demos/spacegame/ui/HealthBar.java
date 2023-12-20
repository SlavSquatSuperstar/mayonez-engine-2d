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
    private UIBox redBox, greenBox;

    public HealthBar(Vec2 position, Vec2 size) {
        this.position = position;
        this.size = size;
    }

    @Override
    public void init() {
        gameObject.setZIndex(SpaceGameZIndex.UI);

        redBox = new UIBox(position, size, Colors.RED);
        gameObject.addComponent(redBox);

        greenBox = new UIBox(position, size, Colors.GREEN) {
            @Override
            public int getZIndex() {
                return super.getZIndex() + 1; // always display above red
            }
        };
        gameObject.addComponent(greenBox);
    }

    /**
     * Sets the fill value of this health bar, from left to right.
     *
     * @param healthPercent the percent health to display
     */
    public void setValue(float healthPercent) {
        // TODO UI set anchor
        var clamped = FloatMath.clamp(healthPercent, 0f, 1f);
        greenBox.setSize(size.mul(new Vec2(clamped, 1f)));
        greenBox.setPosition(position.sub(size.mul(new Vec2((1f - clamped) * 0.5f, 0f))));
    }

}
