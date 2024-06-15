package mayonez.renderer;

import mayonez.*;
import mayonez.graphics.debug.*;
import mayonez.graphics.sprites.*;
import mayonez.graphics.ui.*;
import mayonez.math.*;
import mayonez.renderer.awt.*;
import mayonez.renderer.gl.*;

import java.util.*;

/**
 * A factory class that constructs {@link RenderLayer} objects
 * depending on the engine type.
 *
 * @author SlavSquatSuperstar
 */
public final class RendererFactory {

    private RendererFactory() {
    }

    // Render Layer Methods

    public static RenderLayer createRenderLayer(Sprite background, Vec2 sceneSize, float sceneScale) {
        var useGL = Mayonez.getUseGL();

        // Scene
        var sceneRenderer = useGL ? new GLDefaultRenderer() : new JDefaultRenderer();
        sceneRenderer.setBackground(background, sceneSize, sceneScale);

        // Debug
        var debugRenderer = (DebugRenderer) sceneRenderer;
        var debugDraw = new DebugDraw(sceneScale, debugRenderer);

        return useGL ? createGLRenderLayer(sceneRenderer, debugDraw)
                : createJRenderLayer(sceneRenderer, debugDraw);
    }

    private static RenderLayer createGLRenderLayer(SceneRenderer sceneRenderer, DebugDraw debugDraw) {
        var uiRenderer = new GLUIRenderer();

        return new RenderLayer(List.of(sceneRenderer, uiRenderer), debugDraw) {
            @Override
            public void addRenderable(Renderable r) {
                if (r instanceof UIRenderableElement e) uiRenderer.addUIElement(e);
                else sceneRenderer.addRenderable(r);
            }

            @Override
            public void removeRenderable(Renderable r) {
                if (r instanceof UIRenderableElement e) uiRenderer.removeUIElement(e);
                else sceneRenderer.removeRenderable(r);
            }
        };
    }

    private static RenderLayer createJRenderLayer(SceneRenderer sceneRenderer, DebugDraw debugDraw) {
        return new RenderLayer(List.of(sceneRenderer), debugDraw) {
            @Override
            public void addRenderable(Renderable r) {
                sceneRenderer.addRenderable(r);
            }

            @Override
            public void removeRenderable(Renderable r) {
                sceneRenderer.removeRenderable(r);
            }
        };
    }

}
