package mayonez.graphics.sprites;

import mayonez.*;
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

    @Override
    protected void createSprites(int numSprites, int spacing) {
        var sheetSize = sheetTexture.getSize();

        // Make sure there isn't extra space on the right/bottom
        // Assume spacing is less than tile size
        var imgPos = new Vec2();
        for (var count = 0; count < numSprites; count++) {
            createSprite(sheetTexture.getFilename(), imgPos, count);
            moveToNextSprite(imgPos, spacing, sheetSize);
        }
    }

    private void createSprite(String filename, Vec2 imgPos, int count) {
        var subimage = sheetTexture.getImage().getSubimage((int) imgPos.x, (int) imgPos.y,
                (int) spriteSize.x, (int) spriteSize.y);
        textures.add(new JTexture(String.format("%s (Sprite %s)", filename, count), subimage));
    }

    @Override
    protected void moveToNextSprite(Vec2 imgPos, int spacing, Vec2 sheetSize) {
        imgPos.x += spriteSize.x + spacing;
        if (imgPos.x >= sheetSize.x) { // next row
            imgPos.x = 0;
            imgPos.y += spriteSize.y + spacing;
        }
        Logger.log("pos = %s", imgPos);
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