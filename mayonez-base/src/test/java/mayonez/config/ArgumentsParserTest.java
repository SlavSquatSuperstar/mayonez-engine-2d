package mayonez.config;

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

    @Test
    void longOptionCorrect() {
        try {
            var cl = parser.parse(new String[]{"--engine", "gl"});
            var engine = cl.getOptionValue("e");
            assertEquals("gl", engine);
            assertNotNull(parser.getRunConfig(cl));

            var rec = parser.serialize(cl);
            assertEquals(new Record(Map.of("e", "gl")), rec);
        } catch (Exception e) {
            fail();
        }
    }

    @Test
    void shortOptionCorrect() {
        try {
            var cl = parser.parse(new String[]{"-e", "awt"});
            var engine = cl.getOptionValue("e");
            assertEquals("awt", engine);
            assertNotNull(parser.getRunConfig(cl));

            var rec = parser.serialize(cl);
            assertEquals(new Record(Map.of("e", "awt")), rec);
        } catch (Exception e) {
            fail();
        }
    }

    @Test
    void blankOptionCorrect() {
        try {
            var cl = parser.parse(new String[]{});
            assertFalse(cl.hasOption("engine"));
            assertNotNull(parser.getRunConfig(cl));

            var rec = parser.serialize(cl);
            assertEquals(new Record(), rec);
        } catch (Exception e) {
            fail();
        }
    }

    @Test
    void invalidArgumentWrong() {
        try {
            var cl = parser.parse(new String[]{"--engine", "vk"});
            assertThrows(IllegalArgumentException.class, () -> parser.getRunConfig(cl));
        } catch (Exception e) {
            fail();
        }
    }

    @Test
    void missingArgumentWrong() {
        assertThrows(RuntimeException.class, () -> parser.parse(new String[]{"--engine"}));
    }

}