package mayonez.graphics.sprite;

import mayonez.Mayonez;
import mayonez.io.image.Texture;

/**
 * Stores multiple textures and creates multiple sprites from a larger image.
 *
 * @author SlavSquatSuperstar
 */
public abstract sealed class SpriteSheet permits JSpriteSheet, GLSpriteSheet {

    public abstract Sprite getSprite(int index);

    public abstract Texture getTexture(int index);

    public abstract int numSprites();

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
    public static SpriteSheet create(String filename, int spriteWidth, int spriteHeight, int numSprites, int spacing) {
        if (Mayonez.getUseGL()) {
            return new GLSpriteSheet(filename, spriteWidth, spriteHeight, numSprites, spacing);
        } else {
            return new JSpriteSheet(filename, spriteWidth, spriteHeight, numSprites, spacing);
        }
    }

}
