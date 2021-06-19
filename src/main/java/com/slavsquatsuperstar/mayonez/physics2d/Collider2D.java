package com.slavsquatsuperstar.mayonez.physics2d;

import com.slavsquatsuperstar.mayonez.Logger;
import com.slavsquatsuperstar.mayonez.Transform;
import com.slavsquatsuperstar.mayonez.Vector2;
import com.slavsquatsuperstar.mayonez.components.Component;

public abstract class Collider2D extends Component {

    protected RigidBody2D rb = null;
    protected Transform transform;

    public Vector2 center() {
        return transform.position;
    }

    public abstract boolean contains(Vector2 point);

    public abstract boolean intersects(Line2D l);

    @Override
    public void start() {
        this.transform = parent.transform;
        if (parent.shouldFollowPhysics()) {
            rb = parent.getComponent(RigidBody2D.class);
            if (rb == null)
                Logger.log("%s needs a RigidBody to function!", getClass().getSimpleName());
        }
    }

    public void setRigidBody(RigidBody2D rb) {
        this.rb = rb;
    }

    public void setTransform(Transform transform) {
        this.transform = transform;
    }

}
