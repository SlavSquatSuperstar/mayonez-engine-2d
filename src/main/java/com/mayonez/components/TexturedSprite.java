package com.mayonez.components;

import java.awt.Graphics2D;

import com.mayonez.Texture;

public class TexturedSprite extends Component {

	// Make a texture sprite component
	private Texture texture;

	public static final String TEXTURES_DIRECTORY = "assets/";

	public TexturedSprite(String path) {
		texture = new Texture(path);
	}

	@Override
	public void render(Graphics2D g2) {
		int x = (int) parent.transform.position.x;// + scene.getCamera().getXOffset();
		int y = (int) parent.transform.position.y;// + scene.getCamera().getYOffset();

		texture.drawImage(g2, x, y, (int) parent.transform.scale.x, (int) parent.transform.scale.y);
	}

}
