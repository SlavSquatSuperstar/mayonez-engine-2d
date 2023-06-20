package mayonez.util;

import mayonez.*;
import mayonez.input.*;
import org.junit.jupiter.api.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;

/**
 * Unit tests for the {@link mayonez.util.StringUtils} class.
 *
 * @author SlavSquatSuperstar
 */
class StringUtilsTest {

    // Array Methods
    @Test
    void joinArrayToStringSuccess() {
        String[] arr = {"Water", "Earth", "Fire", "Air"};
        var joined = "Water, Earth, Fire, Air";
        assertEquals(joined, StringUtils.join(arr, ", "));
    }

    @Test
    void splitStringToArraySuccess() {
        var text = "Water, Earth, Fire, Air";
        String[] split = {"Water", "Earth", "Fire", "Air"};
        assertArrayEquals(split, StringUtils.split(text, ", "));
    }

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
        var obj = mock(GameObject.class);
        assertEquals("GameObject", StringUtils.getObjectClassName(obj));
    }

    @Test
    void getClassNameFromAnonymousIsParent() {
        var scene = new Scene("scene") {
        };
        assertEquals("Scene", StringUtils.getObjectClassName(scene));
    }

    // Find Enum Tests

    @Test
    void findEnumConstantWithSpaceCorrect() {
        var name = "left shift";
        assertEquals(Key.LEFT_SHIFT, StringUtils.findConstantWithName(Key.values(), name));
    }

    @Test
    void findEnumConstantNoSpacesCorrect() {
        var name = "space";
        assertEquals(Key.SPACE, StringUtils.findConstantWithName(Key.values(), name));
    }

    @Test
    void findEnumConstantInvalidIsNull() {
        var name = "joystick";
        assertNull(StringUtils.findConstantWithName(Key.values(), name));
    }

}
