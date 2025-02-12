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
    static final int NUM_TEXTURES = 2;
    private static final Texture[] ASTEROID_TEXTURES;

    static {
        var ASTEROID_TEXTURE_FILES = new String[] {
                "assets/spacegame/textures/asteroids/asteroid1.png",
                "assets/spacegame/textures/asteroids/asteroid2.png"
        };
        ASTEROID_TEXTURES = new Texture[NUM_TEXTURES];
        for (int i = 0; i < NUM_TEXTURES; i++) {
            ASTEROID_TEXTURES[i] = Textures.getTexture(ASTEROID_TEXTURE_FILES[i]);
        }
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

        addSprite(properties.color(), properties.spriteIndex());
    }

    private void addSprite(Color color, int spriteIndex) {
        addSprite(ASTEROID_TEXTURES[spriteIndex], color);
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

}
