package slavsquatsuperstar.mayonezgl.renderer;

import slavsquatsuperstar.mayonez.GameObject;
import slavsquatsuperstar.mayonez.Preferences;

import java.util.ArrayList;
import java.util.List;

public class RendererGL {

    private final int MAX_BATCH_SIZE = Preferences.MAX_BATCH_SIZE;
    private final List<RenderBatch> batches;

    public RendererGL() {
        batches = new ArrayList<>();
    }

    // Game Loop Methods

    public void start() {
        batches.add(new RenderBatch(MAX_BATCH_SIZE));
    }

    public void render() {
        batches.forEach(RenderBatch::render);
    }

    // Renderer Methods

    public void addObject(GameObject o) {
        SpriteGL spr = o.getComponent(SpriteGL.class);
        if (spr != null) {
            for (RenderBatch batch : batches) {
                if (batch.hasRoom()) { // has room for sprite
                    TextureGL tex = spr.getTexture();
                    // has texture or room for another texture
                    if (tex == null || (batch.hasTexture(tex) || batch.hasTextureRoom())) {
                        batch.addSprite(spr);
                        return;
                    }
                }
            }
            // Sprite not added
            RenderBatch batch = new RenderBatch(MAX_BATCH_SIZE);
            batch.start();
            batches.add(batch);
            batch.addSprite(spr);
        }
    }

}
