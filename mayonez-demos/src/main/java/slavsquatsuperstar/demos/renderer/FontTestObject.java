package slavsquatsuperstar.demos.renderer;

import mayonez.*;
import mayonez.graphics.*;
import mayonez.graphics.font.*;
import mayonez.input.*;
import mayonez.math.*;

/**
 * An object for testing font rendering.
 *
 * @author SlavSquatSuperstar
 */
class FontTestObject extends GameObject {// wu
    public static final int WORLD_FONT_SIZE = 6;// px
    public static final int UI_FONT_SIZE = 40;

    // See https://en.wikipedia.org/wiki/Pangram for more pangrams

    private static final String UI_MESSAGE = """
            The quick brown
            fox jumps over
            the lazy dog.
            Pack my box
            with five dozen
            liquor jugs.
            """;

    private static final String WORLD_MESSAGE = """
            (ABC)[DEF]
            {GHI}<JKL>
            \\MNO/"PQR"
            |STU|'VWX'
            1+2-3*4÷5
            Agpqxy
            ÁÄÅáäå
            """;

    public FontTestObject(String name) {
        super(name);
    }

    @Override
    protected void init() {
        var worldText = new TextLabel(
                WORLD_MESSAGE, new Vec2(-41, -10.5f)
        );
        worldText.setInUI(false);
        worldText.setColor(Colors.BLUE);
        worldText.setFontSize(WORLD_FONT_SIZE);
        addComponent(worldText);
//        worldText.setAnchor(Anchor.TOP_LEFT);

        var uiText = new TextLabel(
                UI_MESSAGE,  new Vec2(180, Preferences.getScreenHeight() - 145)
        );
        uiText.setInUI(true);
        uiText.setColor(Colors.RED);
        uiText.setFontSize(UI_FONT_SIZE);
        addComponent(uiText);
//        uiText.setAnchor(Anchor.TOP_LEFT);

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
                } else if (KeyInput.keyPressed("m")) {
                    worldText.setPosition(worldText.getPosition().add(new Vec2(1, -1)));
                    uiText.setPosition(uiText.getPosition().add(new Vec2(5, -5)));
                } else if (KeyInput.keyPressed("l")) {
                    worldText.setLineSpacing(worldText.getLineSpacing() + 0.25f);
                    uiText.setLineSpacing(uiText.getLineSpacing() + 0.25f);
                } else if (KeyInput.keyPressed("k")) {
                    worldText.setFontSize(worldText.getFontSize() + 1);
                    uiText.setFontSize(uiText.getFontSize() + 1);
                }
            }
        });
    }
}
