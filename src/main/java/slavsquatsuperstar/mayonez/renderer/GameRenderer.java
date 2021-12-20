package slavsquatsuperstar.mayonez.renderer;

import slavsquatsuperstar.mayonez.GameObject;
import slavsquatsuperstar.mayonez.Scene;

import java.awt.*;

public interface GameRenderer {

    void render(Graphics2D g2);

    /**
     * Submit a {@link GameObject} for rendering.
     *
     * @param o the game object
     */
    void addObject(GameObject o);

    void removeObject(GameObject o);

    void setScene(Scene newScene);

}
