package slavsquatsuperstar.demos.spacegame.objects.ships;

import mayonez.graphics.textures.*;
import mayonez.math.*;
import mayonez.util.Record;
import slavsquatsuperstar.demos.spacegame.PrefabUtils;
import slavsquatsuperstar.demos.spacegame.combat.projectiles.WeaponHardpoint;
import slavsquatsuperstar.demos.spacegame.movement.ThrusterProperties;

import java.util.*;

/**
 * A set of properties for spaceships.
 *
 * @param name         the spaceship's name
 * @param scale        the spaceship's transform scale
 * @param colliderSize the spaceship's relative hitbox size
 * @param moveThrust   the spaceship's move thrust power
 * @param turnThrust   the spaceship's turn thrust power
 * @param maxHull      the spaceship's base hull health
 * @param maxShield    the spaceship's base shield health
 * @param shieldRegen  the spaceship's base shield regen speed
 * @param texture      the spaceship's sprite texture
 * @param thrusters    the spaceship's thruster configuration
 * @param hardpoints   the spaceship's hardpoint configuration
 * @author SlavSquatSuperstar
 */
// TODO shield and thruster properties
public record SpaceshipProperties(
        String name,
        Vec2 scale, Vec2 colliderSize,
        float moveThrust, float turnThrust,
        float maxHull, float maxShield, float shieldRegen,
        Texture texture,
        List<ThrusterProperties> thrusters, List<WeaponHardpoint> hardpoints
) {

    public SpaceshipProperties(Record record) {
        this(
                record.getString("name"),
                new Vec2(record.getFloat("scale")),
                PrefabUtils.getColliderSize(record),
                record.getFloat("moveThrust"), record.getFloat("turnThrust"),
                record.getFloat("maxHull"),
                record.getFloat("maxShield"), record.getFloat("shieldRegen"),
                Textures.getTexture(record.getString("textureFile")),
                getThrusters(record.getString("thrustersFile")),
                getHardpoints(record.getString("hardpointsFile"))
        );
    }

    private static List<ThrusterProperties> getThrusters(String csvFileName) {
        return PrefabUtils.getRecordsFromFile(csvFileName)
                .stream().map(ThrusterProperties::new).toList();
    }

    private static List<WeaponHardpoint> getHardpoints(String csvFileName) {
        return PrefabUtils.getRecordsFromFile(csvFileName)
                .stream().map(WeaponHardpoint::new).toList();
    }

}
