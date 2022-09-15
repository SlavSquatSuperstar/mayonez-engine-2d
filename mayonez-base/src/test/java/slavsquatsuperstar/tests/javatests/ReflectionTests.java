package slavsquatsuperstar.tests.javatests;

import org.junit.jupiter.api.Test;
import slavsquatsuperstar.mayonez.Logger;

import java.lang.reflect.Constructor;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for the java.lang.reflect package.
 *
 * @author SlavSquatSuperstar
 */
public class ReflectionTests {

    @Test
    public void newInstanceSuccess() {
        Asset inst = newInstance(Asset.class, "preferences.json", true);
        assertEquals("preferences.json", inst.filename);
        assertTrue(inst.isClasspath);
    }

    @Test
    public void callConstructorSuccess() {
        TextFile inst = newAssetInstance(TextFile.class, "preferences.json", true);
        assertEquals("preferences.json", inst.filename);
        assertTrue(inst.isClasspath);
        inst.read();
    }

    @Test
    public void newInstanceFail() {
        Asset inst1 = newInstance(Asset.class, "foo", "bar");
        assertNull(inst1);
        Asset inst2 = newInstance(Asset.class);
        assertNull(inst2);
    }

    public static <T> T newInstance(Class<T> cls, Object... args) {
        try {
            Constructor<?> ctor = cls.getConstructors()[0];
            return cls.cast(ctor.newInstance(args));
        } catch (Exception e) {
            Logger.log("Error initializing object %s", cls.getName());
            return null;
        }
    }

    public static <T extends Asset> T newAssetInstance(Class<T> cls, String filename, boolean isClasspath) {
        try {
            Constructor<?> ctor = cls.getDeclaredConstructor(String.class, boolean.class);
            return cls.cast(ctor.newInstance(filename, isClasspath));
        } catch (Exception e) {
            Logger.log("Error initializing asset %s", cls.getName());
            e.printStackTrace();
            return null;
        }
    }

    static class Asset {
        String filename;
        boolean isClasspath;

        public Asset(String filename, boolean isClasspath) {
            this.filename = filename;
            this.isClasspath = isClasspath;
        }

        @Override
        public String toString() {
            return String.format("%s %s (%s)", getClass().getSimpleName(),
                    filename, isClasspath ? "Classpath" : "Local");
        }
    }

    static class TextFile extends Asset {

        public TextFile(String filename, boolean isClasspath) {
            super(filename, isClasspath);
        }

        public TextFile(String filename) {
            super(filename, false);
        }

        public void read() {
            System.out.println("Read text from " + filename);
        }

    }
}


