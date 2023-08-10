package slavsquatsuperstar.demos.spacegame.objects;

import mayonez.GameObject;
import mayonez.graphics.Color;
import mayonez.graphics.debug.ShapeSprite;
import mayonez.graphics.sprites.SpriteSheet;
import mayonez.graphics.sprites.Sprites;
import mayonez.math.Vec2;
import mayonez.physics.Rigidbody;
import mayonez.physics.colliders.BallCollider;
import mayonez.scripts.KeepInScene;

/**
 * Creates prefab asteroid objects to spawn in space.
 *
 * @author SlavSquatSuperstar
 */
public class AsteroidPrefabs {

    private static final SpriteSheet ASTEROID_SPRITES = Sprites.createSpriteSheet(
            "assets/spacegame/textures/asteroids.png",
            32, 32, 2, 0
    );

    public static void addCollider(GameObject asteroid) {
        asteroid.addComponent(new BallCollider(new Vec2(1f)));
        asteroid.addComponent(new KeepInScene(KeepInScene.Mode.WRAP));
    }

    public static void addSprite(GameObject asteroid, Color color) {
        asteroid.addComponent(new ShapeSprite(color, true));
    }

    public static void addSprite(GameObject asteroid, int spriteIndex, Color color) {
        // Disable textures until other objects receive one
        var sprite = ASTEROID_SPRITES.getSprite(spriteIndex);
        sprite.setColor(color);
        asteroid.addComponent(sprite);
    }

    public static void setStartingVelocity(GameObject asteroid, float mass, Vec2 velocity) {
        Rigidbody rb;
        asteroid.addComponent(rb = new Rigidbody(mass, 0.2f, 0.2f));
        rb.setVelocity(velocity);
    }

}
