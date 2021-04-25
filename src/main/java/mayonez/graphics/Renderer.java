package mayonez.graphics;

import java.awt.Graphics;
import java.util.LinkedList;

public class Renderer {


	private LinkedList<Renderable> renderables;
	private LinkedList<Renderable> toRemove;

	public Renderer() {
		renderables = new LinkedList<Renderable>();
		toRemove = new LinkedList<Renderable>();
	}

	public void render(Graphics g) {
		for (Renderable r : renderables) {
			r.render(g);
		}

		renderables.removeAll(toRemove);
	}

	public void addObject(Renderable r) {
		renderables.add(r);
	}

	// TODO Maybe check if already in list?
	public void removeObject(Renderable r) {
		toRemove.add(r);
	}

}
