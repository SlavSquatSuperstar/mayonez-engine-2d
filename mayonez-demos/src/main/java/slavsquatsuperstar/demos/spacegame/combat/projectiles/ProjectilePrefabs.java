package slavsquatsuperstar.demos.spacegame.combat.projectiles;

import mayonez.*;
import mayonez.assets.*;
import mayonez.assets.text.*;

import java.util.*;

/**
 * Creates prefab projectiles that spaceships can fire.
 *
 * @author SlavSquatSuperstar
 */
public final class ProjectilePrefabs {

    private static final CSVFile PROJECTILE_DATA = Assets.getAsset(
            "assets/spacegame/data/projectiles.csv", CSVFile.class);

    private static final List<ProjectileType> PROJECTILE_TYPES = readProjectileTypes();

    private ProjectilePrefabs() {
    }

    /**
     * Get the number of projectile types.
     *
     * @return the count
     */
    public static int count() {
        return PROJECTILE_TYPES.size();
    }

    /**
     * Get the {@link ProjectileType} stored at the specified index.
     *
     * @param projectileIndex the index of the {@link ProjectileType}
     * @return the projectile type, or null if the index is invalid
     */
    public static ProjectileType getProjectileType(int projectileIndex) {
        if (projectileIndex < 0 || projectileIndex >= PROJECTILE_TYPES.size()) return null;
        return PROJECTILE_TYPES.get(projectileIndex);
    }

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
        else return type.createProjectileObject(source);
    }

    private static List<ProjectileType> readProjectileTypes() {
        if (PROJECTILE_DATA == null) return Collections.emptyList();
        return PROJECTILE_DATA.readCSV().stream()
                .map(ProjectileType::new).toList();
    }

}
