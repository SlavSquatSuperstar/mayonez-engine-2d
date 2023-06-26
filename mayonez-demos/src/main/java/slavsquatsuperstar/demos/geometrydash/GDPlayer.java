package slavsquatsuperstar.demos.geometrydash;

import mayonez.*;
import mayonez.graphics.camera.*;
import mayonez.graphics.sprites.*;
import mayonez.math.*;
import mayonez.physics.*;
import mayonez.physics.colliders.*;
import mayonez.scripts.*;
import mayonez.scripts.movement.*;

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

        getScene().getCamera().setKeepInScene(false).setMode(CameraMode.FREE);

        // Add player scripts
        float thrustForce = 10f;
        addComponent(new BoxCollider(new Vec2(1, 1)));
        addComponent(new Rigidbody(1f).setDrag(0.2f).setFixedRotation(true));
        addComponent(new KeyMovement(MoveMode.POSITION, thrustForce).setTopSpeed(thrustForce));
        addComponent(new KeepInScene(KeepInScene.Mode.STOP));
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
