package slavsquatsuperstar.demos;

import mayonez.*;
import mayonez.assets.text.*;
import mayonez.graphics.*;
import mayonez.graphics.font.*;
import mayonez.graphics.sprites.*;
import mayonez.math.*;
import slavsquatsuperstar.demos.font.SceneTextObject;
import slavsquatsuperstar.demos.font.UITextObject;

public class FontTestScene extends Scene {

    public FontTestScene(String name) {
        super(name, 0, 0, 10);
    }

    @Override
    protected void init() {
        var json = new JSONFile("assets/fonts/font_pixel.json");
        var record = json.readJSON();
        var metadata = new FontMetadata(record);

        var text = new TextFile("assets/fonts/font_pixel_widths.txt");
        var widthsLines = text.readLines();
        var widthsStr = String.join("", widthsLines);

        var widths = new int[widthsStr.length()];
        for (int i = 0; i < widths.length; i++) {
            widths[i] = Integer.parseInt(widthsStr, i, i + 1, 10);
        }

        var spriteSheet = (GLSpriteSheet) Sprites.createSpriteSheet(
                "assets/fonts/font_pixel.png",
                metadata.glyphHeight(), metadata.glyphHeight(), metadata.numCharacters(), 0
        );

        // Text characteristics
        // TODO fix whitespace width
        var message = "ABCDEFGHIJKLM\nNOPQRSTUVWXYZ\nabcdefghijklm\nnopqrstuvwyxz";
        var font = new Font(spriteSheet, metadata, widths);


        // Scene font
        var fontSize = 6; // pt
        var lineSpacing = 2; // px
        addObject(new SceneTextObject(
                message, new Vec2(-30, 10), font,
                Colors.BLACK, fontSize, lineSpacing)
        );

        // UI font
        var uiFontSize = 30; // pt
        var uiLineSpacing = 2; // px
        addObject(new UITextObject(
                message, new Vec2(25, 775), font,
                Colors.RED, uiFontSize, uiLineSpacing)
        );
    }

}
