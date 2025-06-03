package slavsquatsuperstar.demos;

import mayonez.*;
import mayonez.util.Record;

/**
 * Stores information about a scene read from a file.
 *
 * @param sceneClass the scene's full Java class name
 * @param name       the scene's instance name
 * @author SlavSquatSuperstar
 */
record SceneInfo(String sceneClass, String name) {

    SceneInfo(Record record) {
        this(record.getString("sceneClass"), record.getString("name"));
    }

    Scene instantiate() {
        try {
            var cls = Class.forName(sceneClass);
            if (Scene.class.isAssignableFrom(cls)) {
                var ctor = cls.getDeclaredConstructor(String.class);
                return (Scene) ctor.newInstance(name);
            }
        } catch (ReflectiveOperationException e) {
            Logger.error("Could not instantiate scene from class \"%s\"", sceneClass);
            return null;
        }
        return null;
    }
}
