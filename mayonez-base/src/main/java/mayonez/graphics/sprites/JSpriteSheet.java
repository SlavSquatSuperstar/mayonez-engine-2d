package mayonez.graphics.sprites;

import mayonez.assets.image.*;
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
final class JSpriteSheet extends SpriteSheet {

    private final JTexture sheetTexture;
    private final Vec2 spriteSize;
    private final List<JTexture> textures;
    private final int numSprites, spacing;

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
        this.numSprites = numSprites;
        this.spacing = spacing;
        textures = createSprites();
    }

    // Create Sprite Methods

    private List<JTexture> createSprites() {
        List<JTexture> textures = new ArrayList<>();
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
        var spacing = new Vec2(this.spacing);
        var spriteMove = spriteSize.add(spacing);
        var shape = getSheetShape(spriteMove, spacing);
        var cols = (int) shape.x;
        var rows = (int) shape.y;

        // Read sprites from top left of sheet
        var regions = new ImageRegion[numSprites];
        for (var y = 0; y < rows; y++) {
            for (var x = 0; x < cols; x++) {
                var spriteIdx = y * cols + x;
                // Check index doesn't go out of bounds
                if (spriteIdx >= numSprites) return regions;

                var spriteOrigin = new Vec2(x, y).mul(spriteMove);
                regions[spriteIdx] = new ImageRegion(spriteOrigin, spriteSize);
            }
        }
        return regions;
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
    public JTexture getTexture(int index) {
        return textures.get(index);
    }

    @Override
    public String toString() {
        return "JSpriteSheet (%s)".formatted(sheetTexture);
    }

}