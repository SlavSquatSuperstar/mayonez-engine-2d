package mayonez;

import java.awt.Color;
import java.awt.Graphics;

public class RectangleSprite extends Component implements Renderable {

	private int width, height;
	private Color color;

	public RectangleSprite(int width, int height, Color color) {
		this.width = width;
		this.height = height;
		this.color = color;
	}

	@Override
	public void render(Graphics g) {
		g.setColor(color);
		int x = (int) parent.getPosition().x;
		int y = (int) parent.getPosition().y;
		g.fillRect(x, y, width, height);
	}

}
