package slavsquatsuperstar.mayonez.graphics.renderer;

import slavsquatsuperstar.mayonez.Preferences;
import slavsquatsuperstar.mayonez.io.Assets;
import slavsquatsuperstar.mayonez.io.GLTexture;
import slavsquatsuperstar.mayonez.io.Shader;

import java.util.ArrayList;
import java.util.List;

public abstract class GLRenderer {

    // GPU Resources
    protected final List<RenderBatch> batches;
    protected final Shader shader;
    protected final int[] textureSlots; // support multiple texture IDs in batch

    public GLRenderer(String shaderFile) {
        this.batches = new ArrayList<>();
        this.shader = Assets.getShader(shaderFile);
        textureSlots = new int[Preferences.getMaxTextureSlots()];
        for (int i = 0; i < this.textureSlots.length; i++) this.textureSlots[i] = i; // ints 0-7
    }

    protected abstract void rebuffer();

    protected final RenderBatch getAvailableBatch(GLTexture texture, int zIndex) {
        for (RenderBatch batch : batches) {
            if (batch.hasRoom() && batch.getZIndex() == zIndex) { // has room for sprite (vertices)
                if (batch.hasTextureRoom() || batch.hasTexture(texture) || texture == null) // has room for texture or using color
                    return batch;
            }
        }
        // All batches full
        RenderBatch batch = createBatch(zIndex);
        batch.clear();
        batches.add(batch);
        return batch;
    }

    protected abstract RenderBatch createBatch(int zIndex);

}
