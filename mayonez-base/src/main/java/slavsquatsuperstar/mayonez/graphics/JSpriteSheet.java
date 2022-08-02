package slavsquatsuperstar.mayonez.graphics;

import slavsquatsuperstar.mayonez.io.Assets;
import slavsquatsuperstar.mayonez.io.JTexture;
import slavsquatsuperstar.mayonez.annotations.EngineType;
import slavsquatsuperstar.mayonez.annotations.UsesEngine;

import java.util.ArrayList;
import java.util.List;

/**
 * Creates multiple {@link JSprite}s from a larger image.
 *
 * @author SlavSquatSuperstar
 */
@UsesEngine(EngineType.AWT)
public class JSpriteSheet extends SpriteSheet {

    private final List<JSprite> sprites; // store images in memory

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
        sprites = new ArrayList<>();
        JTexture texture = Assets.getJTexture(filename);
        int width = texture.getImage().getWidth();
//        int height = sheet.getImage().getHeight();

        // Make sure there isn't extra space on the right/bottom
        // Assume spacing is less than tile size
        int imgX = 0;
        int imgY = 0;
        for (int count = 0; count < numSprites; count++) {
            sprites.add(new JSprite(texture.getImage().getSubimage(imgX, imgY, spriteWidth, spriteHeight)));
            imgX += spriteWidth + spacing;
            if (imgX >= width) {
                imgX = 0;
                imgY += spriteHeight + spacing;
            }
        }
    }

    @Override
    public JSprite getSprite(int index) {
        return sprites.get(index).copy();
    }

}