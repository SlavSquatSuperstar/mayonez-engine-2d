package slavsquatsuperstar.demos.spacegame.objects.ships;

import mayonez.graphics.textures.*;
import mayonez.math.*;
import mayonez.util.Record;
import slavsquatsuperstar.demos.spacegame.combat.projectiles.WeaponHardpoint;
import slavsquatsuperstar.demos.spacegame.movement.ThrusterProperties;

import java.util.*;

/**
 * A set of properties for spaceships.
 *
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
public record SpaceshipProperties(
        Vec2 colliderSize,
        float moveThrust, float turnThrust,
        float maxHull, float maxShield, float shieldRegen,
        Texture texture,
        List<ThrusterProperties> thrusters, List<WeaponHardpoint> hardpoints
) {

    public SpaceshipProperties(Record record) {
        this(
                new Vec2(record.getFloat("sizeX"), record.getFloat("sizeY")),
                record.getFloat("moveThrust"), record.getFloat("turnThrust"),
                record.getFloat("maxHull"),
                record.getFloat("maxShield"), record.getFloat("shieldRegen"),
                Textures.getTexture(record.getString("spriteFile")),
                getThrusters(record.getString("thrustersFile")),
                getHardpoints(record.getString("hardpointsFile"))
        );
    }

    private static List<ThrusterProperties> getThrusters(String csvFileName) {
        return SpaceshipPrefabs.getRecordsFromFile(csvFileName)
                .stream().map(ThrusterProperties::new).toList();
    }

    private static List<WeaponHardpoint> getHardpoints(String csvFileName) {
        return SpaceshipPrefabs.getRecordsFromFile(csvFileName)
                .stream().map(WeaponHardpoint::new).toList();
    }

}
