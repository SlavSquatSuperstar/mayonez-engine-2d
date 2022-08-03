package slavsquatsuperstar.mayonez.graphics;

import slavsquatsuperstar.mayonez.*;
import slavsquatsuperstar.mayonez.annotations.EngineType;
import slavsquatsuperstar.mayonez.annotations.UsesEngine;
import slavsquatsuperstar.mayonez.io.Assets;
import slavsquatsuperstar.mayonez.io.JTexture;
import slavsquatsuperstar.mayonez.physics.shapes.Rectangle;

import java.awt.*;
import java.awt.image.BufferedImage;

/**
 * A component that draws an image at a {@link GameObject}'s position. For use the AWT engine.
 *
 * @author SlavSquatSuperstar
 */
@UsesEngine(EngineType.AWT)
public final class JSprite extends Sprite {

    private final JTexture texture; // TODO missing texture image?

    public JSprite(String filename) {
        texture = Assets.getJTexture(filename);
        if (texture == null) {
            Logger.error("I/O: Error loading image file \"%s\"", filename);
            Mayonez.stop(-1);
        }
    }

    public JSprite(JTexture texture) {
        this.texture = texture;
    }

    @Override
    public void render(Graphics2D g2) {
        if (texture != null) texture.draw(g2, transform, new Transform(), getScene().getCellSize());
        else DebugDraw.drawShape(new Rectangle(transform.position, transform.scale), Colors.MAGENTA);
    }

    public BufferedImage getImage() {
        return texture.getImage();
    }

    @Override
    public JSprite copy() {
        return new JSprite(texture);
    }

}
