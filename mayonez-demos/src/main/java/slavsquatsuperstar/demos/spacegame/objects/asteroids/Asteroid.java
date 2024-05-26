package slavsquatsuperstar.demos.spacegame.objects.asteroids;

import mayonez.*;
import mayonez.graphics.*;
import mayonez.graphics.sprites.*;
import mayonez.graphics.textures.*;
import mayonez.math.*;
import mayonez.physics.colliders.*;
import mayonez.physics.dynamics.*;
import mayonez.scripts.*;
import slavsquatsuperstar.demos.spacegame.objects.SpaceGameLayer;
import slavsquatsuperstar.demos.spacegame.objects.SpaceGameZIndex;

/**
 * An asteroid in space that can be destroyed and spawn fragments.
 *
 * @author SlavSquatSuperstar
 */
public abstract class Asteroid extends GameObject {

    // Constants
    private static final int NUM_TEXTURES = 2;
    private static final Texture[] ASTEROID_TEXTURES = getAsteroidTextures();

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

        addSprite(properties.color(), Random.randomInt(0, NUM_TEXTURES - 1));
    }

    private void addSprite(Color color, int spriteIndex) {
        var sprite = Sprites.createSprite(ASTEROID_TEXTURES[spriteIndex]);
        sprite.setColor(color);
        addComponent(sprite);
    }

    protected void addCollider() {
        addComponent(new BallCollider(new Vec2(1f)));
        addComponent(new KeepInScene(KeepInScene.Mode.WRAP));
    }

    protected Rigidbody addRigidbody(float mass) {
        Rigidbody rb;
        addComponent(rb = new Rigidbody(mass, 0.2f, 0.2f));
        return rb;
    }

    // Static Methods

    private static Texture[] getAsteroidTextures() {
        var asteroidTextures = new Texture[NUM_TEXTURES];
        for (int i = 0; i < NUM_TEXTURES; i++) {
            asteroidTextures[i] = Textures.getTexture(
                    "assets/spacegame/textures/asteroids/asteroid%d.png"
                            .formatted(i + 1)
            );
        }
        return asteroidTextures;
    }

}
