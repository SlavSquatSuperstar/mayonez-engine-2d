package com.game;

import java.awt.Color;

import com.mayonez.Assets;
import com.mayonez.GameObject;
import com.mayonez.Scene;
import com.mayonez.SpriteSheet;
import com.mayonez.components.RectangleCollider;
import com.mayonez.components.RigidBody;
import com.mayonez.components.Sprite;
import com.util.Preferences;
import com.util.Vector2;

public class LevelEditorScene extends Scene {

	public LevelEditorScene(String name, int width, int height) {
		super(name, width, height);
		background = Color.BLUE;
	}

	@Override
	public void init() {
		GameObject player = new GameObject("Player", new Vector2(100, 100)) {
			@Override
			protected void init() {
				int id = 19;
				SpriteSheet layer1 = new SpriteSheet("assets/player/layer1.png", 42, 42, 2, 13, 13 * 5);
				SpriteSheet layer2 = new SpriteSheet("assets/player/layer2.png", 42, 42, 2, 13, 13 * 5);
				SpriteSheet layer3 = new SpriteSheet("assets/player/layer3.png", 42, 42, 2, 13, 13 * 5);
				addComponent(new PlayerController(layer1.getSprite(id), layer2.getSprite(id), layer3.getSprite(id),
						Color.RED, Color.GREEN));
				for (Sprite s : getComponent(PlayerController.class).layers)
					addComponent(s);
				addComponent(new RigidBody(2));
				addComponent(new RectangleCollider(Preferences.PLAYER_WIDTH, Preferences.PLAYER_HEIGHT));
			}
		};

		GameObject testObject = new GameObject("Test Object", new Vector2(400, 400)) {
			@Override
			protected void init() {
				addComponent(Assets.getSprite("assets/mario.png"));
				transform.scale = new Vector2(2, 2);
			}
		};

		addObject(player);
		addObject(testObject);
	}

}
