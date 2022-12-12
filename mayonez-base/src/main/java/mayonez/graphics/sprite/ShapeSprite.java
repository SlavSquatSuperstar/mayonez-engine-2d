package mayonez.graphics.sprite;


import mayonez.Component;
import mayonez.DebugDraw;
import mayonez.graphics.Color;
import mayonez.graphics.Renderable;
import mayonez.physics.colliders.Collider;

import java.awt.*;

/**
 * Draws a {@link mayonez.physics.colliders.Collider} to the screen.
 *
 * @author SlavSquatSuperstar
 */
public class ShapeSprite extends Component implements Renderable {

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
        // needs to be converted to pixels
//        DebugShape shape = new DebugShape(collider.transformToWorld(), color, fill, getZIndex());
//        shape.render(g2);
        if (fill) DebugDraw.fillShape(collider.transformToWorld(), color);
        else DebugDraw.drawShape(collider.transformToWorld(), color);
    }

    @Override
    public int getZIndex() {
        return gameObject.getZIndex();
    }
}
