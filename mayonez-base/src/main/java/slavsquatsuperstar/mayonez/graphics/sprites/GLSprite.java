package slavsquatsuperstar.mayonez.graphics.sprites;

import org.joml.Vector4f;
import slavsquatsuperstar.math.Vec2;
import slavsquatsuperstar.mayonez.GameObject;
import slavsquatsuperstar.mayonez.annotations.EngineType;
import slavsquatsuperstar.mayonez.annotations.UsesEngine;
import slavsquatsuperstar.mayonez.io.GLTexture;
import slavsquatsuperstar.util.Color;

/**
 * A component that draws an image at a {@link GameObject}'s position using the GL engine.
 *
 * @author SlavSquatSuperstar
 */
@UsesEngine(EngineType.GL)
public final class GLSprite extends Sprite {

    private final GLTexture texture;
    private final Color color;

    /**
     * Create a new GLSprite that renders an entire texture.
     *
     * @param texture a texture
     */
    GLSprite(GLTexture texture) {
        this.texture = texture;
        color = new Color(new Vector4f(255f)); // Opaque white
    }

    /**
     * Create a new GLSprite that only renders a color.
     *
     * @param color a color
     */
    GLSprite(Color color) {
        this.color = color;
        texture = null;
    }

    // Getters and Setters

    public Color getColor() {
        return this.color;
    }

    @Override
    public GLTexture getTexture() {
        return texture;
    }

    public Vec2[] getTexCoords() {
        return (texture == null) ? GLTexture.DEFAULT_TEX_COORDS : texture.getTexCoords();
    }

    @Override
    public GLSprite copy() {
        return (texture == null) ? new GLSprite(color) : new GLSprite(texture);
    }

}
