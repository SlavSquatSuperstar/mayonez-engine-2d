package mayonez.graphics.sprites;

import mayonez.assets.image.*;
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
final class GLSpriteSheet extends SpriteSheet {

    private final GLTexture sheetTexture;
    private final Vec2 spriteSize;
    private final List<GLTexture> textures;
    private final int numSprites, spacing;

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
        this.numSprites = numSprites;
        this.spacing = spacing;
        textures = createSprites();
    }

    // Create Sprite Methods

    private List<GLTexture> createSprites() {
        List<GLTexture> textures = new ArrayList<>();
        var regions = getSpriteRegions();
        for (int i = 0; i < numSprites; i++) {
            textures.add(sheetTexture.getSubTexture(
                    regions[i], "Sprite " + i)
            );
        }
        return textures;
    }

    @Override
    protected ImageRegion[] getSpriteRegions() {
        var regions = new ImageRegion[numSprites];
        var spriteOrigin = getFirstSpriteOrigin();

        // Read sprites from top left of sheet
        for (var i = 0; i < numSprites; i++) {
            regions[i] = new ImageRegion(spriteOrigin, spriteSize);
            // Move to next sprite
            spriteOrigin = getNextSpriteOrigin(spriteOrigin);
        }
        return regions;
    }

    @Override
    protected Vec2 getFirstSpriteOrigin() {
        // GL uses bottom left as image origin
        return new Vec2(0, sheetTexture.getSize().y - spriteSize.y);
    }

    @Override
    protected Vec2 getNextSpriteOrigin(Vec2 spriteOrigin) {
        var nextSpriteOrigin = new Vec2(
                spriteOrigin.x + (spriteSize.x + spacing), spriteOrigin.y
        );
        if (nextSpriteOrigin.x >= sheetTexture.getSize().x) {
            // Go to next row
            nextSpriteOrigin.x = 0;
            nextSpriteOrigin.y -= spriteSize.y + spacing;
        }
        return nextSpriteOrigin;
    }

    // Sheet Getters

    @Override
    public Texture getSheetTexture() {
        return sheetTexture;
    }

    @Override
    public int numSprites() {
        return numSprites;
    }

    @Override
    public GLTexture getTexture(int index) {
        return textures.get(index);
    }

    @Override
    public String toString() {
        return "GLSpriteSheet (%s)".formatted(sheetTexture);
    }

}
