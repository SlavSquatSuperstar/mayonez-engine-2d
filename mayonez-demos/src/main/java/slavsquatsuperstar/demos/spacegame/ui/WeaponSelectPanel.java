package slavsquatsuperstar.demos.spacegame.ui;

import mayonez.*;
import mayonez.event.*;
import mayonez.graphics.*;
import mayonez.graphics.textures.*;
import mayonez.graphics.ui.*;
import mayonez.math.*;
import slavsquatsuperstar.demos.spacegame.combat.projectiles.ProjectilePrefabs;
import slavsquatsuperstar.demos.spacegame.events.SpaceGameEvents;
import slavsquatsuperstar.demos.spacegame.events.WeaponCooldownUpdate;
import slavsquatsuperstar.demos.spacegame.objects.SpaceGameZIndex;

/**
 * Displays all available weapons and highlights the currently selected weapon.
 *
 * @author SlavSquatSuperstar
 */
public class WeaponSelectPanel extends Script implements EventListener<Event> {

    // Constants
    private static final Texture BACKGROUND_TEXTURE = Textures.getTexture(
            "assets/spacegame/textures/ui/hotbar_background.png");
    private static final Texture BORDER_TEXTURE = Textures.getTexture(
            "assets/spacegame/textures/ui/hotbar_border.png");

    private static final float BACKGROUND_MARGIN = 5f;
    private static final float BORDER_MARGIN = 10f;

    // Fields
    private final Vec2 position, size;
    private final UIBox[] backgrounds;
    private UIBox selectedBorder, rechargeOverlay;
    private float rechargePercent;

    public WeaponSelectPanel(Vec2 position, Vec2 size, int numWeapons) {
        this.position = position;
        this.size = size;
        backgrounds = new UIBox[numWeapons];
    }

    @Override
    public void init() {
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

        // Recharge overlay over selected hotbar element
        rechargeOverlay = new UIBox(position, size.add(new Vec2(BACKGROUND_MARGIN)), new Color(76, 76, 76, 180)) {
            @Override
            public int getZIndex() {
                return super.getZIndex() + 2; // display above icon
            }
        };
        gameObject.addComponent(rechargeOverlay);

        // Border over selected hotbar element
        selectedBorder = new UIBox(position, size.add(new Vec2(BORDER_MARGIN)), BORDER_TEXTURE) {
            @Override
            public int getZIndex() {
                return super.getZIndex() + 3; // display above overlay
            }
        };
        gameObject.addComponent(selectedBorder);



        setSelection(0);
    }

    @Override
    protected void start() {
        SpaceGameEvents.getPlayerEventSystem().subscribe(this);

        // TODO show fire cooldown
        gameObject.setZIndex(SpaceGameZIndex.UI);
        rechargePercent = 0f;
    }

    // UI Methods

    /**
     * Sets the selected element of the weapon panel and display it as highlighted.
     *
     * @param index the index to select
     */
    public void setSelection(int index) {
        if (!IntMath.inRange(index, 0, backgrounds.length - 1)) return;
        // Move border
        selectedBorder.setPosition(backgrounds[index].getPosition());
        rechargeOverlay.setPosition(backgrounds[index].getPosition());
    }

    /**
     * Sets the fill value of the weapon recharge overlay, from bottom to top.
     *
     * @param cooldownPercent the percent cooldown to display
     */
    public void setCooldownPercent(float cooldownPercent) {
        // Clamp percent between 0%-100%
        var clamped = FloatMath.clamp(cooldownPercent, 0f, 1f);
        // Anchor bottom
        rechargeOverlay.setSize(size.mul(new Vec2(1f, clamped)));
        // TODO not moving to correct hotbar slot
        rechargeOverlay.setPosition(position.sub(size.mul(new Vec2(0f, (1f - clamped) * 0.5f))));
    }

    // Script Callbacks

    @Override
    protected void onDestroy() {
        SpaceGameEvents.getPlayerEventSystem().unsubscribe(this);
    }

    @Override
    public void onEvent(Event event) {
        if (event instanceof WeaponCooldownUpdate u) {
            rechargePercent = u.getRechargePercent();
            System.out.println("Recharging: " + rechargePercent);
            setCooldownPercent(rechargePercent);
        }
    }
}
