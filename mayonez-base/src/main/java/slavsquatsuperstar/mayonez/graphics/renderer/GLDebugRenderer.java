package slavsquatsuperstar.mayonez.graphics.renderer;

import org.joml.Vector4f;
import slavsquatsuperstar.math.MathUtils;
import slavsquatsuperstar.math.Vec2;
import slavsquatsuperstar.mayonez.DebugDraw;
import slavsquatsuperstar.mayonez.annotations.EngineType;
import slavsquatsuperstar.mayonez.annotations.UsesEngine;
import slavsquatsuperstar.mayonez.graphics.GLCamera;
import slavsquatsuperstar.mayonez.physics.shapes.Circle;
import slavsquatsuperstar.mayonez.physics.shapes.Edge;
import slavsquatsuperstar.mayonez.physics.shapes.Polygon;

import java.awt.*;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import static org.lwjgl.opengl.GL11.*;

/**
 * Draws debug information using OpenGL.
 *
 * @author SlavSquatSuperstar
 */
@UsesEngine(EngineType.GL)
public final class GLDebugRenderer extends GLRenderer implements DebugRenderer {

    private final static int MAX_LINES = 500;
    private final List<DebugLine> lines;

    public GLDebugRenderer() {
        super("assets/shaders/line.glsl");
        lines = new ArrayList<>();
    }

    // Debug Draw Methods

    @Override
    public void addShape(DebugShape shape) {
        if (shape.shape instanceof Edge edge) {
            addLine(edge, shape.color, shape.priority.ordinal());
        } else if (shape.shape instanceof Polygon poly) {
            for (Edge edge : poly.getEdges()) addLine(edge, shape.color, shape.priority.ordinal());
        } else if (shape.shape instanceof Circle circle) {
            int numEdges = (int) (circle.getRadius() * 2f * MathUtils.PI);
            Polygon poly = new Polygon(circle.center(), numEdges, circle.getRadius());
            for (Edge edge : poly.getEdges()) addLine(edge, shape.color, shape.priority.ordinal());
        }
    }

    private void addLine(Edge edge, Color shapeColor, int zIndex) {
        Vector4f color = new slavsquatsuperstar.util.Color(shapeColor).toGL();
//        lines.add(new DebugLine(edge.getStart(), edge.getEnd(), color));
        // Draw extra lines to simulate stroke size
        float stroke = DebugDraw.STROKE_SIZE;
        // Stretch lines to look flush
        float len = edge.length;
        Edge stretched = edge.scale(new Vec2((len + stroke - 1) / len), null);

        // Add extra lines
        Vec2 norm = edge.unitNormal();
        float start = -(stroke - 1) / 2;
        for (int i = 0; i < stroke; i++) {
            Edge e = stretched.translate(norm.mul(start + i));
            lines.add(new DebugLine(e.getStart(), e.getEnd(), color, zIndex));
        }
    }

    // Game Loop Methods

    @Override
    public void render(Graphics2D g2) {
        // Bind
        shader.bind();
        GLCamera camera = (GLCamera) getCamera();
        shader.uploadMat4("uProjection", camera.getProjectionMatrix());
        shader.uploadMat4("uView", camera.getViewMatrix());

        // Draw
        rebuffer();
        glEnable(GL_LINE_SMOOTH);
        glDisable(GL_BLEND);
        glLineWidth(1);
//        glLineWidth(DebugDraw.STROKE_SIZE); // doesn't work on macOS
        batches.forEach(RenderBatch::render); // Draw
        glDisable(GL_LINE_SMOOTH);
        glEnable(GL_BLEND);

        shader.unbind();
        lines.clear(); // Clear lines after each frame
    }

    @Override
    protected void rebuffer() {
        batches.forEach(RenderBatch::clear); // Prepare batches
        for (DebugLine line : lines) {
            RenderBatch batch = getAvailableBatch(null, line.zIndex);
            batch.pushVec2(line.start);
            batch.pushVec4(line.color);
            batch.pushVec2(line.end);
            batch.pushVec4(line.color);
        }
        batches.forEach(RenderBatch::upload); // Finalize batches
        batches.sort(Comparator.comparingInt(RenderBatch::getZIndex)); // Sort batches by z-index
    }

    @Override
    protected RenderBatch createBatch(int zIndex) {
        return new RenderBatch(MAX_LINES, zIndex, DrawPrimitive.LINE);
    }

    @Override
    public void stop() {
        batches.forEach(RenderBatch::delete);
        lines.clear();
    }

}
