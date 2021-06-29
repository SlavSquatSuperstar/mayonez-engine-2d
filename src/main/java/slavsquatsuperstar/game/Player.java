package slavsquatsuperstar.game;

import slavsquatsuperstar.mayonez.GameObject;
import slavsquatsuperstar.mayonez.Preferences;
import slavsquatsuperstar.util.SpriteSheet;
import slavsquatsuperstar.mayonez.Vector2;
import slavsquatsuperstar.mayonez.components.Sprite;
import slavsquatsuperstar.mayonez.physics2d.AlignedBoxCollider2D;
import slavsquatsuperstar.mayonez.physics2d.RigidBody2D;

import java.awt.*;

public class Player extends GameObject {

    private final GameObject ground;

    public Player(String name, Vector2 position, GameObject ground) {
        super(name, position);
        this.ground = ground;
    }

    @Override
    protected void init() {
        // Create player avatar
        SpriteSheet layer1 = new SpriteSheet("player/layer1.png", 42, 42, 2, 13, 13 * 5);
        SpriteSheet layer2 = new SpriteSheet("player/layer2.png", 42, 42, 2, 13, 13 * 5);
        SpriteSheet layer3 = new SpriteSheet("player/layer3.png", 42, 42, 2, 13, 13 * 5);

        int id = 19;
        int threshold = 200;

        Sprite[] layers = new Sprite[]{layer1.getSprite(id), layer2.getSprite(id), layer3.getSprite(id)};
        Color[] colors = {Color.RED, Color.GREEN};

        // Create sprite layers
        for (int i = 0; i < colors.length; i++) {
            Sprite l = layers[i];
            for (int y = 0; y < l.getImage().getWidth(); y++) {
                for (int x = 0; x < l.getImage().getHeight(); x++) {
                    Color color = new Color(l.getImage().getRGB(x, y));
                    if (color.getRed() > threshold && color.getGreen() > threshold && color.getBlue() > threshold)
                        l.getImage().setRGB(x, y, colors[i].getRGB());
                }
            }
        }
        for (Sprite s : layers)
            addComponent(s);

        // Add player script
        addComponent(new AlignedBoxCollider2D(new Vector2(Preferences.TILE_SIZE, Preferences.TILE_SIZE)));
        addComponent(new RigidBody2D(0f));
        PlayerController pc = new PlayerController(ground);
        addComponent(pc);
    }

}
