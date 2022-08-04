package slavsquatsuperstar.mayonez.graphics;

import org.joml.Vector2f;
import org.joml.Vector4f;
import slavsquatsuperstar.mayonez.GameObject;
import slavsquatsuperstar.mayonez.annotations.EngineType;
import slavsquatsuperstar.mayonez.annotations.UsesEngine;
import slavsquatsuperstar.mayonez.io.GLTexture;

/**
 * A component that draws an image at a {@link GameObject}'s position. For use the GL engine.
 *
 * @author SlavSquatSuperstar
 */
@UsesEngine(EngineType.GL)
public final class GLSprite extends Sprite { // = Gabe's SpriteRenderer

    private Vector4f color = new Vector4f(1, 1, 1, 1);
    private GLTexture texture = null; // Rendering just a color

    private Vector2f[] texCoords = new Vector2f[]{
            new Vector2f(1, 1),
            new Vector2f(1, 0),
            new Vector2f(0, 0),
            new Vector2f(0, 1)
    };

    public GLSprite(Vector4f color) {
        this.color = color;
    }

    public GLSprite(GLTexture texture) {
        this.texture = texture;
    }

    public GLSprite(GLTexture texture, Vector2f[] texCoords) {
        this.texture = texture;
        this.texCoords = texCoords;
    }

    // Getters and Setters

    public Vector4f getColor() {
        return this.color;
    }

    public void setColor(Vector4f color) {
        this.color = color;
    }

    public GLTexture getTexture() {
        return texture;
    }

    public void setTexture(GLTexture texture) {
        this.texture = texture;
    }

    public Vector2f[] getTexCoords() {
        return texCoords;
    }

    public void setTexCoords(Vector2f[] texCoords) {
        this.texCoords = texCoords;
    }

    @Override
    public GLSprite copy() {
        return new GLSprite(texture, texCoords);
    }

}
