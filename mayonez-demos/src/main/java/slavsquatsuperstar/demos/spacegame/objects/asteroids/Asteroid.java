package slavsquatsuperstar.demos.spacegame.objects.asteroids;

import mayonez.*;
import mayonez.graphics.*;
import mayonez.graphics.sprites.*;
import mayonez.graphics.textures.*;
import mayonez.math.*;
import mayonez.physics.colliders.*;
import mayonez.physics.dynamics.*;
import mayonez.scripts.*;
import slavsquatsuperstar.demos.spacegame.SpaceGameScene;
import slavsquatsuperstar.demos.spacegame.objects.SpaceGameLayer;
import slavsquatsuperstar.demos.spacegame.objects.SpaceGameZIndex;

/**
 * An asteroid in space that can be destroyed and spawn fragments.
 *
 * @author SlavSquatSuperstar
 */
public abstract class Asteroid extends GameObject {

    // Constants
    private static final int NUM_LARGE_TEXTURES = 2;
    private static final Texture[] LARGE_ASTEROID_TEXTURES;
    private static final int NUM_SMALL_TEXTURES = 4;
    private static final SpriteSheet SMALL_ASTEROID_TEXTURES;

    static {
        // Read textures
        var ASTEROID_TEXTURE_FILES = new String[] {
                "assets/spacegame/textures/asteroids/asteroid1.png",
                "assets/spacegame/textures/asteroids/asteroid2.png"
        };
        LARGE_ASTEROID_TEXTURES = new Texture[NUM_LARGE_TEXTURES];
        for (int i = 0; i < NUM_LARGE_TEXTURES; i++) {
            LARGE_ASTEROID_TEXTURES[i] = Textures.getTexture(ASTEROID_TEXTURE_FILES[i]);
        }

        // Read spritesheet
        SMALL_ASTEROID_TEXTURES = Sprites.createSpriteSheet(
                "assets/spacegame/textures/asteroids/asteroids_small.png",
                8, 8, NUM_SMALL_TEXTURES, 0);
    }

    // Instance Fields
    protected final AsteroidProperties properties;

    public Asteroid(String name, Vec2 position, AsteroidProperties properties) {
        super(name, position);
        this.properties = properties;
    }

    @Override
    protected void init() {
        setLayer(getScene().getLayer(SpaceGameLayer.ASTEROIDS));
        setZIndex(SpaceGameZIndex.ASTEROID);

        transform.setRotation(Random.randomAngle());
        transform.setScale(properties.getScale());

        addSprite(properties.texture(), properties.color());
    }

    private void addSprite(Texture texture, Color color) {
        var sprite = Sprites.createSprite(texture);
        sprite.setColor(color);
        addComponent(sprite);
    }

    protected void addCollider() {
        addComponent(new BallCollider(new Vec2(1f)));
        addComponent(new KeepInScene(SpaceGameScene.SCENE_HALF_SIZE.mul(-1f),
                SpaceGameScene.SCENE_HALF_SIZE, KeepInScene.Mode.WRAP));
    }

    protected Rigidbody addRigidbody(float mass) {
        Rigidbody rb;
        addComponent(rb = new Rigidbody(mass, 0.01f, 0.01f));
        return rb;
    }

    protected Rigidbody addRigidbody(float mass, Vec2 startImpulse, float startAngularImpulse) {
        Rigidbody rb;
        addComponent(rb = new Rigidbody(mass, 0.01f, 0.01f));
        rb.applyImpulse(startImpulse);
        rb.applyAngularImpulse(startAngularImpulse);
        return rb;
    }

    // Static Methods

    static Texture getRandomLargeTexture() {
        return LARGE_ASTEROID_TEXTURES[Random.randomInt(0, NUM_LARGE_TEXTURES - 1)];
    }

    static Texture getRandomSmallTexture() {
        return SMALL_ASTEROID_TEXTURES.getTexture(Random.randomInt(0, NUM_SMALL_TEXTURES - 1));
    }

}
