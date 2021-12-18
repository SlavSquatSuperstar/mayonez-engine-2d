package slavsquatsuperstar.mayonez;

import slavsquatsuperstar.mayonez.renderer.Sprite;

import java.util.ArrayList;
import java.util.List;

/**
 * Creates multiple {@link Sprite}s from a larger image.
 *
 * @author SlavSquatSuperstar
 */
public class SpriteSheet {

    public List<Sprite> sprites;
    public Sprite sheet;
    public int tileWidth, tileHeight, spacing;

    public SpriteSheet(String filename, int tileWidth, int tileHeight, int spacing, int columns, int size) {
        this.tileWidth = tileWidth;
        this.tileHeight = tileHeight;
        this.spacing = spacing;

        sprites = new ArrayList<>();
        sheet = new Sprite(filename);

        int row = 0;
        int count = 0;
        while (count < size) {
            for (int col = 0; col < columns; col++) {
                int imgX = (col * tileWidth) + (col * spacing);
                int imgY = (row * tileHeight) + (row * spacing);

                sprites.add(new Sprite(sheet.getImage().getSubimage(imgX, imgY, tileWidth, tileHeight)));
                count++;
                if (count > size - 1)
                    break;
            }
            row++;
        }
    }

    public Sprite getSprite(int index) {
        return sprites.get(index);
    }

}
