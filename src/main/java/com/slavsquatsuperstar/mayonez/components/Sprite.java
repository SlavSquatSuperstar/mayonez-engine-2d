package com.slavsquatsuperstar.mayonez.components;

import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;

import javax.imageio.ImageIO;

import com.slavsquatsuperstar.mayonez.Game;
import com.slavsquatsuperstar.mayonez.assets.Assets;
import com.slavsquatsuperstar.mayonez.GameObject;
import com.slavsquatsuperstar.mayonez.Logger;
import com.slavsquatsuperstar.mayonez.Preferences;

/**
 * An image used to display a {@link GameObject} or background.
 * 
 * @author SlavSquatSuperstar
 */
public class Sprite extends Component {

	private static final String TEXTURES_DIRECTORY = "assets/";
	private BufferedImage texture;

	public Sprite(String filename) {
		filename = TEXTURES_DIRECTORY + filename;
		try {
			this.texture = ImageIO.read(Assets.getAsset(filename, true).path);
		} catch (Exception e) {
			Logger.log("Sprite: Error loading image \"%s\"", filename);
			Game.instance().stop(-1);
		}
	}

	public Sprite(BufferedImage image) {
		this.texture = image; // TODO copy rather than reference?
	}

	@Override
	public final void update(float dt) {} // Sprites shouldn't update any game logic

	@Override
	public void render(Graphics2D g2) {
		AffineTransform transform = new AffineTransform();
		transform.setToIdentity();
		transform.translate(parent.transform.position.x, parent.transform.position.y);
		transform.scale(parent.transform.scale.x, parent.transform.scale.y);
		transform.rotate(Math.toRadians(parent.transform.rotation), Preferences.TILE_SIZE / 2f,
				Preferences.TILE_SIZE / 2f);

		g2.drawImage(texture, transform, null);
	}

	public BufferedImage getImage() {
		return texture;
	}

}
