package slavsquatsuperstar.mayonezgl.renderer;

import slavsquatsuperstar.mayonez.GameObject;

import java.util.ArrayList;
import java.util.List;

public class RendererGL {

    private final int MAX_BATCH_SIZE = 1000;
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
        SpriteRenderer sprite = o.getComponent(SpriteRenderer.class);
        if (sprite != null) {
            // Add Object Sprite
            boolean added = false;

            for (RenderBatch batch : batches) {
                if (batch.hasRoom()) {
                    batch.addSprite(sprite );
                    added = true;
                    break;
                }
            }

            if (!added) {
                RenderBatch batch = new RenderBatch(MAX_BATCH_SIZE);
                batch.start();
                batches.add(batch);
                batch.addSprite(sprite);
            }
        }
    }

}
