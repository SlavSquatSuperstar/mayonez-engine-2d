package slavsquatsuperstar.demos.spacegame.objects.asteroids;

import mayonez.*;
import mayonez.graphics.*;
import mayonez.graphics.debug.*;
import mayonez.graphics.sprites.*;
import mayonez.math.*;
import mayonez.physics.*;
import mayonez.physics.colliders.*;
import mayonez.scripts.*;
import slavsquatsuperstar.demos.spacegame.objects.ZIndex;

/**
 * A template for prefab asteroid objects to spawn in space.
 *
 * @author SlavSquatSuperstar
 */
public abstract class BaseAsteroid extends GameObject {

    private static final SpriteSheet ASTEROID_SPRITES = Sprites.createSpriteSheet(
            "assets/spacegame/textures/asteroids.png",
            32, 32, 2, 0
    );

    protected final AsteroidProperties properties;

    public BaseAsteroid(String name, Vec2 position, AsteroidProperties properties) {
        super(name, position);
        setZIndex(ZIndex.ASTEROID);
        this.properties = properties;
    }

    @Override
    protected void init() {
        setZIndex(ZIndex.ASTEROID);
        setTransform(properties);

        addCollider();
        addSprite(properties.color());
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
        var sprite = ASTEROID_SPRITES.getSprite(spriteIndex);
        sprite.setColor(color);
        addComponent(sprite);
    }

    protected Rigidbody addRigidbody(float mass) {
        Rigidbody rb;
        addComponent(rb = new Rigidbody(mass, 0.2f, 0.2f));
        return rb;
    }

}
