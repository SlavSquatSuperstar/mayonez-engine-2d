package slavsquatsuperstar.game;

import slavsquatsuperstar.mayonez.GameObject;
import slavsquatsuperstar.math.Vec2;
import slavsquatsuperstar.mayonez.physics2d.Rigidbody2D;
import slavsquatsuperstar.mayonez.physics2d.colliders.AlignedBoxCollider2D;
import slavsquatsuperstar.mayonez.renderer.Sprite;
import slavsquatsuperstar.mayonez.scripts.KeepInScene;
import slavsquatsuperstar.mayonez.scripts.KeyMovement;
import slavsquatsuperstar.mayonez.scripts.MoveMode;
import slavsquatsuperstar.util.SpriteSheet;

import java.awt.*;

public class Player extends GameObject {

    public Player(String name, Vec2 position) {
        super(name, position);
    }

    @Override
    protected void init() {
        // Create player avatar
        int tileSize = getScene().getCellSize();
        SpriteSheet layer1 = new SpriteSheet("player/layer1.png", tileSize, tileSize, 2, 13, 13 * 5);
        SpriteSheet layer2 = new SpriteSheet("player/layer2.png", tileSize, tileSize, 2, 13, 13 * 5);
        SpriteSheet layer3 = new SpriteSheet("player/layer3.png", tileSize, tileSize, 2, 13, 13 * 5);

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

        // Add player scripts
        float thrustForce = 6f;
        addComponent(new AlignedBoxCollider2D(new Vec2(1, 1)));
        addComponent(new Rigidbody2D(12f));
        addComponent(new KeyMovement(MoveMode.FORCE, thrustForce).setTopSpeed(5f));
        addComponent(new KeepInScene(0, 0, getScene().getWidth(), getScene().getHeight(), KeepInScene.Mode.STOP));
        addComponent(new PlayerController(thrustForce));
    }

}
