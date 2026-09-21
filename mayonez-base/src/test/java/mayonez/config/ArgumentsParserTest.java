package mayonez.config;

import mayonez.application.Backend;
import mayonez.util.Record;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for the {@link ArgumentsParser} class.
 *
 * @author SlavSquatSuperstar
 */
class ArgumentsParserTest {

    private ArgumentsParser parser;

    @BeforeEach
    void createLauncher() {
        parser = new ArgumentsParser();
    }

    // One-Argument Options

    @Test
    void longOptionCorrect() {
        try {
            var cl = parser.parse(new String[]{"--backend", "gl"});
            var engine = cl.getOptionValue("b");
            assertEquals("gl", engine);
            assertEquals(Backend.GL, parser.getBackend(cl));

            var rec = parser.serialize(cl);
            assertEquals(new Record(Map.of("backend", "gl")), rec);
        } catch (Exception e) {
            fail();
        }
    }

    @Test
    void shortOptionCorrect() {
        try {
            var cl = parser.parse(new String[]{"-b", "awt"});
            var engine = cl.getOptionValue("b");
            assertEquals("awt", engine);
            assertEquals(Backend.AWT, parser.getBackend(cl));

            var rec = parser.serialize(cl);
            assertEquals(new Record(Map.of("backend", "awt")), rec);
        } catch (Exception e) {
            fail();
        }
    }

    @Test
    void blankOptionCorrect() {
        try {
            var cl = parser.parse(new String[]{});
            assertFalse(cl.hasOption("backend"));
            assertEquals(Backend.DEFAULT, parser.getBackend(cl));

            var rec = parser.serialize(cl);
            assertEquals(new Record(), rec);
        } catch (Exception e) {
            fail();
        }
    }

    // Wrong Arguments

    @Test
    void invalidArgumentWrong() {
        try {
            var cl = parser.parse(new String[]{"--backend", "vk"});
            assertThrows(IllegalArgumentException.class, () -> parser.getBackend(cl));
        } catch (Exception e) {
            fail();
        }
    }

    @Test
    void missingArgumentWrong() {
        assertThrows(RuntimeException.class, () -> parser.parse(new String[]{"--backend"}));
    }

}