package mayonez.util;

import mayonez.*;
import mayonez.input.*;
import org.junit.jupiter.api.*;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for the {@link mayonez.util.StringUtils} class.
 *
 * @author SlavSquatSuperstar
 */
class StringUtilsTest {

    // Capitalization Tests

    @Test
    void capitalizeFirstWordAllLetters() {
        var text = "heLLo THerE";
        assertEquals("Hello there", StringUtils.capitalizeFirstWord(text));
    }

    @Test
    void capitalizeAllWordsAllLetters() {
        var text = "heLLo THerE";
        assertEquals("Hello There", StringUtils.capitalizeAllWords(text));
    }

    @Test
    void capitalizeWordsWithSymbols() {
        var text = "h3LL0 TH3r3!";
        assertEquals("H3ll0 Th3r3!", StringUtils.capitalizeAllWords(text));
    }

    @Test
    void capitalizeWordsAllSymbols() {
        var text = "|-|3110 7|-|3|23!";
        assertEquals("|-|3110 7|-|3|23!", StringUtils.capitalizeAllWords(text));
    }

    // Class Name Tests

    @Test
    void getClassNameFromNullIsNull() {
        assertEquals("null", StringUtils.getObjectClassName(null));
    }

    @Test
    void getClassNameNonAnonymousIsClassName() {
        var obj = new GameObject("");
        assertEquals("GameObject", StringUtils.getObjectClassName(obj));
    }

    @Test
    void getClassNameFromAnonymousIsParent() {
        var scene = new Scene("scene") {
        };
        assertEquals("Scene", StringUtils.getObjectClassName(scene));
    }

    // Find With Name Tests

    @Test
    void findObjectWithSpaceCorrect() {
        var keys = new Key[] {Key.LEFT_SHIFT, Key.RIGHT_SHIFT, Key.LEFT, Key.RIGHT, Key.SPACE};
        var name = "left shift";
        assertEquals(Key.LEFT_SHIFT, StringUtils.findWithName(Arrays.asList(keys), name));
    }

    @Test
    void findObjectNoSpacesCorrect() {
        var name = "space";
        assertEquals(Key.SPACE, StringUtils.findWithName(Key.getEntries(), name));
    }

    @Test
    void findObjectInvalidIsNull() {
        var name = "joystick";
        assertNull(StringUtils.findWithName(Key.getEntries(), name));
    }

}
