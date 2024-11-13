package mayonez.assets;

import mayonez.assets.text.*;
import org.junit.jupiter.api.*;

import java.lang.reflect.Constructor;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for the {@link java.lang.reflect.Constructor} class.
 *
 * @author SlavSquatSuperstar
 */
class ConstructorTest {
    
    private static final String TEST_FILENAME = "src/test/resources/testassets/text/properties.txt";

    @Test
    void newSuperclassInstanceSuccess() throws Exception {
        var inst = instantiateAsset(Asset.class, TEST_FILENAME);
        assertInstanceOf(Asset.class, inst);
        assertEquals(TEST_FILENAME, inst.getFilename());
        assertInstanceOf(ExternalFilePath.class, inst.getFilePath());
    }

    @Test
    void newSubclassInstanceSuccess() throws Exception {
        var inst = instantiateAsset(TextFile.class, TEST_FILENAME);
        assertInstanceOf(TextFile.class, inst);
        assertEquals(TEST_FILENAME, inst.getFilename());
        assertInstanceOf(ExternalFilePath.class, inst.getFilePath());
        assertNotEquals(0, inst.readLines().length);
    }

    @Test
    void newInstanceFailWithWrongArgs() {
        assertThrows(Exception.class, () -> instantiateAsset(Asset.class));
        assertThrows(Exception.class, () -> instantiateAsset(Asset.class, "foo", "bar"));
    }

    private static <T> T instantiateAsset(Class<T> cls, Object... args) throws Exception {
        Constructor<?> ctor = cls.getDeclaredConstructor(String.class);
        return cls.cast(ctor.newInstance(args));
    }

}


