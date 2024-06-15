package slavsquatsuperstar.demos.font;

import mayonez.*;
import mayonez.assets.text.*;
import mayonez.graphics.*;
import mayonez.graphics.font.*;
import mayonez.graphics.textures.*;
import mayonez.input.*;
import mayonez.math.*;

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

        var message1 = """
                ABCDEFGHIJKLM
                NOPQRSTUVWXYZ
                abcdefghijklm
                nopqrstuvwxyz
                `1234567890-=
                ~!@#$%^&*()_+
                /|\\[]{}<>;',?:".
                """;

        var message2 = """
                (ABC)[DEF]
                {GHI}<JKL>
                1+2-3=0
                7*7=49
                ~m,n;w_
                ~M.N:W_
                """;

        // Scene font
        var fontSize = 6; // pt
        var lineSpacing = 2; // px

        // UI font
        var uiFontSize = 30; // pt
        var uiLineSpacing = 2; // px

        addObject(new GameObject("Text Holder") {
            @Override
            protected void init() {
                TextLabel worldText1;
                addComponent(worldText1 = new WorldTextLabel(
                        message1, new Vec2(-5, 30), font,
                        Colors.BLUE, fontSize, lineSpacing)
                );

                TextLabel worldText2;
                addComponent(worldText2 = new WorldTextLabel(
                        message2, new Vec2(-55, 5), font,
                        Colors.GREEN, fontSize, lineSpacing)
                );

                TextLabel uiText;
                addComponent(uiText = new UITextLabel(
                        message1, new Vec2(25, 775), font,
                        Colors.RED, uiFontSize, uiLineSpacing
                ));

                addComponent(new Script() {
                    @Override
                    protected void update(float dt) {
                        if (KeyInput.keyPressed("space")) {
                            textVisible = !textVisible;
                            worldText1.setEnabled(textVisible);
                            worldText2.setEnabled(textVisible);
                            uiText.setEnabled(textVisible);
                        }
                    }

                    @Override
                    protected void debugRender() {
                        getScene().getDebugDraw().drawPoint(new Vec2(-5, 30), Colors.GREEN);
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

    private Font createFont() {
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
