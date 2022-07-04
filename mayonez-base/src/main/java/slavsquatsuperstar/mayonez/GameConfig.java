package slavsquatsuperstar.mayonez;

import slavsquatsuperstar.mayonez.fileio.JSON;

/**
 * A collection of settings for various parts of the game engine.
 *
 * @author SlavSquatSuperstar
 */
// TODO safety measures so we don't use wrong data type
public abstract class GameConfig extends JSON {

    /**
     * Erases the stored JSON data and replaces it with data from another config object,
     * including key-value pairs with null elements.
     *
     * @param json another JSON object
     */
    protected void copyFrom(JSON json) {
        this.clear(); // erase JSON object
        for (String key : json.keys()) // copy entire JSON object
            this.set(key, json.get(key));
    }

    /**
     * Overwrites or appends key-value pairs with non-null values from another object while preserving other existing data.
     *
     * @param json another JSON object
     */
    protected void loadFrom(JSON json) {
        for (String key : json.keys()) { // overwrite or create values if not null
            Object value = json.get(key);
            if (value != null) this.set(key, value);
        }
    }

}
