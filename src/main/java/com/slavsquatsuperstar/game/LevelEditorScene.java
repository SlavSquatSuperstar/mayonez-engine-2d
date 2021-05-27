package com.slavsquatsuperstar.game;

import java.awt.Color;

import com.slavsquatsuperstar.mayonez.Assets;
import com.slavsquatsuperstar.mayonez.GameObject;
import com.slavsquatsuperstar.mayonez.Scene;
import com.slavsquatsuperstar.mayonez.SpriteSheet;
import com.slavsquatsuperstar.mayonez.components.BoxCollider;
import com.slavsquatsuperstar.mayonez.components.RigidBody;
import com.slavsquatsuperstar.mayonez.components.Sprite;
import com.slavsquatsuperstar.util.Constants;
import com.slavsquatsuperstar.util.Vector2;

public class LevelEditorScene extends Scene {

	public LevelEditorScene(String name, int width, int height) {
		super(name, width, height);
		background = Color.WHITE;
	}

	@Override
	public void init() {
		GameObject player = new GameObject("Player", new Vector2(200, 200)) {
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
				addComponent(new BoxCollider(Constants.PLAYER_WIDTH, Constants.PLAYER_HEIGHT));
			}
		};
		addObject(player);

		addObject(new GameObject("Test Object 1", new Vector2(0, 0)) {
			@Override
			protected void init() {
				addComponent(Assets.getSprite("assets/mario.png"));
				transform.scale = new Vector2(2, 2);
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
