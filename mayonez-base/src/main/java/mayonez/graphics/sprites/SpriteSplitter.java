package mayonez.graphics.sprites;

import mayonez.assets.image.*;

/**
 * A strategy for splitting sub-sprites from a sprite sheet.
 *
 * @author SlavSquatSuperstar
 */
public interface SpriteSplitter {

    /**
     * Get the regions of all the sub-sprites on this sprite sheet.
     *
     * @return the sprite regions
     */
    ImageRegion[] getSpriteRegions();

}
