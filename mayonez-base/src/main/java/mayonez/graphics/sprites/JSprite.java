package mayonez.graphics.sprites;

import mayonez.*;
import mayonez.annotations.*;
import mayonez.graphics.Color;
import mayonez.graphics.*;
import mayonez.graphics.textures.*;
import mayonez.math.shapes.Rectangle;
import mayonez.util.*;

import java.awt.*;
import java.awt.image.*;

/**
 * A component that draws an image at a {@link GameObject}'s position using the AWT engine.
 *
 * @author SlavSquatSuperstar
 */
@UsesEngine(EngineType.AWT)
public final class JSprite extends Sprite implements JRenderable {

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

    // Sprite Methods

    @Override
    public void render(Graphics2D g2) {
        if (texture != null) {
            texture.draw(g2, transform, spriteXf, getScene().getScale());
        } else {
            getScene().getDebugDraw().fillShape(new Rectangle(transform.getPosition(), transform.getScale()), color);
        }
    }

    // Sprite Methods

    public BufferedImage getImage() {
        return (texture == null) ? null : texture.getImage();
    }

    @Override
    public JTexture getTexture() {
        return texture;
    }

    @Override
    public int getZIndex() {
        return gameObject.getZIndex();
    }

    @Override
    public JSprite copy() {
        return (texture == null) ? new JSprite(color) : new JSprite(texture);
    }

}
