package mayonez.graphics.sprites;


import mayonez.Component;
import mayonez.graphics.Color;
import mayonez.graphics.*;
import mayonez.math.*;
import mayonez.math.shapes.Rectangle;
import mayonez.math.shapes.Shape;
import mayonez.physics.colliders.*;

import java.awt.*;

/**
 * Draws a {@link mayonez.physics.colliders.Collider} to the screen.
 *
 * @author SlavSquatSuperstar
 */
public class ShapeSprite extends Component implements JRenderable, GLRenderable {

    private Collider collider;
    private DebugShape shape;

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
        shape = new DebugShape(getColliderShape(), color, fill, getZIndex());
    }

    @Override
    public void update(float dt) {
        shape.setShape(getColliderShape());
    }

    // Renderer Methods

    @Override
    public void render(Graphics2D g2) {
        if (g2 != null) shape.render(g2);
    }

    @Override
    public void pushToBatch(RenderBatch batch) {
        shape.pushToBatch(batch); // doesn't work, need to split shape
    }

    // Renderable Methods

    @Override
    public int getBatchSize() {
        return fill ? RenderBatch.MAX_TRIANGLES : RenderBatch.MAX_LINES;
    }

    @Override
    public DrawPrimitive getPrimitive() {
        return fill ? DrawPrimitive.TRIANGLE : DrawPrimitive.LINE;
    }

    @Override
    public int getZIndex() {
        return gameObject.getZIndex();
    }

    // Shape Methods

    public Shape getColliderShape() { // convert collider to world then to pixels
        if (collider == null) return new Rectangle(new Vec2(0f), new Vec2(1f));
        else return collider.transformToWorld().scale(new Vec2(getScene().getScale()), new Vec2(0f));
    }

}
