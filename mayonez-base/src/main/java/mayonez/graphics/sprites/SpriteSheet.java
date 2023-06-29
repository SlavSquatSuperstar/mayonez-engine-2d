package mayonez.graphics.sprites;

import mayonez.graphics.textures.*;
import mayonez.math.*;

/**
 * Stores multiple textures and creates multiple sprites from a larger image.
 *
 * @author SlavSquatSuperstar
 */
public abstract sealed class SpriteSheet permits JSpriteSheet, GLSpriteSheet {

    /**
     * Create individual sprites from the sprite sheet, reading from left to right
     * then top to bottom.
     *
     * @param numSprites the number of sprites on the sheet
     * @param spacing    the space between each sprite in pixels
     */
    protected abstract void createSprites(int numSprites, int spacing);

    /**
     * Move to the next sprite on the sprite sheet.
     *
     * @param imgPos    the position of the image on the sprite sheet in pixels
     * @param spacing   the space between each sprite in pixels
     * @param sheetSize the size of the sprite sheet texture in pixels
     */
    protected abstract void moveToNextSprite(Vec2 imgPos, int spacing, Vec2 sheetSize);

    // Sprite/Texture Getters

    public abstract Sprite getSprite(int index);

    public abstract Texture getTexture(int index);

    public abstract int numSprites();

    public Sprite[] toSpriteArray() {
        var sprites = new Sprite[numSprites()];
        for (var i = 0; i < sprites.length; i++) sprites[i] = getSprite(i);
        return sprites;
    }

}
