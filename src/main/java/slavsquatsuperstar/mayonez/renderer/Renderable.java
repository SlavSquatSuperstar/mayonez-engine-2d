package slavsquatsuperstar.mayonez.renderer;

import java.awt.*;

/**
 * Maps some object to a draw function.
 */
@FunctionalInterface
interface Renderable {

    void draw(Graphics2D g2);

}
