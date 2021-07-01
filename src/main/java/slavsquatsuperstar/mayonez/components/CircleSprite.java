package slavsquatsuperstar.mayonez.components;

import java.awt.*;

public class CircleSprite extends Component {

    private float radius;
    private Color color;

    public CircleSprite(float radius, Color color) {
        this.radius = radius;
        this.color = color;
    }

    @Override
    public void render(Graphics2D g2) {
        g2.setColor(color);
        g2.drawOval((int) (parent.getX() - radius), (int) (parent.getY() - radius), (int) radius * 2, (int) radius * 2);
    }
}
