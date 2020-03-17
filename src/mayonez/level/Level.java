package mayonez.level;

import java.awt.Graphics;
import java.util.ArrayList;

import lib.math.Vector;
import mayonez.object.Mobile;
import mayonez.object.Object;

public class Level {

	private ArrayList<Object> objects;
	private Vector force;

	public Level() {
		objects = new ArrayList<Object>();
		force = new Vector();
	}

	public void addObject(Object o) {
		objects.add(o);
	}

	public void update() {
		for (Object o : objects) {

			if (o instanceof Mobile) {
				((Mobile) o).applyForce(force);
			}

			o.update();
		}
	}

	public void render(Graphics g) {
		for (Object o : objects) {
			o.render(g);
		}

	}

	public void applyForce(double fx, double fy) {
		force = new Vector(fx, fy);
	}

}
