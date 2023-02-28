package mayonez.graphics.sprites;

import mayonez.annotations.*;
import mayonez.io.*;
import mayonez.io.image.*;
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

    /**
     * Creates a spritesheet from the given image file.
     *
     * @param filename   the name of the parent texture
     * @param spriteSize how wide and tall each sprite is
     * @param numSprites how many sprites to create
     * @param spacing    the padding in between sprites
     */
    GLSpriteSheet(String filename, Vec2 spriteSize, int numSprites, int spacing) {
        sheetTexture = Assets.getGLTexture(filename);
        sprites = new ArrayList<>();
        createSprites(spriteSize, numSprites, spacing);
    }

    @Override
    protected void createSprites(Vec2 spriteSize, int numSprites, int spacing) {
        var texSize = sheetTexture.getSize();

        // Read sprite sheet from top left, but read image from bottom left
        var imgPos = new Vec2(0, texSize.y - spriteSize.y);
        for (var i = 0; i < numSprites; i++) {
            createSprite(spriteSize, imgPos);
            moveToNextSprite(imgPos, spriteSize, spacing, texSize);
        }
    }

    private void createSprite(Vec2 spriteSize, Vec2 imgPos) {
        var texCoords = getSubImageSize(spriteSize, imgPos);
        sprites.add(new GLSprite(new GLTexture(sheetTexture, texCoords)));
    }

    private Vec2[] getSubImageSize(Vec2 spriteSize, Vec2 imgPos) {
        // Normalize image coordinates
        var texSize = sheetTexture.getSize();
        var imgMin = imgPos.div(texSize);
        var imgMax = imgPos.add(spriteSize).div(texSize);
        var imgSize = imgMax.sub(imgMin);

        return Rectangle.rectangleVertices(imgMin.add(imgSize.mul(0.5f)), imgSize, 0);
    }

    @Override
    public GLSprite getSprite(int index) {
        return sprites.get(index).copy();
    }

    @Override
    public GLTexture getTexture(int index) {
        var subSprite = sprites.get(index);
        if (subSprite == null) return null;
        else return new GLTexture(subSprite.getTexture(), subSprite.getTexCoords());
    }

    @Override
    public int numSprites() {
        return sprites.size();
    }
}
