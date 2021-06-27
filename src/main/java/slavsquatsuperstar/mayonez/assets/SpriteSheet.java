package slavsquatsuperstar.mayonez.assets;

import slavsquatsuperstar.mayonez.components.Sprite;

import java.util.ArrayList;

public class SpriteSheet {

    public ArrayList<Sprite> sprites;
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
