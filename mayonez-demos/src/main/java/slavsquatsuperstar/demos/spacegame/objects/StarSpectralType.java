package slavsquatsuperstar.demos.spacegame.objects;

import mayonez.math.*;
import mayonez.util.Record;

/**
 * The spectral class of a star, corresponding to its temperature and color.
 * <br>
 * Source: <a href="https://en.wikipedia.org/wiki/Stellar_classification">
 * Wikipedia</a>
 *
 * @author SlavSquatSuperstar
 */
public record StarSpectralType(
        String className, String colorName,
        int minTemp, int maxTemp, float weight
) {

    StarSpectralType(Record record) {
        this(
                record.getString("class"), record.getString("color"),
                record.getInt("minTemp"), record.getInt("maxTemp"),
                record.getFloat("weight")
        );
    }

    public int getRandomTemp() {
        return Random.randomInt(minTemp, maxTemp);
    }

}
