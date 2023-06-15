package mayonez;

import org.junit.jupiter.api.*;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for the {@link mayonez.Component} class.
 *
 * @author SlavSquatSuperstar
 */
class ComponentTest {

    @Test
    void componentIDsAreUnique() {
        var comp1 = new Component() {
        };
        var comp2 = new Component() {
        };
        assertNotEquals(comp1.componentID, comp2.componentID);
    }

}
