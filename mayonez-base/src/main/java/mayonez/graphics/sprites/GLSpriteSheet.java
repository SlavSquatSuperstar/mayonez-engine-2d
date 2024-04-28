package mayonez.graphics.sprites;

import mayonez.graphics.*;
import mayonez.graphics.textures.*;
import mayonez.math.*;
import mayonez.math.shapes.*;

import java.util.*;

/**
 * Creates multiple {@link GLSprite}s from a larger image.
 *
 * @author SlavSquatSuperstar
 */
@UsesEngine(EngineType.GL)
public final class GLSpriteSheet extends SpriteSheet {

    private final GLTexture sheetTexture;
    private final List<GLSprite> sprites;
    private final Vec2 spriteSize;

    /**
     * Creates a sprite sheet from the given image file.
     *
     * @param filename   the name of the parent texture
     * @param spriteSize the dimensions of each sprite, in pixels
     * @param numSprites how many sprites to create
     * @param spacing    the padding in between sprites
     */
    GLSpriteSheet(String filename, Vec2 spriteSize, int numSprites, int spacing) {
        sheetTexture = Textures.getGLTexture(filename);
        sprites = new ArrayList<>();
        this.spriteSize = spriteSize;
        createSprites(numSprites, spacing);
    }

    // Create Sprite Methods

    @Override
    protected void createSprites(int numSprites, int spacing) {
        // GL uses bottom left as image origin
        var sheetTopLeft = new Vec2(0, getSheetSize().y);
        var spriteTopLeft = sheetTopLeft.sub(new Vec2(0, spriteSize.y));

        // Read sprites from top left of sheet
        for (var i = 0; i < numSprites; i++) {
            addCurrentSprite(spriteTopLeft, i);
            moveToNextSprite(spriteTopLeft, spacing);
        }
    }

    private void addCurrentSprite(Vec2 spriteBottomLeft, int index) {
        var texCoords = getSubimageCoords(spriteBottomLeft);
        sprites.add(new GLSprite(new GLSpriteSheetTexture(sheetTexture, index, texCoords)));
    }

    private Vec2[] getSubimageCoords(Vec2 spriteBottomLeft) {
        // Normalize image coordinates to between 0-1
        var texSize = sheetTexture.getSize();
        var imgMin = spriteBottomLeft.div(texSize);
        var imgMax = spriteBottomLeft.add(spriteSize).div(texSize);
        var imgSize = imgMax.sub(imgMin);
        return Rectangle.rectangleVertices(imgMin.midpoint(imgMax), imgSize, 0);
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

    // Getters

    @Override
    public Vec2 getSheetSize() {
        return sheetTexture.getSize();
    }

    @Override
    public Sprite getSprite(int index) {
        return sprites.get(index).copy();
    }

    @Override
    public GLTexture getTexture(int index) {
        var subSprite = sprites.get(index);
        if (subSprite == null) return null;
        else if (subSprite.getTexture() == null) return null;
        else return new GLSpriteSheetTexture(subSprite.getTexture(), index, subSprite.getTexCoords());
    }

    @Override
    public int size() {
        return sprites.size();
    }

}
