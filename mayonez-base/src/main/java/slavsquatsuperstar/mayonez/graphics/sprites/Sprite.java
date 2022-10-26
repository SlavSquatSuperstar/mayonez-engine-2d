package slavsquatsuperstar.mayonez.graphics.sprites;

import slavsquatsuperstar.mayonez.Component;
import slavsquatsuperstar.mayonez.GameObject;

/**
 * A visual representation of a GameObject.
 *
 * @author SlavSquatSuperstar
 */
public abstract sealed class Sprite extends Component permits GLSprite, JSprite {

    /**
     * Returns a new sprite with the same image but not attached to any {@link GameObject}.
     *
     * @return a copy of this image
     */
    public abstract Sprite copy();

}