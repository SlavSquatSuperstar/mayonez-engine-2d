package slavsquatsuperstar.demos;

import mayonez.*;
import mayonez.assets.text.*;
import mayonez.graphics.*;
import mayonez.graphics.font.*;
import mayonez.graphics.textures.*;
import mayonez.input.*;
import mayonez.math.*;
import slavsquatsuperstar.demos.font.WorldTextLabel;
import slavsquatsuperstar.demos.font.TextLabel;
import slavsquatsuperstar.demos.font.UITextLabel;

/**
 * A scene for testing font rendering in the UI and in the game.
 *
 * @author SlavSquatSuperstar
 */
public class FontTestScene extends Scene {

    private static final boolean CAMERA_DEBUG_MODE = true;

    private boolean textVisible;

    public FontTestScene(String name) {
        super(name, 0, 0, 10);
    }

    @Override
    protected void init() {
        textVisible = true;

        var font = createFont();
        if (font == null) return;

        var message1 = "ABCDEFGHIJKLM\nNOPQRSTUVWXYZ\nabcdefghijklm\nnopqrstuvwyxz\n0123456789";
        var message2 = "D\u0000\u007f\ufffd\u200cDDDDD\nD D D D\nD  D  D|\nDDDDDD";

        // Scene font
        var fontSize = 6; // pt
        var lineSpacing = 2; // px

        // UI font
        var uiFontSize = 30; // pt
        var uiLineSpacing = 2; // px

        addObject(new GameObject("Text Holder") {
            @Override
            protected void init() {
                TextLabel textObj1;
                addComponent(textObj1 = new WorldTextLabel(
                        message1, new Vec2(-30, 0), font,
                        Colors.BLACK, fontSize, lineSpacing)
                );

                TextLabel textObj2;
                addComponent(textObj2 = new WorldTextLabel(
                        message2, new Vec2(15, 30), font,
                        Colors.BLUE, fontSize, 0
                ));

                TextLabel textObj3;
                addComponent(textObj3 = new UITextLabel(
                        message1, new Vec2(25, 775), font,
                        Colors.RED, uiFontSize, uiLineSpacing
                ));

                addComponent(new Script() {
                    @Override
                    protected void update(float dt) {
                        if (KeyInput.keyPressed("space")) {
                            textVisible = !textVisible;
                            textObj1.setEnabled(textVisible);
                            textObj2.setEnabled(textVisible);
                            textObj3.setEnabled(textVisible);
                        }
                    }
                });
            }
        });
    }

    @Override
    protected void onUserUpdate(float dt) {
        if (CAMERA_DEBUG_MODE) {
            var moveInput = new Vec2(KeyInput.getAxis("horizontal"), KeyInput.getAxis("vertical"));
            var translation = moveInput.unit().rotate(getCamera().getRotation()).div(getCamera().getZoom());

            getCamera().getTransform().move(translation);
            getCamera().rotate(KeyInput.getAxis("arrows horizontal"));
            getCamera().zoom(1 + 0.01f * KeyInput.getAxis("arrows vertical"));
        }
    }

//    @Override
//    protected void onUserRender() {
//        getDebugDraw().drawPoint(new Vec2(-30, 0), Colors.GREEN);
//        getDebugDraw().drawPoint(new Vec2(15, 30), Colors.GREEN);
//    }

    private Font createFont() {
        // Font files
        var json = new JSONFile("assets/fonts/font_pixel.json");
        var record = json.readJSON();
        var metadata = new FontMetadata(record);

        // Create font
        var fontTexture = Textures.getTexture("assets/fonts/font_pixel.png");
        if (!(fontTexture instanceof GLTexture)) return null;
        var font = new Font((GLTexture) fontTexture, metadata);

        // Widths file
//        var widthsFile = new TextFile("assets/fonts/font_pixel_widths.txt");
//        var widthsLines = widthsFile.readLines();
//        var widthsStr = String.join("", widthsLines);
//
//        var widths = new int[widthsStr.length()];
//        for (int i = 0; i < widths.length; i++) {
//            widths[i] = Integer.parseInt(widthsStr, i, i + 1, 10);
//        }
//
//        // Check widths
//        for (int i = 0; i < widths.length; i++) {
//            if (widths[i] == font.getGlyphs()[i].getWidth()) {
//                System.out.println("widths match for char " + (char) (metadata.startCharacter() + i));
//            } else {
//                System.err.println("widths do not match for char " + (char) (metadata.startCharacter() + i));
//            }
//        }

        return font;
    }

}
