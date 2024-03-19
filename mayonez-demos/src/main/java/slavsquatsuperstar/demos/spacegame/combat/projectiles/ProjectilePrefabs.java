package slavsquatsuperstar.demos.spacegame.combat.projectiles;

import mayonez.*;
import mayonez.assets.*;
import mayonez.assets.text.*;
import mayonez.graphics.sprites.*;
import mayonez.math.Random;
import mayonez.physics.colliders.*;
import mayonez.physics.dynamics.*;
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
    private static final CSVFile PROJECTILE_DATA = Assets.getAsset(
            "assets/spacegame/data/projectiles.csv", CSVFile.class);
    private static final List<ProjectileType> PROJECTILE_TYPES;
    public static final int NUM_PROJECTILES;
    public static final SpriteSheet PROJECTILE_SPRITES;

    static {
        PROJECTILE_TYPES = readProjectileTypes();
        NUM_PROJECTILES = PROJECTILE_TYPES.size();
        PROJECTILE_SPRITES = Sprites.createSpriteSheet(
                "assets/spacegame/textures/combat/projectiles.png",
                16, 16, NUM_PROJECTILES, 0);
    }

    private ProjectilePrefabs() {
    }

    // Projectile Data Methods

    /**
     * Get the number of projectile types.
     *
     * @return the count
     */
    public static int count() {
        return Math.min(NUM_PROJECTILES, PROJECTILE_TYPES.size());
    }

    /**
     * Get the {@link ProjectileType} stored at the specified index.
     *
     * @param projectileIndex the index of the {@link ProjectileType}
     * @return the projectile type, or null if the index is invalid
     */
    public static ProjectileType getProjectileType(int projectileIndex) {
        if (projectileIndex < 0 || projectileIndex >= NUM_PROJECTILES) return null;
        return PROJECTILE_TYPES.get(projectileIndex);
    }

    private static List<ProjectileType> readProjectileTypes() {
        if (PROJECTILE_DATA == null) return Collections.emptyList();
        return PROJECTILE_DATA.readCSV().stream()
                .map(ProjectileType::new).toList();
    }

    // Create Prefab Methods

    /**
     * Creates a prefab {@link Projectile} object with the specified projectile type.
     *
     * @param projectileIndex the index of the {@link ProjectileType}
     * @param source          the object that fired the projectile
     * @return the projectile object, or null if the index is invalid
     */
    public static GameObject createPrefab(int projectileIndex, GameObject source) {
        var type = getProjectileType(projectileIndex);
        if (type == null) return null;
        else return createProjectileObject(type, source);
    }

    // TODO add spawn offset
    private static GameObject createProjectileObject(ProjectileType type, GameObject source) {
        return new GameObject(type.name(), getProjectileTransform(type, source), SpaceGameZIndex.PROJECTILE) {
            @Override
            protected void init() {
                setLayer(getScene().getLayer(SpaceGameLayer.PROJECTILES));
                addComponent(new Projectile(source, type.damage(), type.speed(), type.lifetime()));
                addComponent(PROJECTILE_SPRITES.getSprite(type.spriteIndex()));

                addComponent(new BallCollider(type.colliderSize()).setTrigger(true));
                addComponent(new Rigidbody(0.001f));
            }
        };
    }

    private static Transform getProjectileTransform(ProjectileType type, GameObject source) {
        var sourceXf = source.transform;
        return new Transform(
                sourceXf.getPosition().add(sourceXf.getUp().mul(0.5f)),
                sourceXf.getRotation() + Random.randomFloat(-type.weaponSpread(), type.weaponSpread()),
                type.scale()
        );
    }

}
