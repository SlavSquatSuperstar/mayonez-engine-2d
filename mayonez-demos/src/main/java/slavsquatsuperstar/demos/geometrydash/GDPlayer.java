package slavsquatsuperstar.demos.geometrydash;

import mayonez.*;
import mayonez.graphics.*;
import mayonez.graphics.camera.*;
import mayonez.graphics.sprites.*;
import mayonez.math.*;
import mayonez.physics.*;
import mayonez.physics.colliders.*;
import mayonez.scripts.*;
import mayonez.scripts.movement.*;

/**
 * The Geometry Dash player object.
 *
 * @author SlavSquatSuperstar
 */
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
            Logger.printStackTrace(e);
            Mayonez.stop(ExitCode.ERROR);
        }

        getScene().getCamera().setKeepInScene(false).setMode(CameraMode.FREE);

        float thrustForce = 10f;
        addComponent(new BoxCollider(new Vec2(1, 1)));
        addComponent(new Rigidbody(1f).setDrag(0.2f).setFixedRotation(true));
        addComponent(new KeyMovement(MoveMode.POSITION, thrustForce).setTopSpeed(thrustForce));
        addComponent(new KeepInScene(KeepInScene.Mode.STOP));
    }

    private void createPlayerAvatar() throws ClassCastException {
        // Create player avatar
        var numLayers = 2;
        var tileSize = (int) getScene().getScale();
        var numSprites = 12 * 4;

        var spriteSheets = new SpriteSheet[numLayers];
        for (int i = 0; i < spriteSheets.length; i++) {
            spriteSheets[i] = SpritesFactory.createSpriteSheet(
                    "assets/textures/geometrydash/player/layer%d.png".formatted(i),
                    tileSize, tileSize, numSprites, 2);
        }

        var id = Random.randomInt(0, numSprites - 1);

        var layers = new Sprite[numLayers];
        for (int i = 0; i < layers.length; i++) {
            layers[i] = spriteSheets[i].getSprite(id);
        }
        Color[] colors = {new Color(255, 0, 0), new Color(0, 255, 0)};

        // Create sprite layers
        for (var i = 0; i < layers.length; i++) {
            layers[i].setColor(colors[i]);
            addComponent(layers[i]);
        }
    }

}
