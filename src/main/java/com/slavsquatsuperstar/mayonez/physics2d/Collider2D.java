package com.slavsquatsuperstar.mayonez.physics2d;

import com.slavsquatsuperstar.mayonez.Logger;
import com.slavsquatsuperstar.mayonez.Vector2;
import com.slavsquatsuperstar.mayonez.components.Component;

public abstract class Collider2D extends Component {

//	protected Vector2 offset = new Vector2();
	protected RigidBody2D rb;

	public abstract boolean contains(Vector2 point);
//	public abstract boolean intersects(Line2D l);

	@Override
	public void start() {
		rb = parent.getComponent(RigidBody2D.class);
		if (rb == null)
			Logger.log("%s needs a RigidBody to function!", getClass().getSimpleName());
	}


}
