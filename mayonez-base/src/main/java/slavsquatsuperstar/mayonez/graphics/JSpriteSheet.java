package slavsquatsuperstar.mayonez.graphics;

import slavsquatsuperstar.mayonez.annotations.EngineType;
import slavsquatsuperstar.mayonez.annotations.UsesEngine;
import slavsquatsuperstar.mayonez.io.Assets;
import slavsquatsuperstar.mayonez.io.JTexture;

import java.util.ArrayList;
import java.util.List;

/**
 * Creates multiple {@link JSprite}s from a larger image.
 *
 * @author SlavSquatSuperstar
 */
@UsesEngine(EngineType.AWT)
public class JSpriteSheet extends SpriteSheet {

    private final List<JTexture> textures; // store images in memory

    /**
     * Creates a spritesheet from the given image file.
     *
     * @param filename     the name of the texture file
     * @param spriteWidth  how wide each sprite is
     * @param spriteHeight how tall each sprite is
     * @param numSprites   how many sprites to create
     * @param spacing      the padding in between sprites
     */
    public JSpriteSheet(String filename, int spriteWidth, int spriteHeight, int numSprites, int spacing) {
        textures = new ArrayList<>();
        JTexture texture = Assets.getJTexture(filename);
        int width = texture.getImage().getWidth();

        // Make sure there isn't extra space on the right/bottom
        // Assume spacing is less than tile size
        int imgX = 0;
        int imgY = 0;
        for (int count = 0; count < numSprites; count++) {
            textures.add(new JTexture(String.format("%s (Sprite %s)", filename, count), texture.getImage().getSubimage(imgX, imgY, spriteWidth, spriteHeight)));
            imgX += spriteWidth + spacing;
            if (imgX >= width) {
                imgX = 0;
                imgY += spriteHeight + spacing;
            }
        }
    }

    @Override
    public JSprite getSprite(int index) {
        return new JSprite(getTexture(index));
    }

    //    @Override
    public JTexture getTexture(int index) {
        return textures.get(index);
    }

    @Override
    public int numSprites() {
        return textures.size();
    }

}