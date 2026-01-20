package slavsquatsuperstar.demos.spacegame.ui;

import mayonez.graphics.*;
import mayonez.graphics.textures.*;
import mayonez.graphics.ui.*;
import mayonez.math.*;


/**
 * Displays the player's current health and shield.
 *
 * @author SlavSquatSuperstar
 */
public class PlayerStatus extends BoxContainer {

    private static final Texture HEALTH_ICON_TEXTURE = Textures.getTexture(
            "assets/spacegame/textures/ui/health_bar_icon.png");
    private static final Texture SHIELD_ICON_TEXTURE = Textures.getTexture(
            "assets/spacegame/textures/ui/shield_bar_icon.png");

    private static final Texture LABEL_BACKGROUND_TEXTURE = Textures.getTexture(
            "assets/spacegame/textures/ui/gray_background.png");
    private static final Texture LABEL_BORDER_TEXTURE = Textures.getTexture(
            "assets/spacegame/textures/ui/gray_border.png");

    private final Vec2 labelSize, sliderSize;

    private SliderBar healthSlider, shieldSlider;

    public PlayerStatus(Vec2 position, Vec2 labelSize, Vec2 sliderSize, int spacing) {
        super(position, spacing, true);
        this.labelSize = labelSize;
        this.sliderSize = sliderSize;
    }

    @Override
    protected void init() {
        // Create elements
        var healthLabel = new ImageLabel(new Vec2(), labelSize,
                HEALTH_ICON_TEXTURE, LABEL_BACKGROUND_TEXTURE, LABEL_BORDER_TEXTURE);
        healthSlider = new SliderBar(new Vec2(), sliderSize,
                new Color(192, 0, 0), Colors.GREEN);
        var shieldLabel = new ImageLabel(new Vec2(), labelSize,
                SHIELD_ICON_TEXTURE, LABEL_BACKGROUND_TEXTURE, LABEL_BORDER_TEXTURE);
        shieldSlider = new SliderBar(new Vec2(), sliderSize,
                new Color(96, 96, 96), Colors.LIGHT_BLUE);

        // Store elements in a grid
        var statusRows = new UIElement[][]{
                {healthLabel, healthSlider}, {shieldLabel, shieldSlider}
        };
        for (var row : statusRows) {
            var rowContainer = new BoxContainer(position, spacing, false);
            this.addElement(rowContainer);
            for (var elem : row) {
                rowContainer.addElement(elem);
            }
        }
    }

    public SliderBar getHealthSlider() {
        return healthSlider;
    }

    public SliderBar getShieldSlider() {
        return shieldSlider;
    }
}
