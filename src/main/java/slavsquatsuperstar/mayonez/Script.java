package slavsquatsuperstar.mayonez;

import slavsquatsuperstar.mayonez.components.Component;

import java.awt.*;

public abstract class Script extends Component {

    /**
     * A reference to the parent object's {@link Transform}.
     */
    protected Transform transform;

    @Override
    public final void render(Graphics2D g2) {} // Scripts shouldn't render any images

    @Override
    public void setParent(GameObject parent) {
        super.setParent(parent);
        transform = parent.transform;
    }
}
