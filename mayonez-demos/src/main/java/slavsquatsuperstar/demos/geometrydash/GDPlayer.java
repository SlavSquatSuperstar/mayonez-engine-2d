package slavsquatsuperstar.demos.geometrydash;

import mayonez.*;
import mayonez.graphics.*;
import mayonez.graphics.sprites.*;
import mayonez.input.*;
import mayonez.math.*;
import mayonez.math.shapes.Rectangle;
import mayonez.physics.colliders.*;
import mayonez.physics.dynamics.*;
import mayonez.scripts.*;

/**
 * The Geometry Dash player character controlled with the WASD keys.
 *
 * @author SlavSquatSuperstar
 */
class GDPlayer extends Node {

    // Constants
    private static final float PLAYER_SPEED = 12f;
    private static final InputAxis HORIZ_AXIS
            = DefaultKeyAxis.findWithName("horizontal");
    private static final InputAxis VERT_AXIS
            = DefaultKeyAxis.findWithName("vertical");

    private static final int NUM_LAYERS = 2;
    private static final int NUM_SPRITES = 12 * 4;
    private static final SpriteSheet[] PLAYER_SPRITE_SHEETS
            = createSpriteSheets();

    GDPlayer(String name, Vec2 position) {
        super(name, new Transform(position));
    }

    @Override
    protected void init() {
        setZIndex(ZIndex.PLAYER);

        // Player Avatar
        addSpriteLayers();

        addChild(new Collider(new Rectangle(new Vec2(1, 1))));
        addChild(new Rigidbody(1f).setDrag(0.2f).setFixedRotation(true));

        var halfSize = GDEditorScene.SCENE_SIZE.mul(0.5f);
        addChild(new KeepInScene(halfSize.mul(-1f), halfSize, KeepInScene.Mode.STOP));
    }

    @Override
    protected void fixedUpdate(float dt) {
        // Normalize so don't move faster diagonally
        var userInput = new Vec2(KeyInput.getAxis(HORIZ_AXIS), KeyInput.getAxis(VERT_AXIS)).unit();
        getTransform().move(userInput.mul(PLAYER_SPEED * dt));
    }

    private void addSpriteLayers() {
        var spriteIndex = Random.randomInt(0, NUM_SPRITES - 1);

        var layers = new Sprite[NUM_LAYERS];
        for (int i = 0; i < layers.length; i++) {
            layers[i] = PLAYER_SPRITE_SHEETS[i].getSprite(spriteIndex);
        }

        Color[] colors = {
                new Color(255, 0, 0),
                new Color(0, 255, 0)
        };
        for (var i = 0; i < layers.length; i++) {
            layers[i].setColor(colors[i]);
            addChild(layers[i]);
        }
    }

    private static SpriteSheet[] createSpriteSheets() {
        var tileSize = GDEditorScene.TILE_SIZE;
        var numSprites = 12 * 4;

        var spriteSheets = new SpriteSheet[NUM_LAYERS];
        for (int i = 0; i < spriteSheets.length; i++) {
            spriteSheets[i] = Sprites.createSpriteSheet(
                    "assets/geometrydash/textures/player_layer%d.png".formatted(i),
                    tileSize, tileSize, numSprites, 2);
        }
        return spriteSheets;
    }

}
