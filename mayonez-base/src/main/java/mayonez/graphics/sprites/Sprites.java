package mayonez.graphics.sprites;

import mayonez.*;
import mayonez.graphics.*;
import mayonez.graphics.textures.*;
import mayonez.math.*;

/**
 * A factory class that constructs {@link mayonez.graphics.sprites.Sprite} and
 * {@link mayonez.graphics.sprites.SpriteSheet} objects depending on the run
 * configuration.
 *
 * @author SlavSquatSuperstar
 */
public final class Sprites {

    private Sprites() {
    }

    // Sprite Methods

    /**
     * Creates a sprite from the given texture with color white.
     *
     * @param texture an existing texture
     * @return a sprite
     */
    public static Sprite createSprite(Texture texture) {
        if (texture instanceof GLTexture glTexture) {
            return new GLSprite(glTexture);
        } else if (texture instanceof JTexture jTexture) {
            return new JSprite(jTexture);
        } else {
            return createSprite(Colors.WHITE);
        }
    }

    /**
     * Creates a sprite from the given color with no texture.
     *
     * @param color the color
     * @return a sprite
     */
    public static Sprite createSprite(Color color) {
        return Mayonez.getUseGL() ? new GLSprite(color) : new JSprite(color);
    }

    // Sprite Sheet Methods

    /**
     * Creates a spritesheet from the given texture filename and tiling properties.
     *
     * @param filename     the name of the parent texture
     * @param spriteWidth  how wide each sprite is
     * @param spriteHeight how tall each sprite is
     * @param numSprites   how many sprites to create
     * @param spacing      the padding in between sprites
     * @return a sprite sheet
     */
    public static SpriteSheet createSpriteSheet(
            String filename, int spriteWidth, int spriteHeight, int numSprites, int spacing
    ) {
        var texture = Textures.getGLTexture(filename);
        return createSpriteSheet(
                texture, spriteWidth, spriteHeight, numSprites, spacing
        );
    }

    /**
     * Creates a spritesheet from a parent texture and tiling properties.
     *
     * @param texture      parent texture
     * @param spriteWidth  how wide each sprite is
     * @param spriteHeight how tall each sprite is
     * @param numSprites   how many sprites to create
     * @param spacing      the padding in between sprites
     * @return a sprite sheet
     */
    public static SpriteSheet createSpriteSheet(
            Texture texture, int spriteWidth, int spriteHeight, int numSprites, int spacing
    ) {
        var splitter = SpriteSplitters.getSpriteSplitter(
                texture, new Vec2(spriteWidth, spriteHeight),
                new Vec2(spacing), numSprites
        );
        return new SpriteSheet(texture, splitter);
    }

}
