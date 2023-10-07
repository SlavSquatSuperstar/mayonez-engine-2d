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
     * Automatically creates an AWT or GL sprite with a given filename based on the
     * current engine instance.
     *
     * @param filename the texture location
     * @return a sprite
     */
    public static Sprite createSprite(String filename) {
        return Mayonez.getUseGL()
                ? new GLSprite(Textures.getGLTexture(filename))
                : new JSprite(Textures.getJTexture(filename));
    }

    /**
     * Automatically creates an AWT or GL sprite with a given texture based on the
     * current engine instance.
     *
     * @param texture an existing texture
     * @return a sprite
     */
    public static Sprite createSprite(Texture texture) {
        if (texture instanceof GLTexture glTexture) return new GLSprite(glTexture);
        else if (texture instanceof JTexture jTexture) return new JSprite(jTexture);
        else return createSprite(Colors.WHITE);
    }

    /**
     * Automatically creates a AWT or GL sprite with a given color depending on the current engine instance.
     *
     * @param color the color
     * @return a sprite
     */
    public static Sprite createSprite(Color color) {
        return Mayonez.getUseGL() ? new GLSprite(color) : new JSprite(color);
    }

    // Sprite Sheet Methods

    /**
     * Automatically creates a AWT or GL sprite sheet depending on the current engine instance.
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
        if (Mayonez.getUseGL()) {
            return new GLSpriteSheet(
                    filename, new Vec2(spriteWidth, spriteHeight), numSprites, spacing
            );
        } else {
            return new JSpriteSheet(
                    filename, new Vec2(spriteWidth, spriteHeight), numSprites, spacing
            );
        }
    }

}
