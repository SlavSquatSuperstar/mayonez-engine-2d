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

    /**
     * Creates a spritesheet from the given image file.
     *
     * @param filename   the name of the parent texture
     * @param spriteSize how wide and tall each sprite is
     * @param numSprites how many sprites to create
     * @param spacing    the padding in between sprites
     */
    JSpriteSheet(String filename, Vec2 spriteSize, int numSprites, int spacing) {
        sheetTexture = Assets.getJTexture(filename);
        textures = new ArrayList<>();
        createSprites(spriteSize, numSprites, spacing);
    }

    @Override
    protected void createSprites(Vec2 spriteSize, int numSprites, int spacing) {
        var texSize = sheetTexture.getSize();

        // Make sure there isn't extra space on the right/bottom
        // Assume spacing is less than tile size
        var imgPos = new Vec2();
        for (var count = 0; count < numSprites; count++) {
            createSprite(sheetTexture.getFilename(), spriteSize, imgPos, count);
            moveToNextSprite(imgPos, spriteSize, spacing, texSize);
        }
    }

    private void createSprite(String filename, Vec2 spriteSize, Vec2 imgPos, int count) {
        var subimage = sheetTexture.getImage().getSubimage((int) imgPos.x, (int) imgPos.y,
                (int) spriteSize.x, (int) spriteSize.y);
        textures.add(new JTexture(String.format("%s (Sprite %s)", filename, count), subimage));
    }

    @Override
    public JSprite getSprite(int index) {
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