package mayonez.util;

import mayonez.*;
import org.junit.jupiter.api.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;

/**
 * Unit tests for the {@link mayonez.util.StringUtils} class.
 *
 * @author SlavSquatSuperstar
 */
public class StringUtilsTests {

    // Array Methods
    @Test
    public void joinArrayToStringSuccess() {
        String[] arr = {"Water", "Earth", "Fire", "Air"};
        var joined = "Water, Earth, Fire, Air";
        assertEquals(joined, StringUtils.join(arr, ", "));
    }

    @Test
    public void splitStringToArraySuccess() {
        var text = "Water, Earth, Fire, Air";
        String[] split = {"Water", "Earth", "Fire", "Air"};
        assertArrayEquals(split, StringUtils.split(text, ", "));
    }

    // Capitalization Tests

    @Test
    public void capitalizeFirstWordAllLetters() {
        var text = "heLLo THerE";
        assertEquals("Hello there", StringUtils.capitalizeFirstWord(text));
    }

    @Test
    public void capitalizeAllWordsAllLetters() {
        var text = "heLLo THerE";
        assertEquals("Hello There", StringUtils.capitalizeAllWords(text));
    }

    @Test
    public void capitalizeWordsWithSymbols() {
        var text = "h3LL0 TH3r3!";
        assertEquals("H3ll0 Th3r3!", StringUtils.capitalizeAllWords(text));
    }

    @Test
    public void capitalizeWordsAllSymbols() {
        var text = "|-|3110 7|-|3|23!";
        assertEquals("|-|3110 7|-|3|23!", StringUtils.capitalizeAllWords(text));
    }

    // Class Name Tests

    @Test
    public void getClassNameFromNull() {
        assertEquals("null", StringUtils.getObjectClassName(null));
    }

    @Test
    public void getClassNameFromNonAnonymous() {
        var obj = mock(GameObject.class);
        assertEquals("GameObject", StringUtils.getObjectClassName(obj));
    }

    @Test
    public void getClassNameFromAnonymous() {
        var scene = new Scene("scene") {
        };
        assertEquals("Scene", StringUtils.getObjectClassName(scene));
    }

}
