package mayonez.graphics.sprites;

import mayonez.graphics.*;
import mayonez.graphics.textures.*;
import mayonez.math.*;

import java.util.*;

/**
 * Creates multiple {@link GLSprite}s from a larger image.
 *
 * @author SlavSquatSuperstar
 */
@UsesEngine(EngineType.GL)
public final class GLSpriteSheet extends SpriteSheet {

    private final GLTexture sheetTexture;
    private final Vec2 spriteSize;
    private final List<GLTexture> textures;

    /**
     * Creates a sprite sheet from the given texture.
     *
     * @param sheetTexture the parent texture
     * @param spriteSize   the dimensions of each sprite, in pixels
     * @param numSprites   how many sprites to create
     * @param spacing      the padding between sprites, in pixels
     */
    GLSpriteSheet(GLTexture sheetTexture, Vec2 spriteSize, int numSprites, int spacing) {
        this.sheetTexture = sheetTexture;
        this.spriteSize = spriteSize;
        textures = new ArrayList<>(numSprites);
        createSprites(numSprites, spacing);
    }

    // Create Sprite Methods

    @Override
    protected void createSprites(int numSprites, int spacing) {
        // GL uses bottom left as image origin
        var sheetTopLeft = new Vec2(0, getSheetSize().y);
        var spriteBottomLeft = sheetTopLeft.sub(new Vec2(0, spriteSize.y));

        // Read sprites from top left of sheet
        for (var i = 0; i < numSprites; i++) {
            // Add current sprite
            textures.add(new GLSpriteSheetTexture(sheetTexture, i, spriteBottomLeft, spriteSize));
            moveToNextSprite(spriteBottomLeft, spacing);
        }
    }

    @Override
    protected void moveToNextSprite(Vec2 imgOrigin, int spacing) {
        // Origin at bottom left
        imgOrigin.x += spriteSize.x + spacing;
        if (imgOrigin.x >= getSheetSize().x) {
            // If at end of row, go to next row
            imgOrigin.x = 0;
            imgOrigin.y -= spriteSize.y + spacing;
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
        return new GLSprite(getTexture(index));
    }

    @Override
    public GLTexture getTexture(int index) {
        return textures.get(index);
    }

}
