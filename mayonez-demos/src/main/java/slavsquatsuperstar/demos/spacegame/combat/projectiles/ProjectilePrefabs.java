package slavsquatsuperstar.demos.spacegame.combat.projectiles;

import mayonez.*;
import mayonez.graphics.sprites.*;
import mayonez.math.Random;
import mayonez.math.*;
import mayonez.physics.CollisionEvent;
import mayonez.physics.colliders.*;
import mayonez.physics.dynamics.*;
import mayonez.scripts.*;
import slavsquatsuperstar.demos.spacegame.PrefabUtils;
import slavsquatsuperstar.demos.spacegame.objects.SpaceGameLayer;
import slavsquatsuperstar.demos.spacegame.objects.SpaceGameZIndex;

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
        PROJECTILE_TYPES = PrefabUtils.getObjectsFromFile(
                "assets/spacegame/data/projectiles.csv",
                ProjectileType::new
        );

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
     * @param type      the projectile type
     * @param source    the object that fired the projectile
     * @param hardpoint the projectile spawn offset in relation to the source
     * @return the projectile object
     */
    public static Node createProjectilePrefab(
            ProjectileType type, Node source, WeaponHardpoint hardpoint
    ) {
        return new Projectile(type, source) {
            @Override
            protected void init() {
                super.init();
                var weaponSpreadAngle = Random.randomFloat(-type.weaponSpread(), type.weaponSpread());
                var sourceXf = source.getTransform();
                var projXf = new Transform(
                        sourceXf.toWorld(hardpoint.offset()),
                        sourceXf.getRotation() + hardpoint.angle() + weaponSpreadAngle,
                        type.scale()
                );
                projXf = getProjectileTransform(type, source.getTransform(), hardpoint);
                setTransform(projXf);

                var sprite = PROJECTILE_SPRITES.getSprite(type.spriteIndex());
                sprite.setZIndex(SpaceGameZIndex.PROJECTILE);
                addChild(sprite);

                var col = new BulletBoxCollider(type.colliderSize()) {
                    @Override
                    public void onCollisionEvent(CollisionEvent event) {
                        onImpactObject(event); // On trigger
                    }
                };
                col.setLayer(getScene().getLayer(SpaceGameLayer.PROJECTILES));
                col.setPrimaryAxisX(false);
                col.setSweepFactor(type.sweepFactor());
                col.setTrigger(true);
                addChild(col);

                // Set initial velocity
                var projRb = new Rigidbody(0.001f);
                var sourceRb = source.getChild(Rigidbody.class);
                if (sourceRb != null) projRb.setVelocity(sourceRb.getVelocity());
                var initialVelocity = getGlobalTransform().getUp().mul(type.speed());
                projRb.addVelocity(initialVelocity);
                addChild(projRb);
            }
        };
    }

    /**
     * Get the projectile transform in world space.
     *
     * @param type      the projectile type
     * @param sourceXf  the transform of the source object
     * @param hardpoint the projectile spawn offset in relation to the source
     * @return the projectile transform
     */
    private static Transform getProjectileTransform(
            ProjectileType type, Transform sourceXf, WeaponHardpoint hardpoint
    ) {
        var weaponSpreadAngle = Random.randomFloat(-type.weaponSpread(), type.weaponSpread());
        return new Transform(
                sourceXf.toWorld(hardpoint.offset()),
                sourceXf.getRotation() + hardpoint.angle() + weaponSpreadAngle,
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
    public static Node createImpactPrefab(
            ProjectileType type, Transform particleXf, Node target
    ) {
        return new Node("%s Impact".formatted(type.name()), particleXf) {
            @Override
            protected void init() {
                var duration = Random.randomFloat(0.1f, 0.4f);
                addChild(new DestroyAfterDuration(duration));
                addChild(PARTICLE_SPRITES.getSprite(type.spriteIndex()));
                addChild(new ParticleFollowTarget(target));
            }
        };
    }

}
