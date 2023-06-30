package mayonez.graphics.sprites;

import mayonez.annotations.*;
import mayonez.graphics.textures.*;
import mayonez.io.*;
import mayonez.math.*;

import java.util.*;

/**
 * Creates multiple {@link JSprite} instances from a larger image.
 *
 * @author SlavSquatSuperstar
 */
@UsesEngine(EngineType.AWT)
public final class JSpriteSheet extends SpriteSheet {

    private final JTexture sheetTexture;
    private final List<JTexture> textures; // store images in memory
    private final Vec2 spriteSize;

    /**
     * Creates a sprite sheet from the given image file.
     *
     * @param filename   the name of the parent texture
     * @param spriteSize the dimensions of each sprite, in pixels
     * @param numSprites how many sprites to create
     * @param spacing    the padding in between sprites
     */
    JSpriteSheet(String filename, Vec2 spriteSize, int numSprites, int spacing) {
        sheetTexture = Assets.getJTexture(filename);
        textures = new ArrayList<>();
        this.spriteSize = spriteSize;
        createSprites(numSprites, spacing);
    }

    // Create Sprite Methods

    @Override
    protected void createSprites(int numSprites, int spacing) {
        // AWT uses top left as image origin
        var spriteTopLeft = new Vec2(0, 0);
//        Logger.log("pos = %s", spriteTopLeft);

        // Read sprites from top left of sheet
        for (var count = 0; count < numSprites; count++) {
            addCurrentSprite(sheetTexture.getFilename(), spriteTopLeft, count);
            moveToNextSprite(spriteTopLeft, spacing);
        }
    }

    private void addCurrentSprite(String filename, Vec2 spriteTopLeft, int count) {
        var subimage = sheetTexture.getImage().getSubimage(
                (int) spriteTopLeft.x, (int) spriteTopLeft.y,
                (int) spriteSize.x, (int) spriteSize.y
        );
        textures.add(new JTexture("%s (Sprite %s)".formatted(filename, count), subimage));
    }

    @Override
    protected void moveToNextSprite(Vec2 imgOrigin, int spacing) {
        var sheetSize = getSheetSize();

        // Origin at top left
        imgOrigin.x += spriteSize.x + spacing;
        if (imgOrigin.x >= sheetSize.x) {
            // If at end of row, go to next row
            imgOrigin.x = 0;
            imgOrigin.y += spriteSize.y + spacing;
        }
//        Logger.log("pos = %s", imgOrigin);
    }

    // Getters


    @Override
    protected Vec2 getSheetSize() {
        return sheetTexture.getSize();
    }

    @Override
    public Sprite getSprite(int index) {
        return new JSprite(getTexture(index));
    }

    @Override
    public JTexture getTexture(int index) {
        return textures.get(index);
    }

    @Override
    public int numSprites() {
        return textures.size();
    }

}