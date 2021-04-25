package mayonez;

// TODO Observable?
public abstract class Component {

	protected GameObject parent;
	protected Scene scene;

	public void start() {
	}

	public void update() {
	}

	public boolean isInScene(Scene scene) {
		return this.scene == scene;
	}

}
