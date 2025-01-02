package slavsquatsuperstar.demos.geometrydash;

import mayonez.*;
import mayonez.graphics.*;
import mayonez.graphics.sprites.*;
import mayonez.math.*;
import mayonez.physics.colliders.*;
import mayonez.physics.dynamics.*;
import mayonez.scripts.*;
import mayonez.scripts.movement.*;

/**
 * The Geometry Dash player character.
 *
 * @author SlavSquatSuperstar
 */
public class GDPlayer extends GameObject {

    private static final int NUM_SPRITES = 12 * 4;

    public GDPlayer(String name, Vec2 position) {
        super(name, position);
    }

    @Override
    protected void init() {
        setZIndex(ZIndex.PLAYER);
        createPlayerAvatar();

        var speed = 10f;
        addComponent(new BoxCollider(new Vec2(1, 1)));
        addComponent(new Rigidbody(1f).setDrag(0.2f).setFixedRotation(true));
        addComponent(new KeyMovement(speed, MoveMode.POSITION) {
            @Override
            public Vec2 getUserInput() {
                return super.getUserInput().unit(); // Normalize so don't move faster diagonally
            }
        });

        var halfSize = GDEditorScene.SCENE_SIZE.mul(0.5f);
        addComponent(new KeepInScene(halfSize.mul(-1f), halfSize, KeepInScene.Mode.STOP));
    }

    private void createPlayerAvatar() {
        var spriteSheets = createSpriteSheets();
        addSpriteLayers(spriteSheets);
    }

    private SpriteSheet[] createSpriteSheets() {
        var numLayers = 2;
        var tileSize = GDEditorScene.TILE_SIZE;
        var numSprites = 12 * 4;

        var spriteSheets = new SpriteSheet[numLayers];
        for (int i = 0; i < spriteSheets.length; i++) {
            spriteSheets[i] = Sprites.createSpriteSheet(
                    "assets/geometrydash/textures/player_layer%d.png".formatted(i),
                    tileSize, tileSize, numSprites, 2);
        }
        return spriteSheets;
    }

    private void addSpriteLayers(SpriteSheet[] spriteSheets) {
        var spriteIndex = Random.randomInt(0, NUM_SPRITES - 1);

        var layers = new Sprite[spriteSheets.length];
        for (int i = 0; i < layers.length; i++) {
            layers[i] = spriteSheets[i].getSprite(spriteIndex);
        }

        Color[] colors = {new Color(255, 0, 0), new Color(0, 255, 0)};
        for (var i = 0; i < layers.length; i++) {
            layers[i].setColor(colors[i]);
            addComponent(layers[i]);
        }
    }

}
