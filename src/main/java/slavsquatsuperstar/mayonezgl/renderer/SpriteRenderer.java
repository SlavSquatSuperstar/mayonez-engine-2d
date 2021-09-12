package slavsquatsuperstar.mayonezgl.renderer;

import org.joml.Vector2f;
import org.joml.Vector4f;
import slavsquatsuperstar.mayonez.Component;

public class SpriteRenderer extends Component {

    private Vector4f color = new Vector4f(1, 1, 1, 1);
    private Vector2f[] texCoords;
    private TextureGL texture = null;

    public SpriteRenderer(Vector4f color) {
        this.color = color;
    }

    public SpriteRenderer(TextureGL sprite) {
        this.texture = sprite;
    }

    public Vector4f getColor() {
        return this.color;
    }

    public TextureGL getTexture() {
        return texture;
    }

    public Vector2f[] getTexCoords() {
        if (texCoords == null)
            texCoords = new Vector2f[]{
                    new Vector2f(1, 1),
                    new Vector2f(1, 0),
                    new Vector2f(0, 0),
                    new Vector2f(0, 1)
            };
        return texCoords;
    }


}
