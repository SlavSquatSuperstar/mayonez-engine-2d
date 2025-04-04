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
        // GL uses bottom left as origin
        var spriteStart = new Vec2(0, sheetTexture.getSize().y - spriteSize.y);
        var splitter = new TiledSpriteSplitter(
                sheetTexture.getSize(), spriteSize,
                new Vec2(spacing), numSprites,
                spriteStart, new Vec2(1, -1)
        );
        return splitter.getSpriteRegions();
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
