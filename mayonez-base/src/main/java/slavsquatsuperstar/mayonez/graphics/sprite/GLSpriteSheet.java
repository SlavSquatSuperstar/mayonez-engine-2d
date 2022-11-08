package slavsquatsuperstar.mayonez.graphics.sprite;

import slavsquatsuperstar.mayonez.math.Vec2;
import slavsquatsuperstar.mayonez.annotations.EngineType;
import slavsquatsuperstar.mayonez.annotations.UsesEngine;
import slavsquatsuperstar.mayonez.io.Assets;
import slavsquatsuperstar.mayonez.io.GLTexture;
import slavsquatsuperstar.mayonez.physics.shapes.Rectangle;

import java.util.ArrayList;
import java.util.List;

/**
 * Creates multiple {@link GLSprite}s from a larger image.
 *
 * @author SlavSquatSuperstar
 */
@UsesEngine(EngineType.GL)
public final class GLSpriteSheet extends SpriteSheet {

    private final List<GLSprite> sprites;

    /**
     * Creates a spritesheet from the given image file.
     *
     * @param filename     the name of the parent texture
     * @param spriteWidth  how wide each sprite is
     * @param spriteHeight how tall each sprite is
     * @param numSprites   how many sprites to create
     * @param spacing      the padding in between sprites
     */
    GLSpriteSheet(String filename, int spriteWidth, int spriteHeight, int numSprites, int spacing) {
        GLTexture texture = Assets.getGLTexture(filename);
        sprites = new ArrayList<>();

        Vec2 texSize = new Vec2(texture.getWidth(), texture.getHeight());
        Vec2 sprSize = new Vec2(spriteWidth, spriteHeight);

        // Read sprite sheet from top left, but read image from bottom left
        Vec2 imgCoords = new Vec2(0, texSize.y - sprSize.y);
        for (int i = 0; i < numSprites; i++) {
            // Image coordinates need to be normalized
            Vec2 imgMin = imgCoords.div(texSize);
            Vec2 imgMax = imgCoords.add(sprSize).div(texSize);
            Vec2 imgSize = imgMax.sub(imgMin);

            Vec2[] texCoords = Rectangle.rectangleVertices(imgMin.add(imgSize.mul(0.5f)), imgSize, 0);
            sprites.add(new GLSprite(new GLTexture(texture, texCoords)));

            imgCoords.x += spriteWidth + spacing;
            if (imgCoords.x >= texSize.x) {
                imgCoords.x = 0;
                imgCoords.y -= spriteHeight + spacing;
            }
        }
    }

    @Override
    public GLSprite getSprite(int index) {
        return sprites.get(index).copy();
    }

    @Override
    public GLTexture getTexture(int index) {
        GLSprite subSprite = sprites.get(index);
        return new GLTexture(subSprite.getTexture(), subSprite.getTexCoords());
    }

    @Override
    public int numSprites() {
        return sprites.size();
    }
}
