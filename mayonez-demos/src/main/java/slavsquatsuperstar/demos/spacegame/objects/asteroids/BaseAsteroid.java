package slavsquatsuperstar.demos.spacegame.objects.asteroids;

import mayonez.*;
import mayonez.graphics.*;
import mayonez.graphics.debug.*;
import mayonez.graphics.sprites.*;
import mayonez.graphics.textures.*;
import mayonez.math.*;
import mayonez.physics.colliders.*;
import mayonez.physics.dynamics.*;
import mayonez.scripts.*;
import slavsquatsuperstar.demos.spacegame.objects.SpaceGameLayer;
import slavsquatsuperstar.demos.spacegame.objects.SpaceGameZIndex;

/**
 * A template for prefab asteroid objects to spawn in space.
 *
 * @author SlavSquatSuperstar
 */
public abstract class BaseAsteroid extends GameObject {

    private static final int NUM_TEXTURES = 2;
    private static final Texture[] ASTEROID_TEXTURES;

    static {
        ASTEROID_TEXTURES = new Texture[NUM_TEXTURES];
        for (int i = 0; i < NUM_TEXTURES; i++) {
            ASTEROID_TEXTURES[i] = Textures.getTexture(
                    "assets/spacegame/textures/asteroid%d.png"
                            .formatted(i + 1)
            );
        }
    }

//    private static final SpriteSheet ASTEROID_SPRITES = Sprites.createSpriteSheet(
//            "assets/spacegame/textures/asteroids.png",
//            32, 32, 2, 0
//    );

    protected final AsteroidProperties properties;

    public BaseAsteroid(String name, Vec2 position, AsteroidProperties properties) {
        super(name, position);
        this.properties = properties;
    }

    @Override
    protected void init() {
        setLayer(getScene().getLayer(SpaceGameLayer.ASTEROIDS));
        setZIndex(SpaceGameZIndex.ASTEROID);
        setTransform(properties);

        addCollider();
//        addSprite(properties.color());
        addSprite(properties.color(), Random.randomInt(0, 1));
    }

    private void setTransform(AsteroidProperties properties) {
        transform.setRotation(Random.randomAngle());
        transform.setScale(properties.getScale());
    }

    private void addCollider() {
        addComponent(new BallCollider(new Vec2(1f)));
        addComponent(new KeepInScene(KeepInScene.Mode.WRAP));
    }

    private void addSprite(Color color) {
        addComponent(new ShapeSprite(color, true));
    }

    private void addSprite(Color color, int spriteIndex) {
//        var sprite = ASTEROID_SPRITES.getSprite(spriteIndex);
        var sprite = Sprites.createSprite(ASTEROID_TEXTURES[spriteIndex]);
        sprite.setColor(color);
        addComponent(sprite);
    }

    protected Rigidbody addRigidbody(float mass) {
        Rigidbody rb;
        addComponent(rb = new Rigidbody(mass, 0.2f, 0.2f));
        return rb;
    }

}
