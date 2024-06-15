package mayonez.graphics.ui;

import mayonez.*;
import mayonez.graphics.*;
import mayonez.graphics.textures.*;
import mayonez.math.*;
import mayonez.math.shapes.*;
import mayonez.renderer.batch.*;

/**
 * A rectangular sprite with a texture and color that is drawn to the UI.
 *
 * @author SlavSquatSuperstar
 */
@UsesEngine(EngineType.GL)
public class UISprite extends Component implements UIRenderableElement {

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

    // UI Bounds Getters and Setters

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

    // UI Renderable Getters and Setters

    @Override
    public Color getColor() {
        return color;
    }

    @Override
    public void setColor(Color color) {
        this.color = color;
    }

    // TODO better encapsulate
    @Override
    public GLTexture getTexture() {
        if (texture instanceof GLTexture glTexture) {
            return glTexture;
        } else {
            return null;
        }
    }

    @Override
    public void setTexture(Texture texture) {
        this.texture = texture;
    }

    // Renderer Methods

    @Override
    public void pushToBatch(RenderBatch batch) {
        var texID = batch.getTextureSlot((GLTexture) texture);
        var texCoords = (texture != null)
                ? ((GLTexture) texture).getTexCoords()
                : GLTexture.DEFAULT_TEX_COORDS;
        var sprVertices = new BoundingBox(
                bounds.getCenter(), bounds.getSize()
        ).getVertices();
        BatchPushHelper.pushTexture(batch, sprVertices, color, texCoords, texID);
    }

    @Override
    public int getBatchSize() {
        return RenderBatch.MAX_SPRITES;
    }

    @Override
    public DrawPrimitive getPrimitive() {
        return DrawPrimitive.SPRITE;
    }

    @Override
    public int getZIndex() {
        return gameObject.getZIndex();
    }

}
