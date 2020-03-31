package slavsquatstudio.mayonez.engine.level;

import java.awt.Graphics;
import java.util.ArrayList;

import slavsquatstudio.lib.math.Vector;
import slavsquatstudio.mayonez.engine.object.GameObject;
import slavsquatstudio.mayonez.engine.object.Mobile;

public class Level {

	private ArrayList<GameObject> objects;
	private Vector force;

	public Level() {
		objects = new ArrayList<GameObject>();
		force = new Vector();
	}

	public void addObject(GameObject o) {
		objects.add(o);
	}

	public void update() {
		for (GameObject o : objects) {

			if (o instanceof Mobile) {
				((Mobile) o).applyForce(force);
			}

			o.update();
		}
	}

	public void render(Graphics g) {
		for (GameObject o : objects) {
			o.render(g);
		}

	}

	public void applyForce(double fx, double fy) {
		force = new Vector(fx, fy);
	}

}
