package slavsquatsuperstar.mayonezgl.renderer;

import org.joml.Vector4f;
import slavsquatsuperstar.mayonez.Component;
import slavsquatsuperstar.mayonez.Logger;

public class SpriteRenderer extends Component {

    private final Vector4f color;

    public SpriteRenderer(Vector4f color) {
        this.color = color;
    }

    public Vector4f getColor() {
        return this.color;
    }

}
