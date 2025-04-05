package mayonez.graphics.sprites;

import mayonez.assets.image.*;
import mayonez.graphics.textures.*;

/**
 * Stores multiple {@link mayonez.graphics.textures.Texture}s and creates
 * multiple sprites from a larger image using a
 * {@link mayonez.graphics.sprites.SpriteSplitter}. See the
 * {@link Sprites#createSpriteSheet} method for convenience constructors.
 * <p>
 * Sprite sheets (also known as texture atlases) are often used to
 * store an animation or many frequently used textures in one file, which
 * reduces the number of file reads required by the application.
 * Sprite sheets also increase batch performance by requiring fewer textures
 * per draw call.
 *
 * @author SlavSquatSuperstar
 */
public class SpriteSheet {

    private final Texture sheetTexture;
    private final SpriteSplitter splitter;
    private final Texture[] textures;
    private final int numSprites;

    /**
     * Create a sprite sheet from the given texture asset and sprite
     * splitting strategy.
     *
     * @param sheetTexture the texture
     * @param splitter     the splitter
     */
    public SpriteSheet(Texture sheetTexture, SpriteSplitter splitter) {
        this.sheetTexture = sheetTexture;
        this.splitter = splitter;
        textures = createSprites();
        numSprites = textures.length;
    }

    // Create Sprite Methods

    /**
     * Get all the sub-sprite textures using the provided sprite splitter.
     *
     * @return the sprite textures
     */
    protected Texture[] createSprites() {
        var regions = getSpriteRegions();
        Texture[] textures = new Texture[regions.length];
        for (int i = 0; i < textures.length; i++) {
            textures[i] = sheetTexture.getSubTexture(
                    regions[i], "Sprite " + i
            );
        }
        return textures;
    }

    /**
     * Get the regions of all the sub-sprites on this sprite sheet.
     *
     * @return the sprite regions
     */
    protected ImageRegion[] getSpriteRegions() {
        return splitter.getSpriteRegions();
    }

    /**
     * Get the parent texture for this sprite sheet, from which sub-images
     * are created.
     *
     * @return the sheet texture
     */
    protected Texture getSheetTexture() {
        return sheetTexture;
    }

    // Sprite/Texture Getters

    /**
     * Get the number of sprites/textures in this sprite sheet.
     *
     * @return the sprite count
     */
    public int numSprites() {
        return numSprites;
    }

    /**
     * Get the image at the given index from this sprite sheet as a
     * {@link mayonez.graphics.sprites.Sprite}.
     *
     * @param index the image index, from 0 to numSprites - 1
     * @return the sprite
     */
    public Sprite getSprite(int index) {
        return Sprites.createSprite(textures[index]);
    }

    /**
     * Get the image at the given index from this sprite sheet as a
     * {@link mayonez.graphics.textures.Texture}.
     *
     * @param index the image index, from 0 to numSprites - 1
     * @return the texture
     */
    public Texture getTexture(int index) {
        return textures[index];
    }

    /**
     * Get all the textures in this spritesheet as an array.
     * Note that writing to this array will affect the sprite sheet itself.
     *
     * @return the texture array
     */
    public Texture[] getTextures() {
        return textures;
    }

    @Override
    public String toString() {
        return "SpriteSheet (%s)".formatted(sheetTexture);
    }

}
