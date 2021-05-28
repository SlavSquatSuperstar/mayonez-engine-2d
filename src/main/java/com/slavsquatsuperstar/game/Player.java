package com.slavsquatsuperstar.game;

import java.awt.Color;

import com.slavsquatsuperstar.mayonez.GameObject;
import com.slavsquatsuperstar.mayonez.SpriteSheet;
import com.slavsquatsuperstar.mayonez.Vector2;
import com.slavsquatsuperstar.mayonez.components.Sprite;
import com.slavsquatsuperstar.mayonez.physics2d.AlignedBoxCollider2D;
import com.slavsquatsuperstar.mayonez.physics2d.RigidBody2D;
import com.slavsquatsuperstar.util.Constants;

public class Player extends GameObject {

	private GameObject ground;

	public Player(String name, Vector2 position, GameObject ground) {
		super(name, position);
		this.ground = ground;
	}

	@Override
	protected void init() {
		// Create player avatar
		SpriteSheet layer1 = new SpriteSheet("assets/player/layer1.png", 42, 42, 2, 13, 13 * 5);
		SpriteSheet layer2 = new SpriteSheet("assets/player/layer2.png", 42, 42, 2, 13, 13 * 5);
		SpriteSheet layer3 = new SpriteSheet("assets/player/layer3.png", 42, 42, 2, 13, 13 * 5);

		int id = 19;
		int threshold = 200;
		
		Sprite[] layers = new Sprite[] { layer1.getSprite(id), layer2.getSprite(id), layer3.getSprite(id) };
		Color[] colors = { Color.RED, Color.GREEN };

		// Create sprite layers
		for (int i = 0; i < colors.length; i++) {
			Sprite l = layers[i];
			for (int y = 0; y < l.getImage().getWidth(); y++) {
				for (int x = 0; x < l.getImage().getHeight(); x++) {
					Color color = new Color(l.getImage().getRGB(x, y));
					if (color.getRed() > threshold && color.getGreen() > threshold && color.getBlue() > threshold)
						l.getImage().setRGB(x, y, colors[i].getRGB());
				}
			}
		}
		for (Sprite s : layers)
			addComponent(s);

		// Add player script
		PlayerController pc = new PlayerController(ground);
		addComponent(pc);
		addComponent(new AlignedBoxCollider2D(new Vector2(Constants.PLAYER_WIDTH, Constants.PLAYER_HEIGHT)));
		addComponent(new RigidBody2D(2, true));
	}

}
