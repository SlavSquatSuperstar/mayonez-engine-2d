package slavsquatsuperstar.demos.spacegame.objects;

import mayonez.*;
import mayonez.graphics.*;
import mayonez.graphics.debug.*;
import mayonez.math.*;
import mayonez.physics.*;
import mayonez.physics.colliders.*;
import mayonez.scripts.*;
import slavsquatsuperstar.demos.spacegame.ZIndex;
import slavsquatsuperstar.demos.spacegame.combat.Damageable;

/**
 * An asteroid in space that can be destroyed.
 *
 * @author SlavSquatSuperstar
 */
public class Asteroid extends GameObject {

//    private static final SpriteSheet ASTEROID_SPRITES = Sprites.createSpriteSheet(
//            "assets/textures/spacegame/asteroids.png",
//            32, 32, 2, 0
//    );
    private final SpawnManager obstacleSpawner;
    private final int startingHealth;
    private final Color color;

    public Asteroid(String name, SpawnManager obstacleSpawner) {
        super(name);
        setZIndex(ZIndex.ASTEROID);
        this.obstacleSpawner = obstacleSpawner;
        this.startingHealth = Random.randomInt(8, 12);

        var tint = Random.randomInt(96, 176);
        color = new Color(tint, tint, tint);
    }

    @Override
    protected void init() {
        setRandomTransform();
        addAsteroidCollider();
        addAsteroidSprite();
        addAsteroidStartingVelocity();

        addComponent(new Damageable(startingHealth) {
            @Override
            public void onDestroy() {
                if (obstacleSpawner != null) obstacleSpawner.markObjectDestroyed();
                if (Random.randomPercent(0.2f)) return; // don't spawn fragments

                var fragmentCount = Random.randomInt(2, 4);
                for (var i = 0; i < fragmentCount; i++) {
                    var angle = 360f / fragmentCount;
                    var velocity = getRigidbody().getVelocity().mul(Random.randomFloat(0.5f, 2f))
                            .rotate(angle + Random.randomFloat(-30f, 30f));

                    getScene().addObject(new AsteroidFragment(
                            "Asteroid Fragment", transform.getPosition(),
                            transform.getScale().div(fragmentCount),
                            velocity, color));
                }
            }
        });
    }

    private void setRandomTransform() {
        transform.setPosition(getScene().getRandomPosition());
        transform.setRotation(Random.randomFloat(0f, 360f));
        transform.setScale(Random.randomVector(2f, 4f, 2f, 4f));
    }

    private void addAsteroidCollider() {
        addComponent(new BallCollider(new Vec2(1f)));
        addComponent(new KeepInScene(KeepInScene.Mode.WRAP));
    }

    private void addAsteroidSprite() {
        addComponent(new ShapeSprite(color, true));
        // Disable textures until other objects receive one
//        var sprite = ASTEROID_SPRITES.getSprite(Random.randomInt(0, 1));
//        sprite.setColor(color);
//        addComponent(sprite);
    }

    private void addAsteroidStartingVelocity() {
        Rigidbody rb;
        addComponent(rb = new Rigidbody(startingHealth, 0.2f, 0.2f));
        rb.setVelocity(transform.getUp().mul(Random.randomFloat(0f, 3f)));
    }

}
