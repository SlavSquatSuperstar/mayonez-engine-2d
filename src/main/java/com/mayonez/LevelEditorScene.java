package com.mayonez;

import java.awt.Color;

import com.util.Vector2;

public class LevelEditorScene extends Scene {

	GameObject player;

	public LevelEditorScene(String name, int width, int height) {
		super(name, width, height);
		background = Color.BLUE;
	}

	@Override
	public void init() {
		player = new GameObject("Test Object", new Transform(Vector2.ONE.scale(100), Vector2.ONE.scale(64))) {
			@Override
			protected void init() {
//				addComponent(Assets.getSprite("assets/mario.png"));
				SpriteSheet spritesheet = new SpriteSheet("assets/player/layer1.png", 42, 42, 2, 13, 13 * 5);
				addComponent(spritesheet.getSprite(20));
//				addComponent(new RectangleCollider((int) transform.scale.x, (int) transform.scale.y));
//				addComponent(new RigidBody(2));
//				addComponent(new PlayerController());
//				addComponent(new BoxBounds());
			}
		};
		addObject(player);
	}

}
