package com.slavsquatsuperstar.game;

import java.awt.Color;
import java.awt.Graphics2D;

import com.slavsquatsuperstar.mayonez.Assets;
import com.slavsquatsuperstar.mayonez.GameObject;
import com.slavsquatsuperstar.mayonez.Scene;
import com.slavsquatsuperstar.mayonez.Vector2;
import com.slavsquatsuperstar.util.Constants;

public class LevelEditorScene extends Scene {
	
	public LevelEditorScene(String name) {
		super(name, (int) (Constants.SCREEN_WIDTH * 1.2), (int) (Constants.SCREEN_HEIGHT * 1.5));
		background = Color.WHITE;
	}

	@Override
	public void init() {
		GameObject ground = new GameObject("Ground", new Vector2(0, height - 20)) {
			@Override
			protected void update(float dt) {
				super.update(dt);
				setX(scene.getCamera().getX());
			}

			@Override
			public void render(Graphics2D g2) {
				super.render(g2);
				g2.setColor(Color.BLACK);
				g2.getTransform().getTranslateX();
				g2.fillRect((int) getX() - 10, (int) getY(), Constants.SCREEN_WIDTH + 20, height);
			}
		};
		addObject(ground);

		GameObject player = new Player("Player", new Vector2(100, 100), ground);
		addObject(player);

		addObject(new GameObject("Test Object 1", new Vector2(0, 0)) {
			@Override
			protected void init() {
				addComponent(Assets.getSprite("assets/mario.png"));
				transform.scale = transform.scale.mul(2);
			}
		});
		addObject(new GameObject("Test Object 2", new Vector2(width - 32, height - 32)) {
			@Override
			protected void init() {
				addComponent(Assets.getSprite("assets/goomba.png"));
			}
		});
	}
	
}
