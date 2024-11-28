package mayonez.renderer;

import mayonez.assets.*;
import mayonez.graphics.debug.*;
import mayonez.graphics.sprites.*;
import mayonez.math.*;
import mayonez.renderer.awt.*;
import mayonez.renderer.gl.*;
import mayonez.renderer.shader.*;

import java.util.*;

/**
 * A factory class that constructs {@link RenderLayer} objects
 * depending on the engine type.
 *
 * @author SlavSquatSuperstar
 */
public final class RendererFactory {

    private static final Shader defaultShader = Objects.requireNonNull
            (Assets.getAsset("assets/shaders/default.glsl", Shader.class));
    private static final Shader uiShader = Objects.requireNonNull(
            Assets.getAsset("assets/shaders/ui.glsl", Shader.class));

    private RendererFactory() {
    }

    // Render Layer Methods

    public static RenderLayer createRenderLayer(boolean useGL, Sprite background, Vec2 sceneSize, float sceneScale) {
        // Scene
        var sceneRenderer = useGL ? new GLDefaultRenderer(defaultShader) : new JDefaultRenderer();
        sceneRenderer.setBackground(background, sceneSize);

        // Debug
        var debugRenderer = (DebugRenderer) sceneRenderer;
        var debugDraw = new DebugDraw(sceneScale, debugRenderer);

        return useGL ? createGLRenderLayer(sceneRenderer, debugDraw)
                : createJRenderLayer(sceneRenderer, debugDraw);
    }

    private static RenderLayer createGLRenderLayer(SceneRenderer sceneRenderer, DebugDraw debugDraw) {
        var uiRenderer = new GLUIRenderer(uiShader);

        return new RenderLayer(List.of(sceneRenderer, uiRenderer), debugDraw) {
            @Override
            public void addRenderable(Renderable r) {
                if (r.isInUI()) uiRenderer.addUIElement(r);
                else sceneRenderer.addRenderable(r);
            }

            @Override
            public void removeRenderable(Renderable r) {
                if (r.isInUI()) uiRenderer.removeUIElement(r);
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
