package slavsquatsuperstar.demos.mario;

import mayonez.GameObject;
import mayonez.Transform;
import mayonez.graphics.Color;
import mayonez.graphics.sprites.Sprite;
import mayonez.math.Vec2;

/**
 * A translucent colored square.
 *
 * @author SlavSquatSuperstar
 */
class ColoredSquare extends GameObject {

    private final Color color;

    public ColoredSquare(String name, Vec2 position, Color color, int zIndex) {
        super(name, new Transform(position, 0, new Vec2(4f)), zIndex);
        this.color = color;
    }

    @Override
    protected void init() {
        addComponent(Sprite.create(color));
    }
}
