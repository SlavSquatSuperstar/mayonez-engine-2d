package slavsquatsuperstar.demos.spacegame.ui;

import mayonez.*;
import mayonez.graphics.*;
import mayonez.graphics.ui.*;
import mayonez.math.*;
import slavsquatsuperstar.demos.spacegame.objects.SpaceGameZIndex;

/**
 * Displays all available weapons and highlights the currently selected weapon.
 *
 * @author SlavSquatSuperstar
 */
public class WeaponSelectPanel extends Script {

    // Constants
    private static final Color SELECTED_COLOR = Colors.DARK_GRAY;
    private static final Color UNSELECTED_COLOR = Colors.GRAY;
    private static final float SELECTED_BORDER_WIDTH = 7.5f;
    private static final float UNSELECTED_BORDER_WIDTH = 5f;

    // Fields
    private final Vec2 position, size;
    private final Color[] boxColors;
    private final UIBox[] borderBoxes;
    private int selectedIndex;

    public WeaponSelectPanel(Vec2 position, Vec2 size, Color... boxColors) {
        this.position = position;
        this.size = size;
        this.boxColors = boxColors;
        borderBoxes = new UIBox[boxColors.length];
    }

    @Override
    public void init() {
        // TODO show currently active
        // TODO show fire cooldown
        gameObject.setZIndex(SpaceGameZIndex.UI);

        var boxSpacing = new Vec2(50, 0);

        for (int i = 0; i < boxColors.length; i++) {
            var currPos = position.add(boxSpacing.mul(i));
            borderBoxes[i] = new UIBox(currPos, size.add(new Vec2(UNSELECTED_BORDER_WIDTH)), UNSELECTED_COLOR);
            gameObject.addComponent(borderBoxes[i]);

            var box = new UIBox(currPos, size, boxColors[i]) {
                @Override
                public int getZIndex() {
                    return super.getZIndex() + 1; // display above outline
                }
            };
            gameObject.addComponent(box);
        }

        setSelection(0);
    }

    /**
     * Sets the selected element of the weapon panel and display it as highlighted.
     *
     * @param index the index to select
     */
    public void setSelection(int index) {
        if (!IntMath.inRange(index, 0, borderBoxes.length)) return;

        // Unselect last
        borderBoxes[selectedIndex].setColor(UNSELECTED_COLOR);
        borderBoxes[selectedIndex].setSize(size.add(new Vec2(UNSELECTED_BORDER_WIDTH)));

        // Select current
        selectedIndex = index;
        borderBoxes[selectedIndex].setColor(SELECTED_COLOR);
        borderBoxes[selectedIndex].setSize(size.add(new Vec2(SELECTED_BORDER_WIDTH)));
    }

}
