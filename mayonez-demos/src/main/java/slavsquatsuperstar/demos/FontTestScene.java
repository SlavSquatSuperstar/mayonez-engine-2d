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
        var fontSize = 6; // pt
        var lineSpacing = 2; // px
        var startPos = new Vec2(-30, 20);
//        var lineOffsetPos = new Vec2(0, (float) fontSize + (float) lineSpacing / fontSize);
        var lineOffsetPos = new Vec2(0, (float) fontSize * (1 + (float) lineSpacing / metadata.glyphHeight()));

        // TODO convert to screen units
        for (int i = 0; i < messages.length; i++) {
            addObject(createTextObject(
                    messages[i], font, Colors.BLACK,
                    startPos.sub(lineOffsetPos.mul(i)), fontSize
            ));
        }
    }

    private static GameObject createTextObject(String message, Font font, Color color, Vec2 position, int fontSize) {
        return new GameObject("Text Object", position) {
            @Override
            protected void init() {
                var metadata = font.getMetadata();

                var lastCharPos = new Vec2();
                for (int i = 0; i < message.length(); i++) {
                    var charCode = message.charAt(i);
                    var glyph = font.getGlyph(charCode);
                    if (glyph == null) continue;

                    var sprite = createTextSprite(glyph, color, fontSize, lastCharPos);
                    addComponent(sprite);

                    var worldWidth = (float) fontSize * (glyph.getWidth() + metadata.glyphSpacing()) / (float) glyph.getHeight();
                    lastCharPos = lastCharPos.add(new Vec2(worldWidth, 0));
                }
            }
        };
    }

    private static Sprite createTextSprite(Glyph glyph, Color color, int fontSize, Vec2 charPos) {
        var tex = glyph.getTexture();
        var sprite = Sprites.createSprite(tex);
        sprite.setColor(color);
        sprite.setSpriteTransform(new Transform(charPos, 0f, new Vec2(fontSize)));
        return sprite;
    }

}
