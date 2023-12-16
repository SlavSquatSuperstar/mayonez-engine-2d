package mayonez.graphics.ui;

import mayonez.*;
import mayonez.annotations.*;
import mayonez.graphics.*;
import mayonez.graphics.batch.*;
import mayonez.graphics.textures.*;

/**
 * Draws a colored box on the UI.
 *
 * @author SlavSquatSuperstar
 */
@UsesEngine(EngineType.GL)
@ExperimentalFeature
// TODO extend GLSprite
public class UIBox extends Component implements UIElement {

    private Color color;

    // Todo keep separate UI transform
    public UIBox(Color color) {
        super(UpdateOrder.RENDER);
        this.color = color;
    }

    // Renderer Methods
    @Override
    public void pushToBatch(RenderBatch batch) {
        var uiXf = this.transform;
        var texCoords = GLTexture.DEFAULT_TEX_COORDS;
        BatchPushHelper.pushTexture(batch, uiXf, this.color, texCoords, 0, 1f);
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
