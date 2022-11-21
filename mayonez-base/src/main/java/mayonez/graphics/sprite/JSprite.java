package mayonez.graphics.sprite;

import mayonez.DebugDraw;
import mayonez.GameObject;
import mayonez.annotations.EngineType;
import mayonez.annotations.UsesEngine;
import mayonez.io.image.JTexture;
import mayonez.physics.shapes.Rectangle;
import mayonez.util.Color;
import mayonez.util.Colors;

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

    /**
     * Creates a new JSprite that renders an entire texture.
     *
     * @param texture a texture
     */
    JSprite(JTexture texture) {
        super(Colors.WHITE);
        this.texture = texture;
    }

    /**
     * Creates a new JSprite that only renders a color.
     *
     * @param color a color
     */
    JSprite(Color color) {
        super(color);
        texture = null;
    }

    @Override
    public void render(Graphics2D g2) {
        if (texture != null) texture.draw(g2, transform, spriteXf, getScene().getScale());
        else DebugDraw.drawShape(new Rectangle(transform.position, transform.scale), color);
    }

    // Sprite Methods

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
