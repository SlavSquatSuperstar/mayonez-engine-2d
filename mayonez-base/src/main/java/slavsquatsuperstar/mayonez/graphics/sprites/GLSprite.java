package slavsquatsuperstar.mayonez.graphics.sprites;

import org.joml.Vector4f;
import slavsquatsuperstar.math.Vec2;
import slavsquatsuperstar.mayonez.GameObject;
import slavsquatsuperstar.mayonez.annotations.EngineType;
import slavsquatsuperstar.mayonez.annotations.UsesEngine;
import slavsquatsuperstar.mayonez.io.GLTexture;
import slavsquatsuperstar.mayonez.physics.shapes.Rectangle;

/**
 * A component that draws an image at a {@link GameObject}'s position. For use the GL engine.
 *
 * @author SlavSquatSuperstar
 */
@UsesEngine(EngineType.GL)
public final class GLSprite extends Sprite {

    private Vector4f color = new Vector4f(1, 1, 1, 1); // opaque white
    private GLTexture texture = null; // Rendering just a color

    private Vec2[] texCoords = Rectangle.rectangleVertices(new Vec2(0.5f), new Vec2(1f), 0f);

    public GLSprite(Vector4f color) {
        this.color = color;
    }

    public GLSprite(GLTexture texture) {
        this.texture = texture;
    }

    public GLSprite(GLTexture texture, Vec2[] texCoords) {
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

    public Vec2[] getTexCoords() {
        return texCoords;
    }

    @Override
    public GLSprite copy() {
        return new GLSprite(texture, texCoords);
    }

}
