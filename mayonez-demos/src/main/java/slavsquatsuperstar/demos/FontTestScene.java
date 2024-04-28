package slavsquatsuperstar.demos;

import mayonez.*;
import mayonez.assets.text.*;
import mayonez.graphics.*;
import mayonez.graphics.font.*;
import mayonez.graphics.sprites.*;
import mayonez.math.*;

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

        var font = new Font(spriteSheet, metadata, widths);

        var messages = new String[]{
                "ABCDEFGHIJKLM", "NOPQRSTUVWXYZ", "abcdefghijklm", "nopqrstuvwyxz"
        };
        var startPos = new Vec2(-30, 20);
        var offsetPos = new Vec2(0, -metadata.glyphHeight());

        // TODO combine scene scale, glyph scale, and glyph spacing
        for (int i = 0; i < messages.length; i++) {
            addObject(createTextObject(
                    font, messages[i],
                    new Transform(startPos.add(offsetPos.mul(i)), 0f, new Vec2(5, 5))
            ));
        }
    }

    private static GameObject createTextObject(Font font, String message, Transform xf) {
        return new GameObject("Text Object", xf) {
            @Override
            protected void init() {
                var metadata = font.getMetadata();
                var startPos = new Vec2();
                var offsetPos = new Vec2(metadata.glyphSpacing(), 0);

                for (int i = 0; i < message.length(); i++) {
                    var sprite = createTextSprite(font, message.charAt(i), startPos.add(offsetPos.mul(i)));
                    addComponent(sprite);
                }
            }
        };
    }

    private static Sprite createTextSprite(Font font, int charCode, Vec2 charPos) {
        var glyph = font.getGlyph(charCode);
        if (glyph == null) return null;

        var tex = glyph.getTexture();
        var sprite = Sprites.createSprite(tex);
        sprite.setColor(Colors.BLACK);
        sprite.setSpriteTransform(new Transform(charPos));
        return sprite;
    }

}
