package slavsquatsuperstar.mayonez.physics2d.colliders;

import slavsquatsuperstar.math.MathUtils;
import slavsquatsuperstar.math.Vec2;

// TODO scale with transform

/**
 * An axis-aligned bounding box (AABB), a rectangle that is never rotated. The sides will always align with the x and y
 * axes.
 *
 * @author SlavSquatSuperstar
 */
public class AlignedBoxCollider2D extends BoxCollider2D {

    // Need to lock rotation somehow

    public AlignedBoxCollider2D(Vec2 size) {
        super(size);
    }

    // Shape Properties

    // min in world space
    public Vec2 min() {
        return center().sub(size().div(2));
    }

    // max in world space
    public Vec2 max() {
        return center().add(size().div(2));
    }

    @Override
    public float getRotation() {
        return 0f;
    }

    @Override
    public Vec2[] getVertices() {
        return new Vec2[]{
                new Vec2(min()), new Vec2(min().x, max().y), new Vec2(max()), new Vec2(max().x, min().y)
        };
    }

    @Override
    public Vec2[] getNormals() {
        return new Vec2[]{new Vec2(1, 0), new Vec2(0, 1)};
    }

    @Override
    public AlignedBoxCollider2D getMinBounds() {
        return new AlignedBoxCollider2D(localSize()).setTransform(transform).setRigidBody(rb);
    }

    @Override
    public Vec2 toLocal(Vec2 world) {
        return world.sub(center()).div(transform.scale);
    }

    @Override
    public Vec2 toWorld(Vec2 local) {
        return local.mul(transform.scale).add(center());
    }

    // AABB vs Point

    @Override
    public boolean contains(Vec2 point) {
        return MathUtils.inRange(point.x, min().x, max().x) && MathUtils.inRange(point.y, min().y, max().y);
    }

    @Override
    public Vec2 nearestPoint(Vec2 position) {
        if (contains(position))
            return position;
        return position.clampInbounds(min(), max());
    }

}
