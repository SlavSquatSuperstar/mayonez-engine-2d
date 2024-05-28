package mayonez.graphics.sprites;

import mayonez.graphics.*;
import mayonez.graphics.textures.*;
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
    private final Vec2 spriteSize;
    private final List<JTexture> textures; // store images in memory

    /**
     * Creates a sprite sheet from the given texture.
     *
     * @param sheetTexture the parent texture
     * @param spriteSize   the dimensions of each sprite, in pixels
     * @param numSprites   how many sprites to create
     * @param spacing      the padding between sprites, in pixels
     */
    JSpriteSheet(JTexture sheetTexture, Vec2 spriteSize, int numSprites, int spacing) {
        this.sheetTexture = sheetTexture;
        this.spriteSize = spriteSize;
        textures = new ArrayList<>(numSprites);
        createSprites(numSprites, spacing);
    }

    // Create Sprite Methods

    @Override
    protected void createSprites(int numSprites, int spacing) {
        // AWT uses top left as image origin
        var spriteTopLeft = new Vec2(0, 0);

        // Read sprites from top left of sheet
        for (var i = 0; i < numSprites; i++) {
            // Add current sprite
            textures.add(new JSpriteSheetTexture(sheetTexture, i, spriteTopLeft, spriteSize));
            moveToNextSprite(spriteTopLeft, spacing);
        }
    }

    @Override
    protected void moveToNextSprite(Vec2 imgOrigin, int spacing) {
        // Origin at top left
        imgOrigin.x += spriteSize.x + spacing;
        if (imgOrigin.x >= getSheetSize().x) {
            // If at end of row, go to next row
            imgOrigin.x = 0;
            imgOrigin.y += spriteSize.y + spacing;
        }
    }

    // Sheet Getters

    @Override
    public Vec2 getSheetSize() {
        return sheetTexture.getSize();
    }

    @Override
    public int numSprites() {
        return textures.size();
    }

    @Override
    public Sprite getSprite(int index) {
        return new JSprite(getTexture(index));
    }

    @Override
    public JTexture getTexture(int index) {
        return textures.get(index);
    }

}