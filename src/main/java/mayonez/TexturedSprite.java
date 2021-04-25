package mayonez;

import java.awt.Graphics;

import mayonez.graphics.Renderable;
import mayonez.graphics.Texture;

public class TexturedSprite extends Component implements Renderable {

	// Make a texture sprite component
	private Texture texture;

	public static final String TEXTURES_DIRECTORY = "assets/";

	public TexturedSprite(String path) {
		texture = new Texture(path);
	}

	@Override
	public void render(Graphics g) {
		int x = (int) parent.position.x;// + scene.getCamera().getXOffset();
		int y = (int) parent.position.y;// + scene.getCamera().getYOffset();

		texture.drawImage(g, x, y, parent.getWidth(), parent.getHeight());
	}

}
