package slavsquatsuperstar.mayonezgl.renderer;

import org.joml.Vector2f;
import slavsquatsuperstar.mayonez.fileio.Assets;

import java.util.ArrayList;
import java.util.List;

public class GLSpriteSheet {

    private final List<GLSprite> sprites;

    /**
     * Creates a spritesheet from the given texture.
     *
     * @param filename     the name of the parent texture
     * @param spriteWidth  how wide each sprite is
     * @param spriteHeight how tall each sprite is
     * @param numSprites   how many sprites to create
     * @param spacing      the padding in between sprites
     */
    public GLSpriteSheet(String filename, int spriteWidth, int spriteHeight, int numSprites, int spacing) {
        GLTexture texture = Assets.getAsset(filename, GLTexture.class);
        sprites = new ArrayList<>();

        int width = texture.getWidth();
        int height = texture.getHeight();

        // Read sprite sheet from top left, but read image  from bottom left
        // Image coordinates need to be normalized
        int imgX = 0;
        int imgY = width - spriteHeight;
        for (int i = 0; i < numSprites; i++) {
            float topY = (float) (imgY + spriteHeight) / height;
            float rightX = (float) (imgX + spriteWidth) / width;
            float leftX = (float) imgX / width;
            float bottomY = (float) imgY / height;

            Vector2f[] texCoords = new Vector2f[]{
                    new Vector2f(rightX, topY),
                    new Vector2f(rightX, bottomY),
                    new Vector2f(leftX, bottomY),
                    new Vector2f(leftX, topY)
            };
            sprites.add(new GLSprite(texture, texCoords));

            imgX += spriteWidth + spacing;
            if (imgX >= width) {
                imgX = 0;
                imgY -= spriteHeight + spacing;
            }
        }

    }

    public GLSprite getSprite(int index) {
        return sprites.get(index).copy();
    }

}
