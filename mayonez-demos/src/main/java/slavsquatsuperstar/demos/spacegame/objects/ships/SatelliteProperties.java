package slavsquatsuperstar.demos.spacegame.objects.ships;

import mayonez.graphics.textures.*;
import mayonez.math.*;
import mayonez.util.Record;
import slavsquatsuperstar.demos.spacegame.PrefabUtils;

/**
 * A set of properties for satellites.
 *
 * @param name         the satellite's name
 * @param scale        the spaceship's transform scale
 * @param colliderSize the spaceship's relative hitbox size
 * @param maxHull      the spaceship's base hull health
 * @param texture      the spaceship's sprite texture
 */
public record SatelliteProperties(
        String name,
        Vec2 scale, Vec2 colliderSize,
        float maxHull,
        Texture texture
) {

    public SatelliteProperties(Record record) {
        this(
                record.getString("name"),
                new Vec2(record.getFloat("scale")),
                PrefabUtils.getColliderSize(record),
                record.getFloat("maxHull"),
                Textures.getTexture(record.getString("textureFile"))
        );
    }

}
