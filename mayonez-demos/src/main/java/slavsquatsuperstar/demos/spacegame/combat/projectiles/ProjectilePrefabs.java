package slavsquatsuperstar.demos.spacegame.combat.projectiles;

import mayonez.*;
import mayonez.graphics.sprites.*;
import mayonez.math.Random;
import mayonez.math.*;
import mayonez.physics.colliders.*;
import mayonez.physics.dynamics.*;
import mayonez.scripts.*;
import slavsquatsuperstar.demos.spacegame.objects.SpaceGameLayer;
import slavsquatsuperstar.demos.spacegame.objects.SpaceGameZIndex;
import slavsquatsuperstar.demos.spacegame.objects.ships.SpaceshipPrefabs;

import java.util.*;

/**
 * Creates prefab projectiles that spaceships can fire.
 *
 * @author SlavSquatSuperstar
 */
public final class ProjectilePrefabs {

    // Constants
    public static final List<ProjectileType> PROJECTILE_TYPES;
    public static final SpriteSheet PROJECTILE_SPRITES, PARTICLE_SPRITES;

    static {
        // Read projectile types
        var records = SpaceshipPrefabs
                .getRecordsFromFile("assets/spacegame/data/projectiles.csv");
        PROJECTILE_TYPES = records.stream().map(ProjectileType::new).toList();

        // Read sprite sheets
        PROJECTILE_SPRITES = Sprites.createSpriteSheet(
                "assets/spacegame/textures/combat/projectiles.png",
                16, 16, PROJECTILE_TYPES.size(), 0);
        PARTICLE_SPRITES = Sprites.createSpriteSheet(
                "assets/spacegame/textures/combat/impacts.png",
                16, 16, PROJECTILE_TYPES.size(), 0);
    }

    private ProjectilePrefabs() {
    }

    // Create Prefab Methods

    /**
     * Create a prefab {@link Projectile} object with the specified projectile type.
     *
     * @param type        the projectile type
     * @param source      the object that fired the projectile
     * @param offsetPos   the projectile spawn position in relation to the source
     * @param offsetAngle the projectile spawn angle in relation to the source
     * @return the projectile object, or null if the index is invalid
     */
    public static GameObject createProjectilePrefab(
            ProjectileType type, GameObject source, Vec2 offsetPos, float offsetAngle
    ) {
        var projXf = getProjectileTransform(type, source.transform, offsetPos, offsetAngle);
        return new GameObject(type.name(), projXf, SpaceGameZIndex.PROJECTILE) {
            @Override
            protected void init() {
                setLayer(getScene().getLayer(SpaceGameLayer.PROJECTILES));
                addComponent(new Projectile(source, type));
                addComponent(PROJECTILE_SPRITES.getSprite(type.spriteIndex()));

                var col = new BulletBoxCollider(type.colliderSize());
                col.setPrimaryAxisX(false);
                col.setSweepFactor(1.5f);
                addComponent(col.setTrigger(true));
                addComponent(new Rigidbody(0.001f));
            }
        };
    }

    /**
     * Get the projectile transform in world space.
     *
     * @param type        the projectile type
     * @param sourceXf    the transform of the source object
     * @param offsetPos   the projectile spawn position in relation to the source
     * @param offsetAngle the projectile spawn angle in relation to the source
     * @return the projectile transform
     */
    private static Transform getProjectileTransform(
            ProjectileType type, Transform sourceXf, Vec2 offsetPos, float offsetAngle
    ) {
        var weaponSpreadAngle = Random.randomFloat(-type.weaponSpread(), type.weaponSpread());
        return new Transform(
                sourceXf.toWorld(offsetPos),
                sourceXf.getRotation() + offsetAngle + weaponSpreadAngle,
                type.scale()
        );
    }

    /**
     * Create an impact particle caused by a projectile impacting an object.
     *
     * @param type       the projectile type
     * @param particleXf the particle transform
     * @param target     the impacted object
     * @return the particle object
     */
    public static GameObject createImpactPrefab(
            ProjectileType type, Transform particleXf, GameObject target
    ) {
        return new GameObject("%s Impact".formatted(type.name()), particleXf) {
            @Override
            protected void init() {
                var duration = Random.randomFloat(0.1f, 0.4f);
                addComponent(new DestroyAfterDuration(duration));
                addComponent(PARTICLE_SPRITES.getSprite(type.spriteIndex()));
                addComponent(new Script() {
                    private Vec2 targetPositionOffset;
                    private float targetRotationOffset;

                    @Override
                    protected void start() {
                        targetPositionOffset = particleXf.getPosition()
                                .sub(target.transform.getPosition());
                        targetRotationOffset = particleXf.getRotation()
                                - target.transform.getRotation();
                    }

                    @Override
                    protected void debugRender() {
                        if (target.isDestroyed()) return;

                        // Follow target
                        this.transform.setPosition(target.transform.getPosition()
                                .add(targetPositionOffset));
                        this.transform.setRotation(target.transform.getRotation()
                                + (targetRotationOffset));
                    }
                });
            }
        };
    }

}
