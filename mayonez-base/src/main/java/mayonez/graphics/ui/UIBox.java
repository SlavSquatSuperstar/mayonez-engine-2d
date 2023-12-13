package mayonez.graphics.ui;

import mayonez.*;
import mayonez.graphics.*;
import mayonez.graphics.batch.*;
import mayonez.graphics.textures.*;
import mayonez.math.shapes.*;

/**
 * Draws a colored box on the UI.
 *
 * @author SlavSquatSuperstar
 */
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
        var color = this.color.toGL$mayonez_base();
        var texCoords = GLTexture.DEFAULT_TEX_COORDS;
        var texID = 0;

        var sprRect = new Rectangle(uiXf.getPosition(), uiXf.getScale(), uiXf.getRotation());
        var sprVertices = sprRect.getVertices();
        for (int i = 0; i < sprVertices.length; i++) {
            batch.pushVec2(sprVertices[i]);
            batch.pushVec4(color);
            batch.pushVec2(texCoords[i]);
            batch.pushInt(texID);
        }
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
