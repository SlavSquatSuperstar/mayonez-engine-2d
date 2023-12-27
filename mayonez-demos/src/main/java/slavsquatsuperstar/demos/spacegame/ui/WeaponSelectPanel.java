package slavsquatsuperstar.demos.spacegame.ui;

import mayonez.*;
import mayonez.graphics.*;
import mayonez.graphics.textures.*;
import mayonez.graphics.ui.*;
import mayonez.math.*;
import slavsquatsuperstar.demos.spacegame.combat.ProjectileType;
import slavsquatsuperstar.demos.spacegame.objects.SpaceGameZIndex;

/**
 * Displays all available weapons and highlights the currently selected weapon.
 *
 * @author SlavSquatSuperstar
 */
public class WeaponSelectPanel extends Script {

    // Constants
    private static final Texture BACKGROUND_TEXTURE = Textures.getTexture(
            "assets/spacegame/textures/ui_background.png");
    private static final Color SELECTED_COLOR = Colors.GRAY;
    private static final Color UNSELECTED_COLOR = Colors.WHITE;
    private static final float SELECTED_MARGIN = 7.5f;
    private static final float UNSELECTED_MARGIN = 5f;

    // Fields
    private final Vec2 position, size;
    private final Color[] boxColors;
    private final UIBox[] backgrounds;
    private int selectedIndex;

    public WeaponSelectPanel(Vec2 position, Vec2 size, Color... boxColors) {
        this.position = position;
        this.size = size;
        this.boxColors = boxColors;
        backgrounds = new UIBox[boxColors.length];
    }

    @Override
    public void init() {
        // TODO show currently active
        // TODO show fire cooldown
        gameObject.setZIndex(SpaceGameZIndex.UI);

        var boxSpacing = new Vec2(50, 0);

        for (int i = 0; i < boxColors.length; i++) {
            var currPos = position.add(boxSpacing.mul(i));
            backgrounds[i] = new UIBox(currPos, size.add(new Vec2(UNSELECTED_MARGIN)), BACKGROUND_TEXTURE);
            gameObject.addComponent(backgrounds[i]);

            var box = new UIBox(currPos, size, ProjectileType.PROJECTILE_SPRITES.getTexture(i)) {
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
        if (!IntMath.inRange(index, 0, backgrounds.length)) return;

        // Unselect last
        backgrounds[selectedIndex].setColor(UNSELECTED_COLOR);
        backgrounds[selectedIndex].setSize(size.add(new Vec2(UNSELECTED_MARGIN)));

        // Select current
        selectedIndex = index;
        backgrounds[selectedIndex].setColor(SELECTED_COLOR);
        backgrounds[selectedIndex].setSize(size.add(new Vec2(SELECTED_MARGIN)));
    }

}
