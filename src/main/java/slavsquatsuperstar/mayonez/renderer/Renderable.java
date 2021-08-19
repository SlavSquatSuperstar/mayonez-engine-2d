package slavsquatsuperstar.mayonez.renderer;

import java.awt.*;

/**
 * Maps an object to a draw function.
 */
@FunctionalInterface
public interface Renderable {
    void render(Graphics2D g2);
}
