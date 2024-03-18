package slavsquatsuperstar.demos.spacegame.ui;

import mayonez.*;
import mayonez.graphics.textures.*;
import mayonez.graphics.ui.*;
import mayonez.math.*;
import slavsquatsuperstar.demos.spacegame.combat.projectiles.ProjectilePrefabs;
import slavsquatsuperstar.demos.spacegame.objects.SpaceGameZIndex;

/**
 * Displays all available weapons and highlights the currently selected weapon.
 *
 * @author SlavSquatSuperstar
 */
public class WeaponSelectPanel extends Script {

    // Constants
    private static final Texture BACKGROUND_TEXTURE = Textures.getTexture(
            "assets/spacegame/textures/ui/hotbar_background.png");
    private static final Texture BORDER_TEXTURE =  Textures.getTexture(
            "assets/spacegame/textures/ui/hotbar_border.png");

    private static final float BACKGROUND_MARGIN = 5f;
    private static final float BORDER_MARGIN = 10f;

    // Fields
    private final Vec2 position, size;
    private final UIBox[] backgrounds;
    private UIBox border;

    public WeaponSelectPanel(Vec2 position, Vec2 size, int numWeapons) {
        this.position = position;
        this.size = size;
        backgrounds = new UIBox[numWeapons];
    }

    @Override
    public void init() {
        // TODO show currently active
        // TODO show fire cooldown
        gameObject.setZIndex(SpaceGameZIndex.UI);

        var boxSpacing = new Vec2(50, 0);

        for (int i = 0; i < backgrounds.length; i++) {
            var currPos = position.add(boxSpacing.mul(i));
            backgrounds[i] = new UIBox(currPos, size.add(new Vec2(BACKGROUND_MARGIN)), BACKGROUND_TEXTURE);
            gameObject.addComponent(backgrounds[i]);

            var icon = new UIBox(currPos, size, ProjectilePrefabs.PROJECTILE_SPRITES.getTexture(i)) {
                @Override
                public int getZIndex() {
                    return super.getZIndex() + 1; // display above background
                }
            };
            gameObject.addComponent(icon);
        }
        border = new UIBox(position, size.add(new Vec2(BORDER_MARGIN)), BORDER_TEXTURE) {
            @Override
            public int getZIndex() {
                return super.getZIndex() + 2; // display above icon
            }
        };
        gameObject.addComponent(border);

        setSelection(0);
    }

    /**
     * Sets the selected element of the weapon panel and display it as highlighted.
     *
     * @param index the index to select
     */
    public void setSelection(int index) {
        if (!IntMath.inRange(index, 0, backgrounds.length - 1)) return;
        // Move border
        border.setPosition(backgrounds[index].getPosition());
    }

}
