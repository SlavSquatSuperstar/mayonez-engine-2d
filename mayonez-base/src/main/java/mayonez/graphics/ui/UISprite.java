package mayonez.graphics.ui;

import mayonez.*;
import mayonez.graphics.*;
import mayonez.graphics.textures.*;
import mayonez.math.*;
import mayonez.math.shapes.*;
import mayonez.renderer.gl.*;

/**
 * A visible user interface element that draws a rectangular sprite texture and color to the UI.
 *
 * @author SlavSquatSuperstar
 */
@UsesEngine(EngineType.GL)
public class UISprite extends Component implements UIElement, GLQuad {

    // Constants
    private static final Color DEFAULT_COLOR = Colors.WHITE;

    // Instance Fields
    private UIBounds bounds;
    private Texture texture;
    private Color color;

    private UISprite(Vec2 position, Vec2 size, Texture texture, Color color) {
        super(UpdateOrder.RENDER);
        bounds = new UIBounds(position, size);
        this.texture = texture;
        this.color = color;
    }

    public UISprite(Vec2 position, Vec2 size, Texture texture) {
        this(position, size, texture, DEFAULT_COLOR);
    }

    public UISprite(Vec2 position, Vec2 size, Color color) {
        this(position, size, null, color);
    }

    // UI Bounds Methods

    @Override
    public Vec2 getPosition() {
        return bounds.getAnchorPos();
    }

    @Override
    public void setPosition(Vec2 position) {
        bounds.setAnchorPos(position);
    }

    @Override
    public Vec2 getSize() {
        return bounds.getSize();
    }

    @Override
    public void setSize(Vec2 size) {
        bounds.setSize(size);
    }

    @Override
    public Anchor getAnchor() {
        return bounds.getAnchorDir();
    }

    @Override
    public void setAnchor(Anchor anchor) {
        bounds.setAnchorDir(anchor);
    }

    public UIBounds getBounds() {
        return bounds;
    }

    public void setBounds(UIBounds bounds) {
        this.bounds = bounds;
    }

    // UI Renderable Methods

    /**
     * Get the color of this UI element.
     *
     * @return the color
     */
    @Override
    public Color getColor() {
        return color;
    }

    /**
     * Set the color of this UI element.
     *
     * @param color the color
     */
    public void setColor(Color color) {
        this.color = color;
    }

    /**
     * Get the texture of this UI element.
     *
     * @return the texture
     */
    @Override
    public GLTexture getTexture() {
        return (texture instanceof GLTexture glTex) ? glTex : null;
    }

    /**
     * Set the texture of this UI element.
     *
     * @param texture the texture
     */
    public void setTexture(Texture texture) {
        this.texture = texture;
    }

    // Renderable Methods

    @Override
    public Vec2[] getVertexPositions() {
        return new BoundingBox(bounds.getCenter(), bounds.getSize()).getVertices();
    }

    @Override
    public int getZIndex() {
        return gameObject.getZIndex();
    }

    @Override
    public boolean isInUI() {
        return true;
    }
}
