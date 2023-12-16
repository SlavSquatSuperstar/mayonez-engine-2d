package mayonez.graphics.renderer;

import mayonez.*;

/**
 * A factory class that constructs {@link mayonez.graphics.renderer.Renderer} objects
 * depending on the engine type.
 *
 * @author SlavSquatSuperstar
 */
public final class RendererFactory {

    private RendererFactory() {
    }

    public static SceneRenderer createSceneRenderer() {
        return Mayonez.getUseGL() ? new GLDefaultRenderer() : new JDefaultRenderer();
    }

    public static UIRenderer createUIRenderer() {
        return Mayonez.getUseGL() ? new GLUIRenderer() : new JUIRenderer();
    }

}
