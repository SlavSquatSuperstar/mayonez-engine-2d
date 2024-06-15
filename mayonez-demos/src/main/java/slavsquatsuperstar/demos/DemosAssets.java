package slavsquatsuperstar.demos;

import mayonez.assets.text.*;
import mayonez.graphics.font.*;
import mayonez.graphics.textures.*;

/**
 * Stores the shared resources used by the demo scenes.
 *
 * @author SlavSquatSuperstar
 */
public final class DemosAssets {

    private static Font font;

    private DemosAssets() {
    }

    public static Font getFont() {
        if (font == null) {
            font = createFont();
        }
        return font;
    }

    private static Font createFont() {
        // Font files
        var json = new JSONFile("assets/fonts/font_pixel.json");
        var record = json.readJSON();
        var metadata = new FontMetadata(record);

        // Create font
        var fontTexture = Textures.getTexture("assets/fonts/font_pixel.png");
        if (!(fontTexture instanceof GLTexture)) return null;
        return new Font((GLTexture) fontTexture, metadata);
    }

}
