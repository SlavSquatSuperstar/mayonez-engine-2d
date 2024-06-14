package mayonez.graphics.ui;

import mayonez.math.*;
import org.junit.jupiter.api.*;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for the {@link UIBounds} class.
 *
 * @author SlavSquatSuperstar
 */
class UIBoundsTest {

    private static final Vec2 POSITION = new Vec2(0, 0);
    private static final Vec2 SIZE = new Vec2(2, 2);

    // Constructor Tests

    @Test
    void centerAnchorPointIsCorrect() {
        var bounds = new UIBounds(POSITION, SIZE, Anchor.CENTER);
        assertEquals(POSITION, bounds.getCenter());
    }

    @Test
    void leftAnchorPointIsCorrect() {
        var bounds = new UIBounds(POSITION, SIZE, Anchor.LEFT);
        assertEquals(new Vec2(1, 0), bounds.getCenter());
    }

    @Test
    void topAnchorPointIsCorrect() {
        var bounds = new UIBounds(POSITION, SIZE, Anchor.TOP);
        assertEquals(new Vec2(0, -1), bounds.getCenter());
    }

    @Test
    void bottomRightAnchorPointIsCorrect() {
        var bounds = new UIBounds(POSITION, SIZE, Anchor.BOTTOM_RIGHT);
        assertEquals(new Vec2(-1, 1), bounds.getCenter());
    }

    // Position Tests

    @Test
    void getPositionIsCorrect() {
        var bounds = new UIBounds(POSITION, SIZE);
        assertEquals(POSITION, bounds.getPosition(Anchor.CENTER));
        assertEquals(new Vec2(-1, 0), bounds.getPosition(Anchor.LEFT));
        assertEquals(new Vec2(0, 1), bounds.getPosition(Anchor.TOP));
        assertEquals(new Vec2(1, -1), bounds.getPosition(Anchor.BOTTOM_RIGHT));
    }

    @Test
    void setPositionCenterIsCorrect() {
        var bounds = new UIBounds(POSITION, SIZE);
        bounds.setPosition(POSITION, Anchor.CENTER);
        assertEquals(POSITION, bounds.getCenter());
    }

    @Test
    void setPositionLeftIsCorrect() {
        var bounds = new UIBounds(POSITION, SIZE);
        bounds.setPosition(POSITION, Anchor.LEFT);
        assertEquals(new Vec2(1, 0), bounds.getCenter());
    }

    @Test
    void setPositionTopIsCorrect() {
        var bounds = new UIBounds(POSITION, SIZE);
        bounds.setPosition(POSITION, Anchor.TOP);
        assertEquals(new Vec2(0, -1), bounds.getCenter());
    }

    @Test
    void setPositionBottomRightIsCorrect() {
        var bounds = new UIBounds(POSITION, SIZE);
        bounds.setPosition(POSITION, Anchor.BOTTOM_RIGHT);
        assertEquals(new Vec2(-1, 1), bounds.getCenter());
    }

}
