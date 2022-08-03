package slavsquatsuperstar.demos.geometrydash;

import slavsquatsuperstar.demos.geometrydash.components.PlayerController;
import slavsquatsuperstar.math.MathUtils;
import slavsquatsuperstar.math.Vec2;
import slavsquatsuperstar.mayonez.GameObject;
import slavsquatsuperstar.mayonez.graphics.JSprite;
import slavsquatsuperstar.mayonez.graphics.JSpriteSheet;
import slavsquatsuperstar.mayonez.physics.Rigidbody;
import slavsquatsuperstar.mayonez.physics.colliders.BoxCollider;
import slavsquatsuperstar.mayonez.scripts.KeepInScene;
import slavsquatsuperstar.mayonez.scripts.KeyMovement;
import slavsquatsuperstar.mayonez.scripts.MoveMode;

import java.awt.*;

public class Player extends GameObject {

    public Player(String name, Vec2 position) {
        super(name, position);
    }

    @Override
    protected void init() {
        // Create player avatar
        int tileSize = (int) getScene().getCellSize();
        JSpriteSheet layer1 = new JSpriteSheet("assets/textures/player/layer1.png", tileSize, tileSize, 13 * 5, 2);
        JSpriteSheet layer2 = new JSpriteSheet("assets/textures/player/layer2.png", tileSize, tileSize, 13 * 5, 2);
        JSpriteSheet layer3 = new JSpriteSheet("assets/textures/player/layer3.png", tileSize, tileSize, 13 * 5, 2);

        int id = MathUtils.random(18, 20);
        int threshold = 200;

        JSprite[] layers = new JSprite[]{layer1.getSprite(id), layer2.getSprite(id), layer3.getSprite(id)};
        Color[] colors = {Color.RED, Color.GREEN};

        // Create sprite layers
        for (int i = 0; i < colors.length; i++) {
            JSprite l = layers[i];
            for (int y = 0; y < l.getImage().getWidth(); y++) {
                for (int x = 0; x < l.getImage().getHeight(); x++) {
                    Color color = new Color(l.getImage().getRGB(x, y));
                    if (color.getRed() > threshold && color.getGreen() > threshold && color.getBlue() > threshold)
                        l.getImage().setRGB(x, y, colors[i].getRGB());
                }
            }
        }
        for (JSprite s : layers) addComponent(s);

        // Add player scripts
        float thrustForce = 5f;
        addComponent(new BoxCollider(new Vec2(1, 1)));
        addComponent(new Rigidbody(1f).setFixedRotation(true));
        addComponent(new KeyMovement(MoveMode.POSITION, thrustForce).setTopSpeed(5f));
        addComponent(new KeepInScene(new Vec2(), getScene().getSize(), KeepInScene.Mode.STOP));
        addComponent(new PlayerController(thrustForce));
    }

}
