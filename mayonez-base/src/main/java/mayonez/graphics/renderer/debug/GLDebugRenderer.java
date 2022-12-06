package mayonez.graphics.renderer.debug;

import mayonez.DebugDraw;
import mayonez.annotations.EngineType;
import mayonez.annotations.UsesEngine;
import mayonez.graphics.renderer.DrawPrimitive;
import mayonez.graphics.renderer.GLRenderer;
import mayonez.graphics.renderer.RenderBatch;
import mayonez.math.Vec2;
import mayonez.physics.shapes.*;

import java.util.ArrayList;
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
    private final static LineStyle lineStyle = LineStyle.QUADS;
    private final List<DebugShape> shapes;

    public GLDebugRenderer() {
        super("assets/shaders/debug.glsl", MAX_LINES);
        shapes = new ArrayList<>();
    }

    // Debug Draw Methods

    @Override
    public void start() {
        super.start();
        shapes.clear();
    }

    @Override
    public void addShape(DebugShape shape) {
        if (shape.shape instanceof Edge edge) {
            addLine(edge, shape);
        } else if (shape.shape instanceof Polygon poly) {
            addPolygon(poly, shape);
        } else if (shape.shape instanceof Ellipse ellipse) {
            addPolygon(ellipse.toPolygon(), shape);
        }
    }

    private void addLine(Edge edge, DebugShape shape) {
        float stroke = DebugDraw.STROKE_SIZE;
        float len = edge.length; // Want to stretch lines to look flush
        switch (lineStyle) {
            case SINGLE -> shapes.add(shape);

            case MULTIPLE -> {
                Edge stretched = edge.scale(new Vec2((len + stroke - 1f) / len), null);

                Vec2 norm = edge.unitNormal();
                float start = (1 - stroke) * 0.5f; // -(stroke - 1) / 2
                for (int i = 0; i < stroke; i++) {
                    Edge e = stretched.translate(norm.mul(start + i));
                    shapes.add(new DebugShape(e, shape));
                }
            }
            case QUADS -> {
                float stretchedLen = (len + stroke - 1f);
                Rectangle rect = new Rectangle(edge.center(), new Vec2(stretchedLen, stroke), edge.toVector().angle());
                for (Triangle tri : rect.getTriangles())
                    shapes.add(new DebugShape(tri, shape.color, true, DebugShape.Priority.LINE));
            }
        }
    }

    private void addPolygon(Polygon poly, DebugShape shape) {
        if (shape.fill) {
            for (Triangle tri : poly.getTriangles()) shapes.add(new DebugShape(tri, shape)); // Create a triangle fan
        } else {
            for (Edge edge : poly.getEdges()) addLine(edge, shape);
        }
    }

    // Game Loop Methods

    @Override
    protected void bind() {
        super.bind();
        // Prepare to draw
        glEnable(GL_LINE_SMOOTH);
        glDisable(GL_BLEND);
        switch (lineStyle) {
            case SINGLE -> glLineWidth(DebugDraw.STROKE_SIZE); // doesn't work on macOS
            case MULTIPLE -> glLineWidth(1);
        }
    }

    @Override
    protected void pushDataToBatch() {
        shapes.forEach(shape -> { // Push shape vertices
            if (shape.shape instanceof Edge line) {
                RenderBatch batch = getAvailableBatch(null, shape.zIndex, DrawPrimitive.LINE);
                batch.pushVec2(line.getStart());
                batch.pushVec4(shape.color.toGL());
                batch.pushVec2(line.getEnd());
                batch.pushVec4(shape.color.toGL());
            } else if (shape.shape instanceof Triangle tri) {
                RenderBatch batch = getAvailableBatch(null, shape.zIndex, DrawPrimitive.TRIANGLE);
                for (Vec2 v : tri.getVertices()) {
                    batch.pushVec2(v);
                    batch.pushVec4(shape.color.toGL());
                }
            }
        });
    }

    @Override
    protected void unbind() {
        super.unbind();
        shapes.clear(); // Clear primitives after each frame
        // Finish drawing
        glDisable(GL_LINE_SMOOTH);
        glEnable(GL_BLEND);
    }

    @Override
    public void stop() {
        super.stop();
        shapes.clear();
    }

    private enum LineStyle {
        /**
         * Method 1. Draw a single line using glLineWidth() (may not work on all platforms)
         */
        SINGLE,
        /**
         * Method 2. Draw extra lines to simulate stroke size.
         */
        MULTIPLE,
        /**
         * Method 3. Draw lines using thin rectangles
         */
        QUADS
    }

}
