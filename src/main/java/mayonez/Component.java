package mayonez;

import util.Logger;

public abstract class Component {

	protected GameObject parent;
	protected Scene scene;

	public Component(GameObject parent) {
		this.parent = parent;
	}

	public void start() {
		if (null == scene)
			scene = parent.scene;
		Logger.log("%s: Created new component", getClass().getSimpleName());
	}

	public void update() {
	}

}
