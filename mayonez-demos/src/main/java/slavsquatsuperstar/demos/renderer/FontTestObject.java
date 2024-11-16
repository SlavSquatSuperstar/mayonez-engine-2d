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

    private static final String MESSAGE1 = """
            ABCDEFGHIJKLM
            NOPQRSTUVWXYZ
            abcdefghijklm
            nopqrstuvwxyz
            `1234567890-=
            ~!@#$%^&*()_+
            /|\\[]{}<>;',?:".
            """;

    private static final String MESSAGE_2 = """
            (ABC)[DEF]
            {GHI}<JKL>
            1+2-3=0
            7*7=49
            ~m,n;w_
            ~M.N:W_
            """;

    private final Font font;

    public FontTestObject(String name, Font font) {
        super(name);
        this.font = font;
    }

    @Override
    protected void init() {
        if (font == null) return;

        // Scene font
        var fontSize = 5; // pt
        var lineSpacing = 2; // px

        // UI font
        var uiFontSize = 32; // pt
        var uiLineSpacing = 2; // px

        TextLabel worldText1;
        addComponent(worldText1 = new WorldTextLabel(
                MESSAGE_2, new Vec2(-42, -10), font,
                Colors.BLUE, fontSize, lineSpacing)
        );

//        TextLabel worldText2;
//        addComponent(worldText2 = new WorldTextLabel(
//                message1, new Vec2(32, 15), font,
//                Colors.GREEN, fontSize, lineSpacing)
//        );

        TextLabel uiText;
        addComponent(uiText = new UITextLabel(
                MESSAGE1, new Vec2(165, 650), font,
                Colors.RED, uiFontSize, uiLineSpacing
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
                    worldText1.setAlignment(align);
//                    worldText2.setAlignment(align);
                    uiText.setAlignment(align);
                }
            }
        });
    }
}
