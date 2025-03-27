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
        var metadata = new FontMetadata(json.readJSON());

        // Create font
        var fontTexture = Textures.getTexture(metadata.fontFile());
        if (fontTexture instanceof GLTexture glTexture) {
            return new Font(metadata, glTexture);
        } else {
            return null;
        }
    }

}
