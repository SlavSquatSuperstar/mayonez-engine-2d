package mayonez.graphics.sprites;

import mayonez.graphics.textures.*;
import mayonez.math.*;

/**
 * Stores multiple textures and creates multiple sprites from a larger image.
 * To instantiate a sprite sheet, use {@link Sprites#createSpriteSheet}.
 * <p>
 * Sprite sheets (also known as texture atlases) are often used to
 * store an animation or many frequently used textures in one file, which
 * reduces the number of file reads required by the application.
 * Sprite sheets also increase batch performance by requiring fewer textures
 * per draw call.
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
     * Get the parent texture for this sprite sheet, from which sub-images are created.
     *
     * @return the sheet texture
     */
    public abstract Texture getSheetTexture();

    /**
     * The dimensions of the sprite sheet texture in pixels.
     *
     * @return the sheet size
     */
    protected abstract Vec2 getSheetSize();

    /**
     * Get the number of sprites/textures in this sprite sheet.
     *
     * @return the sheet size
     */
    public abstract int numSprites();

    // Sprite/Texture Getters

    /**
     * Get the image at the given index from this sprite sheet as a {@link mayonez.graphics.sprites.Sprite}.
     *
     * @param index the image index, from 0 to numSprites - 1
     * @return the sprite
     */
    public Sprite getSprite(int index) {
        return Sprites.createSprite(getTexture(index));
    }

    /**
     * Get all the sprites in this spritesheet as an array.
     *
     * @return the sprite array
     */
    public Sprite[] getSprites() {
        var sprites = new Sprite[numSprites()];
        for (var i = 0; i < sprites.length; i++) {
            sprites[i] = getSprite(i);
        }
        return sprites;
    }

    /**
     * Get the image at the given index from this sprite sheet as a {@link mayonez.graphics.textures.Texture}.
     *
     * @param index the image index, from 0 to numSprites - 1
     * @return the texture
     */
    public abstract Texture getTexture(int index);

    /**
     * Get all the textures in this spritesheet as an array.
     *
     * @return the texture array
     */
    public Texture[] getTextures() {
        var textures = new Texture[numSprites()];
        for (var i = 0; i < textures.length; i++) {
            textures[i] = getTexture(i);
        }
        return textures;
    }

}
