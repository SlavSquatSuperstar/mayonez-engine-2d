package mayonez.graphics.sprite;


import mayonez.Component;
import mayonez.graphics.Color;
import mayonez.graphics.renderable.JRenderable;
import mayonez.graphics.renderer.DebugShape;
import mayonez.math.Vec2;
import mayonez.physics.colliders.Collider;
import mayonez.physics.shapes.Shape;

import java.awt.*;

/**
 * Draws a {@link mayonez.physics.colliders.Collider} to the screen.
 *
 * @author SlavSquatSuperstar
 */
public class ShapeSprite extends Component implements JRenderable {

    private Collider collider;
    public Color color;
    public boolean fill;

    public ShapeSprite(Color color, boolean fill) {
        this.color = color;
        this.fill = fill;
    }

    @Override
    public void start() {
        collider = gameObject.getComponent(Collider.class);
        if (collider == null) this.setEnabled(false);
    }

    @Override
    public void render(Graphics2D g2) {
        if (g2 == null) return;
        new DebugShape(this).render(g2);
    }

    public Shape getShape() { // convert collider to world then to pixels
        return collider.transformToWorld().scale(new Vec2(getScene().getScale()), new Vec2(0f));
    }

    @Override
    public int getZIndex() {
        return gameObject.getZIndex();
    }

}