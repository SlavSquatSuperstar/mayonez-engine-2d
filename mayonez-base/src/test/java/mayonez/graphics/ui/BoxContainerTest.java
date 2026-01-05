package mayonez.graphics.ui;

import mayonez.graphics.*;
import mayonez.math.*;
import org.junit.jupiter.api.*;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for {@link mayonez.graphics.ui.BoxContainer} class.
 *
 * @author SlavSquatSuperstar
 */
class BoxContainerTest {

    private BoxContainer container;
    private int spacing;

    @BeforeEach
    void createContainer() {
        spacing = 10;
        container = new BoxContainer(new Vec2(100, 100), spacing, false);
    }

    @Test
    void horizontalSameSizeCorrect() {
        container.setVertical(false);

        var size = new Vec2(50, 50);
        var numElements = 5;
        for (int i = 0; i < numElements; i++) {
            container.addElement(new UISprite(new Vec2(), size, Colors.WHITE));
        }
        container.arrangeElements();

        var offset = size.x + spacing;
        for (int i = 0; i < numElements; i++) {
            var elem = container.getElement(i);
            assertEquals(new Vec2(container.position.x + offset * i, container.position.y), elem.getPosition());
            assertTrue(true);
        }
    }

    @Test
    void verticalSameSizeCorrect() {
        container.setVertical(true);

        var size = new Vec2(50, 50);
        var numElements = 5;
        for (int i = 0; i < numElements; i++) {
            container.addElement(new UISprite(new Vec2(), size, Colors.WHITE));
        }
        container.arrangeElements();

        var offset = size.y + spacing;
        for (int i = 0; i < numElements; i++) {
            var elem = container.getElement(i);
            assertEquals(new Vec2(container.position.x, container.position.y - offset * i), elem.getPosition());
            assertTrue(true);
        }
    }

    @Test
    void horizontalDifferentSizesCorrect() {
        container.setVertical(false);

        var sizes = new Vec2[] {
                new Vec2(80, 50),
                new Vec2(100, 50),
                new Vec2(20, 50),
                new Vec2(40, 50),
                new Vec2(60, 50),
        };
        var numElements = sizes.length;
        for (Vec2 size : sizes) {
            container.addElement(new UISprite(new Vec2(), size, Colors.WHITE));
        }
        container.arrangeElements();

        var positions = new int[] {100, 200, 270, 310, 370};
        for (int i = 0; i < numElements; i++) {
            var elem = container.getElement(i);
            assertEquals(new Vec2(positions[i], container.position.y), elem.getPosition());
            assertTrue(true);
        }
    }

    @Test
    void verticalDifferentSizesCorrect() {
        container.setVertical(true);

        var sizes = new Vec2[] {
                new Vec2(50, 80),
                new Vec2(50, 100),
                new Vec2(50, 20),
                new Vec2(50, 40),
                new Vec2(50, 60),
        };
        var numElements = sizes.length;
        for (Vec2 size : sizes) {
            container.addElement(new UISprite(new Vec2(), size, Colors.WHITE));
        }
        container.arrangeElements();

        var positions = new int[] {100, 0, -70, -110, -170};
        for (int i = 0; i < numElements; i++) {
            var elem = container.getElement(i);
            assertEquals(new Vec2(container.position.x, positions[i]), elem.getPosition());
            assertTrue(true);
        }
    }

}
