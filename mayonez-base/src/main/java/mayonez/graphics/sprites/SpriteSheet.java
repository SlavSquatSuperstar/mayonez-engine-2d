package mayonez.graphics.sprites;

import mayonez.assets.image.*;
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

    // TODO add origin and move to method
    /**
     * Get the regions of all the sub-sprites on this sprite sheet, starting form
     * the top left and reading row by row.
     *
     * @return the sprite regions
     */
    protected abstract ImageRegion[] getSpriteRegions();

    /**
     * Get the number of sprites on this sprite sheet in each dimension.
     *
     * @param spriteMove the distances to the next sprite, in pixels
     * @param spacing    the spacing along each dimension, in pixels
     * @return the sheet shape
     */
    protected Vec2 getSheetShape(Vec2 spriteMove, Vec2 spacing) {
        var shape1 = getSheetTexture().getSize().div(spriteMove);
        var shape2 = (getSheetTexture().getSize().add(spacing)).div(spriteMove);

        // Check if extra spacing on right/bottom
        var cols = Math.max(shape1.x, shape2.x);
        var rows = Math.max(shape1.y, shape2.y);
        return new Vec2(cols, rows);
    }

    // Sprite Sheet Getters

    /**
     * Get the parent texture for this sprite sheet, from which sub-images are created.
     *
     * @return the sheet texture
     */
    public abstract Texture getSheetTexture();

    /**
     * Get the number of sprites/textures in this sprite sheet.
     *
     * @return the sprite count
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
