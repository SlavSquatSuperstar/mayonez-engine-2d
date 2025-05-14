package mayonez.graphics.font;

import mayonez.assets.*;

import java.util.*;

/**
 * The default bitmap fonts that come with the program.
 *
 * @author SlavSquatSuperstar
 */
public final class Fonts {

    public static final Font DEFAULT_FONT = Objects.requireNonNull(
            Assets.getAsset("assets/fonts/font_pixel.json", Font.class)
    );

    private Fonts() {
    }

}
