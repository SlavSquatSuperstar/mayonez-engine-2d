package slavsquatsuperstar.mayonez;

import slavsquatsuperstar.fileio.Assets;
import slavsquatsuperstar.fileio.Texture;
import slavsquatsuperstar.mayonez.renderer.Sprite;

import java.util.ArrayList;
import java.util.List;

/**
 * Creates multiple {@link Sprite}s from a larger image.
 *
 * @author SlavSquatSuperstar
 */
public class SpriteSheet {

    private List<Sprite> sprites; // store images in memory

    /**
     * Creates a spritesheet from the given image file.
     *
     * @param filename     the name of the texture file
     * @param spriteWidth  how wide each sprite is
     * @param spriteHeight how tall each sprite is
     * @param numSprites   how many sprites to create
     * @param spacing      the padding in between sprites
     */
    public SpriteSheet(String filename, int spriteWidth, int spriteHeight, int numSprites, int spacing) {
        sprites = new ArrayList<>();
        Texture texture = Assets.getAsset(filename, Texture.class);
        int width = texture.getImage().getWidth();
//        int height = sheet.getImage().getHeight();

        // Make sure there isn't extra space on the right/bottom
        // Assume spacing is less than tile size
        int imgX = 0;
        int imgY = 0;
        for (int count = 0; count < numSprites; count++) {
            sprites.add(new Sprite(texture.getImage().getSubimage(imgX, imgY, spriteWidth, spriteHeight)));
            imgX += spriteWidth + spacing;
            if (imgX >= width) {
                imgX = 0;
                imgY += spriteHeight + spacing;
            }
        }
    }

    public Sprite getSprite(int index) {
        return sprites.get(index).copy();
    }

    public int getNumSprites() {
        return sprites.size();
    }

}
