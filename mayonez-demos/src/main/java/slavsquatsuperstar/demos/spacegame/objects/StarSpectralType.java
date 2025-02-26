package slavsquatsuperstar.demos.spacegame.objects;

import mayonez.math.*;
import mayonez.util.Record;

/**
 * The spectral class of a star, corresponding to its temperature and color.
 * Note: Using "Type" to avoid conflict with the Java keyword "class".
 * <p>
 * Source: <a href="https://en.wikipedia.org/wiki/Stellar_classification">
 *     Wikipedia - Stellar classification</a>
 *
 * @author SlavSquatSuperstar
 */
public record StarSpectralType(
        String type, String description,
        int minTemp, int maxTemp,
        float weight
) {

    StarSpectralType(Record record) {
        this(
                record.getString("type"), record.getString("description"),
                record.getInt("minTemp"), record.getInt("maxTemp"),
                record.getFloat("weight")
        );
    }

    public int getRandomTemp() {
        return Random.randomInt(minTemp, maxTemp);
    }

}
