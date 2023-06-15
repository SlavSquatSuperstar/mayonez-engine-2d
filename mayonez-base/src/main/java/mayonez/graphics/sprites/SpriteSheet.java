package mayonez.graphics.sprites;

import mayonez.*;
import mayonez.graphics.textures.*;
import mayonez.math.*;

/**
 * Stores multiple textures and creates multiple sprites from a larger image.
 *
 * @author SlavSquatSuperstar
 */
public abstract sealed class SpriteSheet permits JSpriteSheet, GLSpriteSheet {

    protected abstract void createSprites(Vec2 spriteSize, int numSprites, int spacing);

    protected final void moveToNextSprite(Vec2 imgPos, Vec2 spriteSize, int spacing, Vec2 texSize) {
        imgPos.x += spriteSize.x + spacing;
        if (imgPos.x >= texSize.x) { // next row
            imgPos.x = 0;
            imgPos.y += spriteSize.y + spacing;
        }
    }

    // Sprite/Texture Getters

    public abstract Sprite getSprite(int index);

    public abstract Texture getTexture(int index);

    public abstract int numSprites();

    public Sprite[] toSpriteArray() {
        var sprites = new Sprite[numSprites()];
        for (var i = 0; i < sprites.length; i++) sprites[i] = getSprite(i);
        return sprites;
    }

    // Factory Methods

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
        if (Mayonez.getUseGL())
            return new GLSpriteSheet(filename, new Vec2(spriteWidth, spriteHeight), numSprites, spacing);
        else return new JSpriteSheet(filename, new Vec2(spriteWidth, spriteHeight), numSprites, spacing);
    }

}
