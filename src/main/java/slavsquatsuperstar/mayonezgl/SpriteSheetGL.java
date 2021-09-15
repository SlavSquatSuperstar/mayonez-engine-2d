package slavsquatsuperstar.mayonezgl;

import org.joml.Vector2f;
import slavsquatsuperstar.fileio.Assets;
import slavsquatsuperstar.mayonezgl.renderer.SpriteGL;
import slavsquatsuperstar.mayonezgl.renderer.TextureGL;

import java.util.ArrayList;
import java.util.List;

public class SpriteSheetGL {

    private TextureGL texture;
    private List<SpriteGL> sprites;

    /**
     * Creates a spritesheet from the given texture.
     *
     * @param filename     the name of the parent texture
     * @param spriteWidth  how wide each sprite is
     * @param spriteHeight how tall each sprite is
     * @param numSprites   how many sprites to create
     * @param spacing      the padding in between sprites
     */
    public SpriteSheetGL(String filename, int spriteWidth, int spriteHeight, int numSprites, int spacing) {
        texture = Assets.getAsset(filename, TextureGL.class);
        sprites = new ArrayList<>();

        int width = texture.getWidth();
        int height = texture.getHeight();

        // Read sprite sheet starting from top left, but read image starting from bottom left
        // Image coordinates need to be normalized
        int x = 0;
        int y = width - spriteHeight;
        for (int i = 0; i < numSprites; i++) {
            float topY = (float) (y + spriteHeight) / height;
            float rightX = (float) (x + spriteWidth) / width;
            float leftX = (float) x / width;
            float bottomY = (float) y / height;

            Vector2f[] texCoords = new Vector2f[]{
                    new Vector2f(rightX, topY),
                    new Vector2f(rightX, bottomY),
                    new Vector2f(leftX, bottomY),
                    new Vector2f(leftX, topY)
            };
            sprites.add(new SpriteGL(texture, texCoords));

            x += spriteWidth + spacing;
            if (x >= width) {
                x = 0;
                y -= spriteHeight + spacing;
            }
        }

    }

    public SpriteGL getSprite(int index) {
        return sprites.get(index).copy();
    }

}
