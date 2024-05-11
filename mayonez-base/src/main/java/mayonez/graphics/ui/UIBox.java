package mayonez.graphics.ui;

import mayonez.*;
import mayonez.graphics.*;
import mayonez.graphics.batch.*;
import mayonez.graphics.textures.*;
import mayonez.math.*;
import mayonez.math.shapes.*;

/**
 * Draws a colored or textured box to the UI.
 *
 * @author SlavSquatSuperstar
 */
@UsesEngine(EngineType.GL)
public class UIBox extends Component implements UIElement {

    private static final Color DEFAULT_COLOR = Colors.WHITE;
    private Vec2 position, size;
    private Texture texture;
    private Color color;
    private Anchor anchor;

    private UIBox(Vec2 position, Vec2 size, Texture texture, Color color) {
        super(UpdateOrder.RENDER);
        this.position = position;
        this.size = size;
        this.texture = texture;
        this.color = color;
        this.anchor = Anchor.CENTER;
    }

    public UIBox(Vec2 position, Vec2 size, Texture texture) {
        this(position, size, texture, DEFAULT_COLOR);
    }

    public UIBox(Vec2 position, Vec2 size, Color color) {
        this(position, size, null, color);
    }

    // UI Getters and Setters

    public Vec2 getPosition() {
        return position;
    }

    public void setPosition(Vec2 position) {
        this.position = position;
    }

    public Vec2 getSize() {
        return size;
    }

    public void setSize(Vec2 size) {
        this.size = size;
    }

    public Color getColor() {
        return color;
    }

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

    public void setTexture(Texture texture) {
        this.texture = texture;
    }

    // Anchor Methods

    @Override
    public Anchor getAnchor() {
        return anchor;
    }

    @Override
    public void setAnchor(Anchor anchor) {
        this.anchor = anchor;
    }

    /**
     * Get the difference between the anchor point and this element's center.
     *
     * @return the anchor direction
     */
    public Vec2 getAnchorTranslateDirection() {
        return size.mul(anchor.getDirection().mul(0.5f));
    }

    /**
     * Moves this element so the anchored center equals the original center.
     */
    public void translateToAnchorOrigin() {
        setPosition(position.add(getAnchorTranslateDirection()));
    }

    // Renderer Methods
    @Override
    public void pushToBatch(RenderBatch batch) {
        var texID = batch.getTextureSlot((GLTexture) texture);
        var texCoords = (texture != null) ? ((GLTexture) texture).getTexCoords() : GLTexture.DEFAULT_TEX_COORDS;
        var sprBox = new BoundingBox(
                position.sub(getAnchorTranslateDirection()), size
        );
        var sprVertices = sprBox.getVertices();
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
