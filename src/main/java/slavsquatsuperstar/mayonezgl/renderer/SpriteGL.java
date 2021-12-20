package slavsquatsuperstar.mayonezgl.renderer;

import org.joml.Vector2f;
import org.joml.Vector4f;
import slavsquatsuperstar.mayonez.Component;
import slavsquatsuperstar.mayonez.GameObject;

public class SpriteGL extends Component { // = Gabe's SpriteRenderer

    private Vector4f color = new Vector4f(1, 1, 1, 1);
    private TextureGL texture = null; // Rendering just a color
    private Vector2f[] texCoords = new Vector2f[]{
            new Vector2f(1, 1),
            new Vector2f(1, 0),
            new Vector2f(0, 0),
            new Vector2f(0, 1)
    };

    public SpriteGL(Vector4f color) {
        this.color = color;
    }

    public SpriteGL(TextureGL texture) {
        this.texture = texture;
    }

    public SpriteGL(TextureGL texture, Vector2f[] texCoords) {
        this.texture = texture;
        this.texCoords = texCoords;
    }

    public Vector4f getColor() {
        return this.color;
    }

    public TextureGL getTexture() {
        return texture;
    }

    public Vector2f[] getTexCoords() {
        return texCoords;
    }

    /**
     * Returns a new SpriteGL with the image as this instance's but not attached to any {@link GameObject}.
     *
     * @return a copy of this image
     */
    public SpriteGL copy() {
        return new SpriteGL(texture, texCoords);
    }

}
