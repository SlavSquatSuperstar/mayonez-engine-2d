package slavsquatsuperstar.demos.renderer;

import mayonez.*;
import mayonez.graphics.*;
import mayonez.graphics.font.*;
import mayonez.graphics.ui.*;
import mayonez.input.*;
import mayonez.math.*;

/**
 * An object for testing font rendering.
 *
 * @author SlavSquatSuperstar
 */
class FontTestObject extends GameObject {

    // See https://en.wikipedia.org/wiki/Pangram for more pangrams

    private static final String MESSAGE1 = """
            The quick brown
            fox jumps over
            the lazy dog.
            Pack my box
            with five dozen
            liquor jugs.
            """;

    private static final String MESSAGE2 = """
            (ABC)[DEF]
            {GHI}<JKL>
            \\MNO/"PQR"
            |STU|'VWX'
            1+2-3*4÷5
            Agpqxy
            ÁÄÅáäå
            """;

    private final Font font;

    public FontTestObject(String name, Font font) {
        super(name);
        this.font = font;
    }

    @Override
    protected void init() {
        // Scene font
        var fontSize = 5; // wu
        var lineSpacing = 1; // ln

        // UI font
        var uiFontSize = 32; // px
        var uiLineSpacing = 1; // ln

        TextLabel worldText;
        addComponent(worldText = new WorldTextLabel(
                MESSAGE2, new Vec2(-41, -10.5f),
                font, Colors.BLUE, fontSize, lineSpacing)
        );

        TextLabel uiText;
        addComponent(uiText = new UITextLabel(
                MESSAGE1, new Vec2(180, Preferences.getScreenHeight() - 145),
                font, Colors.RED, uiFontSize, uiLineSpacing
        ));
        uiText.setAnchor(Anchor.TOP_LEFT);

        addComponent(new Script() {
            private TextAlignment align = TextAlignment.LEFT;

            @Override
            protected void update(float dt) {
                // Toggle font alignment
                if (KeyInput.keyPressed("space")) {
                    switch (align) {
                        case LEFT -> align = TextAlignment.CENTER;
                        case CENTER -> align = TextAlignment.RIGHT;
                        case RIGHT -> align = TextAlignment.LEFT;
                    }

                    // Set alignment
                    worldText.setAlignment(align);
                    uiText.setAlignment(align);
                }
            }
        });
    }
}
