package mayonez.graphics.ui;

import mayonez.*;
import mayonez.annotations.*;
import mayonez.graphics.*;
import mayonez.graphics.batch.*;
import mayonez.graphics.textures.*;
import mayonez.math.*;
import mayonez.math.shapes.*;

/**
 * Draws a colored box on the UI.
 *
 * @author SlavSquatSuperstar
 */
@UsesEngine(EngineType.GL)
@ExperimentalFeature
public class UIBox extends Component implements UIElement {

    private Vec2 position, size;
    private Color color;

    public UIBox(Vec2 position, Vec2 size, Color color) {
        super(UpdateOrder.RENDER);
        this.position = position;
        this.size = size;
        this.color = color;
    }

    // UI Methods

    public void setPosition(Vec2 position) {
        this.position = position;
    }

    public void setSize(Vec2 size) {
        this.size = size;
    }

    public void setColor(Color color) {
        this.color = color;
    }

    // Renderer Methods
    @Override
    public void pushToBatch(RenderBatch batch) {
        var texCoords = GLTexture.DEFAULT_TEX_COORDS;
        var sprVertices = new BoundingBox(position, size).getVertices();
        BatchPushHelper.pushTexture(batch, sprVertices, color, texCoords, 0);
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
