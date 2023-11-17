package mayonez.util;

import org.junit.jupiter.api.*;

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
        var foobar = new FooBar();
        assertEquals("FooBar", StringUtils.getObjectClassName(foobar));
    }

    @Test
    void getClassNameGetsActualClass() {
        Object foobar = new FooBar(); // declare as object
        assertEquals("FooBar", StringUtils.getObjectClassName(foobar));
        assertNotEquals("Object", StringUtils.getObjectClassName(foobar));
    }

    @Test
    void getClassNameFromAnonymousIsParent() {
        var baz = new Baz() {
        };
        assertEquals("Baz", StringUtils.getObjectClassName(baz));
    }

    // Find With Name Tests

    @Test
    void findObjectWithSpaceCorrect() {
        var name = "new jersey";
        assertEquals(State.NEW_JERSEY, StringUtils.findWithName(State.values(), name));
    }

    @Test
    void findObjectNoSpacesCorrect() {
        var name = "virginia";
        assertEquals(State.VIRGINIA, StringUtils.findWithName(State.values(), name));
    }

    @Test
    void findObjectInvalidIsNull() {
        var name = "alaska";
        assertNull(StringUtils.findWithName(State.values(), name));
    }

    private enum State {
        FLORIDA,
        MASSACHUSETTS,
        NEW_JERSEY,
        NEW_YORK,
        TEXAS,
        VIRGINIA,
        WASHINGTON;

        @Override
        public String toString() {
            return StringUtils.capitalizeAllWords(
                    name().replace("_", " ")
            );
        }
    }

    private static class FooBar {
    }

    private static class Baz extends FooBar {
    }

}
