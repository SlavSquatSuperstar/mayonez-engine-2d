package slavsquatsuperstar.mayonez.graphics.renderer;

import slavsquatsuperstar.math.Vec2;
import slavsquatsuperstar.mayonez.DebugDraw;
import slavsquatsuperstar.mayonez.annotations.EngineType;
import slavsquatsuperstar.mayonez.annotations.UsesEngine;
import slavsquatsuperstar.mayonez.physics.shapes.Edge;
import slavsquatsuperstar.mayonez.physics.shapes.Ellipse;
import slavsquatsuperstar.mayonez.physics.shapes.Polygon;
import slavsquatsuperstar.mayonez.physics.shapes.Triangle;

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
    private final List<DebugShape> shapes;

    public GLDebugRenderer() {
        super("assets/shaders/debug.glsl", MAX_LINES);
        shapes = new ArrayList<>();
    }

    // Debug Draw Methods

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
//        // If not adding extra lines
//        shapes.add(shape);

        // TODO draw quads instead
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
            shapes.add(new DebugShape(e, shape));
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
        glLineWidth(1);
//        glLineWidth(DebugDraw.STROKE_SIZE); // doesn't work on macOS
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

}
