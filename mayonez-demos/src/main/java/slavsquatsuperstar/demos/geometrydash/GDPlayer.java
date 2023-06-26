package slavsquatsuperstar.demos.geometrydash;

import mayonez.*;
import mayonez.graphics.sprites.JSprite;
import mayonez.graphics.sprites.JSpriteSheet;
import mayonez.graphics.sprites.SpriteSheet;
import mayonez.math.Random;
import mayonez.math.Vec2;
import mayonez.physics.Rigidbody;
import mayonez.physics.colliders.BoxCollider;
import mayonez.scripts.KeepInScene;
import mayonez.scripts.movement.KeyMovement;
import mayonez.scripts.movement.MoveMode;
import slavsquatsuperstar.demos.geometrydash.components.GDPlayerController;

import java.awt.*;

public class GDPlayer extends GameObject {

    public GDPlayer(String name, Vec2 position) {
        super(name, position);
    }

    @Override
    protected void init() {
        try {
            createPlayerAvatar();
        } catch (ClassCastException e) {
            Logger.error("Geometry Dash scenes must be run with AWT engine");
            Mayonez.stop(ExitCode.ERROR);
        }

        // Add player scripts
        float thrustForce = 10f;
        addComponent(new BoxCollider(new Vec2(1, 1)));
        addComponent(new Rigidbody(1f).setFixedRotation(true));
        addComponent(new KeyMovement(MoveMode.POSITION, thrustForce).setTopSpeed(thrustForce));
        addComponent(new KeepInScene(KeepInScene.Mode.STOP));
        addComponent(new GDPlayerController(thrustForce));
    }

    private void createPlayerAvatar() throws ClassCastException {
        // Create player avatar
        var tileSize = (int) getScene().getScale();
        var layer1 = (JSpriteSheet) SpriteSheet.create("assets/textures/geometrydash/player/layer1.png",
                tileSize, tileSize, 13 * 5, 2);
        var layer2 = (JSpriteSheet) SpriteSheet.create("assets/textures/geometrydash/player/layer2.png",
                tileSize, tileSize, 13 * 5, 2);
        var layer3 = (JSpriteSheet) SpriteSheet.create("assets/textures/geometrydash/player/layer3.png",
                tileSize, tileSize, 13 * 5, 2);

        var id = Random.randomInt(18, 20);
        var threshold = 200;

        var layers = new JSprite[]{layer1.getSprite(id), layer2.getSprite(id), layer3.getSprite(id)};
        Color[] colors = {Color.RED, Color.GREEN};

        // Create sprite layers
        for (var i = 0; i < colors.length; i++) {
            var l = layers[i];
            for (var y = 0; y < l.getImage().getWidth(); y++) {
                for (var x = 0; x < l.getImage().getHeight(); x++) {
                    var color = new Color(l.getImage().getRGB(x, y));
                    if (color.getRed() > threshold && color.getGreen() > threshold && color.getBlue() > threshold)
                        l.getImage().setRGB(x, y, colors[i].getRGB());
                }
            }
        }
        for (var spr : layers) addComponent(spr);
    }

}
