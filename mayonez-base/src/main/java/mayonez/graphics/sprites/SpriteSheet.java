package mayonez.graphics.sprites;

import mayonez.graphics.textures.*;
import mayonez.math.*;

/**
 * Stores multiple textures and creates multiple sprites from a larger image.
 * To instantiate a sprite sheet, use {@link Sprites#createSpriteSheet}.
 * <p>
 * Sprite sheets (also known as texture atlases) are often used to
 * store an animation or many frequently used textures in one file.
 * This increases batch performance by requiring fewer textures per draw call.
 *
 * @author SlavSquatSuperstar
 */
public abstract sealed class SpriteSheet permits JSpriteSheet, GLSpriteSheet {

    // Create Sprite Methods

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
     * @param imgOrigin the origin of the current sprite on the sprite sheet in pixels
     * @param spacing   the space between each sprite in pixels
     */
    protected abstract void moveToNextSprite(Vec2 imgOrigin, int spacing);

    // Sprite Sheet Getters

    /**
     * The dimensions of the sprite sheet texture in pixels.
     *
     * @return the sheet size
     */
    public abstract Vec2 getSheetSize();

    // Sprite/Texture Getters

    /**
     * Get the first image from this sprite sheet as a {@link mayonez.graphics.sprites.Sprite}.
     *
     * @param index the image index, from 0 to size()-1
     * @return the sprite
     */
    public abstract Sprite getSprite(int index);

    /**
     * Get the first image from this sprite sheet as a {@link mayonez.graphics.textures.Texture}.
     *
     * @param index the image index, from 0 to size()-1
     * @return the texture
     */
    public abstract Texture getTexture(int index);

    /**
     * Get the number of sprites/textures in this sprite sheet.
     *
     * @return the sheet size
     */
    public abstract int size();

    public Sprite[] getSprites() {
        var sprites = new Sprite[size()];
        for (var i = 0; i < sprites.length; i++) {
            sprites[i] = getSprite(i);
        }
        return sprites;
    }

    public Texture[] getTextures() {
        var textures = new Texture[size()];
        for (var i = 0; i < textures.length; i++) {
            textures[i] = getTexture(i);
        }
        return textures;
    }

}
