package com.mayonez.components;

import java.awt.Graphics2D;

import com.mayonez.GameObject;
import com.mayonez.Scene;

// TODO Observable?
public abstract class Component {

	public GameObject parent;
	public Scene scene;

	public void start() {
	}

	public void update(double dt) {
	}

	public void render(Graphics2D g2) {

	}

	public boolean isInScene(Scene scene) {
		return this.scene == scene;
	}

}
