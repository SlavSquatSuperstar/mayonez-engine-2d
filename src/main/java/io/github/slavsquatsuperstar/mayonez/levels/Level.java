package io.github.slavsquatsuperstar.mayonez.levels;

import java.awt.Graphics;
import java.util.ArrayList;
import java.util.Iterator;

import game.Player;
import io.github.slavsquatsuperstar.mayonez.Logger;
import io.github.slavsquatsuperstar.mayonez.objects.GameObject;

public class Level {

	private int width, height;
	// private String name;

	private ArrayList<GameObject> objects;

	public Level(int width, int height) {
		this.width = width;
		this.height = height;
		
		objects = new ArrayList<GameObject>();
		Logger.log("Level loaded");
		objects.add(new Player(200, 200, 32));
	}

	// Source: https://www.baeldung.com/java-concurrentmodificationexception
	public void update() {
		for (Iterator<GameObject> it = objects.iterator(); it.hasNext();) {
			GameObject o = it.next();
			o.update();

			if (o.isDestroyed())
				it.remove();
		}
	}

	public void render(Graphics g) {
		for (GameObject o : objects)
			o.render(g);
	}

	public void addObject(GameObject o) {
		objects.add(o);
	}

	public int getWidth() {
		return width;
	}

	public int getHeight() {
		return height;
	}

	/*
	 * String getName() { return name; }
	 */

}
