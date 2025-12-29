package slavsquatsuperstar.demos.spacegame.ui;

import mayonez.*;
import mayonez.graphics.*;
import mayonez.graphics.textures.*;
import mayonez.graphics.ui.*;
import mayonez.math.*;


/**
 * Displays the player's current health and shield.
 *
 * @author SlavSquatSuperstar
 */
public class PlayerStatus extends Script {

    // TODO create status bar row container
    private static final Texture HEALTH_ICON_TEXTURE = Textures.getTexture(
            "assets/spacegame/textures/ui/health_bar_icon.png");
    private static final Texture SHIELD_ICON_TEXTURE = Textures.getTexture(
            "assets/spacegame/textures/ui/shield_bar_icon.png");

    private static final Texture LABEL_BACKGROUND_TEXTURE = Textures.getTexture(
            "assets/spacegame/textures/ui/gray_background.png");
    private static final Texture LABEL_BORDER_TEXTURE = Textures.getTexture(
            "assets/spacegame/textures/ui/gray_border.png");

    private final Vec2 position, labelSize, sliderSize;
    private final float statusSpacing;
    private final int statusCols, statusRows;

    private SliderBar healthSlider, shieldSlider;

    public PlayerStatus(Vec2 position, Vec2 labelSize, Vec2 sliderSize, float statusSpacing) {
        this.position = position; // Position of first element center
        this.labelSize = labelSize;
        this.sliderSize = sliderSize;
        this.statusSpacing = statusSpacing;
        this.statusCols = 2;
        this.statusRows = 2;
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

        var statusElements = new UIElement[]{healthLabel, healthSlider, shieldLabel, shieldSlider};
        var rowHeights = new float[statusRows];

        // Divide into rows
        for (int row = 0; row < statusRows; row++) {
            UIElement lastElem = null; // Position and size of last element in row

            for (int col = 0; col < statusCols; col++) {
                var elem = statusElements[row * statusCols + col];
                gameObject.addComponent((Component) elem);

                // Set column positions within row
                float elemX;
                if (lastElem != null) {
                    elemX = lastElem.getPosition().x
                            + lastElem.getSize().x * 0.5f + statusSpacing + elem.getSize().x * 0.5f;
                } else {
                    elemX = position.x;
                }
                elem.setPosition(new Vec2(elemX, 0f));

                // Get row height
                if (rowHeights[row] < elem.getSize().y) {
                    rowHeights[row] = elem.getSize().y;
                }

                lastElem = elem;
            }
        }

        // Set row positions
        var lastRowY = position.y;
        for (int row = 0; row < statusRows; row++) {
            float rowY;
            if (row == 0) {
                rowY = lastRowY;
            } else {
                rowY = lastRowY
                        - rowHeights[row - 1] * 0.5f - statusSpacing - rowHeights[row] * 0.5f;
                lastRowY = rowY;
            }

            for (int col = 0; col < statusCols; col++) {
                var elem = statusElements[row * statusCols + col];
                elem.setPosition(new Vec2(elem.getPosition().x, rowY));
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
