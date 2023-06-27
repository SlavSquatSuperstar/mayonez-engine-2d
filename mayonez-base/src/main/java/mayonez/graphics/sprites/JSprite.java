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

    private final JTexture texture;
    private Color color;

    /**
     * Creates a new JSprite that renders an entire texture.
     *
     * @param texture a texture
     */
    JSprite(JTexture texture) {
        this.color = Colors.WHITE;
        this.texture = texture;
    }

    /**
     * Creates a new JSprite that only renders a color.
     *
     * @param color a color
     */
    JSprite(Color color) {
        this.color = (color != null) ? color : Colors.WHITE;
        this.texture = null;
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

    // Sprite/Renderable Methods

    @Override
    public Color getColor() {
        return color;
    }

    @Override
    public void setColor(Color color) {
        this.color = (color != null) ? color : Colors.WHITE;
        recolorImage(color);
    }

    private void recolorImage(Color color) {
        for (var y = 0; y < getImageWidth(); y++) {
            for (var x = 0; x < getImageHeight(); x++) {
                var pixelColor = getPixelColor(x, y);
                var combinedColor = pixelColor.combine(color);
                setPixelColor(x, y, combinedColor);
            }
        }
    }

    private BufferedImage getImage() {
        return (texture == null) ? null : texture.getImage();
    }

    @Override
    public int getImageWidth() {
        return (getImage() == null) ? 0 : getImage().getWidth();
    }

    @Override
    public int getImageHeight() {
        return (getImage() == null) ? 0 : getImage().getHeight();
    }

    /**
     * Get the pixel's RBG color on this sprite's stored texture at the specific coordinates.
     *
     * @param x the pixel x-coordinate
     * @param y the pixel y-coordinate
     * @return the color, or white if drawing a texture
     */
    public Color getPixelColor(int x, int y) {
        if (getImage() == null) return Colors.WHITE;
        else return new Color(getImage().getRGB(x, y));
    }

    /**
     * Set the pixel's RBG color on this sprite's stored texture at the specific coordinates.
     *
     * @param x     the pixel x-coordinate
     * @param y     the pixel y-coordinate
     * @param color the color to set
     */
    public void setPixelColor(int x, int y, Color color) {
        if (getImage() == null) return;
        getImage().setRGB(x, y, color.getRGBAValue());
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
