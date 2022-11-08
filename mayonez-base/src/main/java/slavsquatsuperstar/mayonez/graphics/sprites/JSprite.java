package slavsquatsuperstar.mayonez.graphics.sprites;

import slavsquatsuperstar.mayonez.DebugDraw;
import slavsquatsuperstar.mayonez.GameObject;
import slavsquatsuperstar.mayonez.Transform;
import slavsquatsuperstar.mayonez.annotations.EngineType;
import slavsquatsuperstar.mayonez.annotations.UsesEngine;
import slavsquatsuperstar.mayonez.io.JTexture;
import slavsquatsuperstar.mayonez.physics.shapes.Rectangle;
import slavsquatsuperstar.util.Color;
import slavsquatsuperstar.util.Colors;

import java.awt.*;
import java.awt.image.BufferedImage;

/**
 * A component that draws an image at a {@link GameObject}'s position using the AWT engine.
 *
 * @author SlavSquatSuperstar
 */
@UsesEngine(EngineType.AWT)
public final class JSprite extends Sprite {

    private final JTexture texture;
    private final Color color;

    /**
     * Creates a new JSprite that renders an entire texture.
     *
     * @param texture a texture
     */
    JSprite(JTexture texture) {
        this.texture = texture;
        color = new Color(Colors.WHITE);
    }

    /**
     * Creates a new JSprite that only renders a color.
     *
     * @param color a color
     */
    JSprite(Color color) {
        this.color = color;
        texture = null;
    }

    @Override
    public void render(Graphics2D g2) {
        if (texture != null) texture.draw(g2, transform, new Transform(), getScene().getScale());
        else DebugDraw.drawShape(new Rectangle(transform.position, transform.scale), color.toAWT());
    }

    // Sprite Methods

    @Override
    public Color getColor() {
        return color;
    }

    @Override
    public JTexture getTexture() {
        return texture;
    }

    public BufferedImage getImage() {
        return texture.getImage();
    }

    @Override
    public JSprite copy() {
        return (texture == null) ? new JSprite(color) : new JSprite(texture);
    }

}
