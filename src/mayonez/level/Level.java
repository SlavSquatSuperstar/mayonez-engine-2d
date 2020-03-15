package mayonez.level;

import java.awt.Graphics;
import java.util.ArrayList;
import mayonez.object.Object;

public class Level {

	private ArrayList<Object> objects;

	public Level() {
		objects = new ArrayList<Object>();
	}

	public void addObject(Object o) {
		objects.add(o);
	}

	public void update() {
		for (Object o : objects) {
			o.update();
		}
	}

	public void render(Graphics g) {
		for (Object o : objects) {
			o.render(g);
		}

	}

}
