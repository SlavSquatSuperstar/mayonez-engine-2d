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
final class JSprite extends Sprite implements JRenderable {

    private JTexture texture;
    private Color color;

    private JSprite(JTexture texture, Color color) {
        this.texture = texture;
        this.color = color;
    }

    /**
     * Creates a new JSprite that renders an entire texture.
     *
     * @param texture a texture
     */
    JSprite(JTexture texture) {
        this(texture, Sprite.DEFAULT_COLOR);
    }

    /**
     * Creates a new JSprite that only renders a color.
     *
     * @param color a color
     */
    JSprite(Color color) {
        this(null, (color != null) ? color : Sprite.DEFAULT_COLOR);
    }

    // Sprite Methods

    @Override
    public void render(Graphics2D g2) {
        if (texture != null) {
            texture.draw(g2, transform, spriteXf, getScene().getScale());
        } else {
            getScene().getDebugDraw().fillShape(
                    new Rectangle(transform.getPosition(), transform.getScale()), color);
        }
    }

    // Color Helper Methods

    private void recolorImage(Color color) {
        for (var y = 0; y < getImageWidth(); y++) {
            for (var x = 0; x < getImageHeight(); x++) {
                var pixelColor = getPixelColor(x, y);
                var combinedColor = pixelColor.combine(color);
                setPixelColor(x, y, combinedColor);
            }
        }
    }

    /**
     * Get the pixel's RBGA color on this sprite's stored texture at the specific coordinates.
     */
    private Color getPixelColor(int x, int y) {
        if (getImage() == null) return Colors.WHITE;
        else return new Color(getImage().getRGB(x, y));
    }

    /**
     * Set the pixel's RBGA color on this sprite's stored texture at the specific coordinates.
     */
    private void setPixelColor(int x, int y, Color color) {
        if (getImage() == null) return;
        getImage().setRGB(x, y, color.getRGBAValue());
    }

    // Sprite/Renderable Getters

    @Override
    public Color getColor() {
        return color;
    }

    @Override
    public void setColor(Color color) {
        this.color = (color != null) ? color : Colors.WHITE;
        recolorImage(color);
    }

    @Override
    public int getImageWidth() {
        return (getImage() == null) ? 0 : getImage().getWidth();
    }

    @Override
    public int getImageHeight() {
        return (getImage() == null) ? 0 : getImage().getHeight();
    }

    private BufferedImage getImage() {
        return (texture == null) ? null : texture.getImage();
    }

    @Override
    public JTexture getTexture() {
        return texture;
    }

    @Override
    public void setTexture(Texture texture) {
        if (texture instanceof JTexture jTexture) {
            this.texture = jTexture;
        }
    }

    @Override
    public int getZIndex() {
        return gameObject.getZIndex();
    }

    @Override
    public JSprite copy() {
        return new JSprite(texture, color);
    }

}
