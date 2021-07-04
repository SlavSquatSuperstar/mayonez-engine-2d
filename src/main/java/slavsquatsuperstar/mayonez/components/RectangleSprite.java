package slavsquatsuperstar.mayonez.components;

import java.awt.*;

public class RectangleSprite extends Component {

    private float width, height;
    private Color color;

    public RectangleSprite(float width, float height, Color color) {
        this.width = width;
        this.height = height;
        this.color = color;
    }

    @Override
    public void render(Graphics2D g2) {
        g2.setColor(color);
        g2.setStroke(new BasicStroke(2));
        g2.drawRect((int) (parent.getX() - width / 2f), (int) (parent.getY() - height / 2f), (int) width, (int) height);
    }

}
