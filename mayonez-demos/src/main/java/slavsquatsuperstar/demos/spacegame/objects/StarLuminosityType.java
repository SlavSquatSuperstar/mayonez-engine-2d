package slavsquatsuperstar.demos.spacegame.objects;

import mayonez.math.*;
import mayonez.util.Record;

/**
 * The luminosity class of a star, corresponding to its size and brightness.
 * Note: Using "Type" to avoid conflict with the Java keyword "class".
 * <p>
 * Source: <a href="https://en.wikipedia.org/wiki/Stellar_classification">
 *     Wikipedia - Stellar classification</a>
 *
 * @author SlavSquatSuperstar
 */
public record StarLuminosityType(
        String type, String description,
        float minBrightness, float maxBrightness,
        float minRadius, float maxRadius,
        float weight
) {

    StarLuminosityType(Record record) {
        this(
                record.getString("type"), record.getString("description"),
                record.getFloat("minBrightness"), record.getFloat("maxBrightness"),
                record.getFloat("minRadius"), record.getFloat("maxRadius"),
                record.getFloat("weight")
        );
    }

    public float getRandomBrightness() {
        return Random.randomFloat(minBrightness, maxBrightness);
    }

    public float getRandomRadius() {
        return Random.randomFloat(minRadius, maxRadius);
    }

}
