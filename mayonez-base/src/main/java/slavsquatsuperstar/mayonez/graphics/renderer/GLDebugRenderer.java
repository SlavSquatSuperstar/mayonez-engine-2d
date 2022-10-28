package slavsquatsuperstar.mayonez.graphics.renderer;

import org.joml.Vector2f;
import org.joml.Vector3f;
import slavsquatsuperstar.math.Vec2;
import slavsquatsuperstar.mayonez.annotations.EngineType;
import slavsquatsuperstar.mayonez.annotations.UsesEngine;
import slavsquatsuperstar.mayonez.graphics.GLCamera;
import slavsquatsuperstar.mayonez.io.Assets;
import slavsquatsuperstar.mayonez.io.Shader;
import slavsquatsuperstar.mayonez.physics.shapes.Edge;

import java.awt.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.lwjgl.opengl.GL15.*;
import static org.lwjgl.opengl.GL20.*;
import static org.lwjgl.opengl.GL30.*;

/**
 * Draws debug information using OpenGL.
 *
 * @author SlavSquatSuperstar
 */
@UsesEngine(EngineType.GL)
public final class GLDebugRenderer extends DebugRenderer {

    private final static int MAX_LINES = 500;
    private final static int VERTEX_SIZE = 6;
    private final List<LinePrimitive> lines;
    private final float[] vertices; // Vertex = (r, g, b, x, y, z)

    // GPU Fields
    private final Shader shader;
    private int vao, vbo;
    private boolean started;

    public GLDebugRenderer() {
        lines = new ArrayList<>();
        vertices = new float[MAX_LINES * VERTEX_SIZE * 2]; // 6 floats for 2 vertices in an edge
        shader = Assets.getShader("assets/shaders/line.glsl");
        started = false;
    }

    @Override
    public void addShape(DebugShape shape) {
        if (lines.size() >= MAX_LINES) return;

        if (shape.shape instanceof Edge edge) {
            Vector3f color = new Vector3f(shape.color.getRed(), shape.color.getGreen(), shape.color.getBlue());
            Vec2 norm = edge.unitNormal();
            // Make line thicker and flush
            float len = edge.length;
            Edge stretched = edge.scale(new Vec2((len + 2) / len), null);
            Edge[] edges = {stretched.translate(norm.mul(-0.5f)), stretched, stretched.translate(norm.mul(0.5f))};
            for (Edge e : edges)
                lines.add(new LinePrimitive(e.getStart().toJOML(), e.getEnd().toJOML(), color, 1));
        }
    }

    // Game Loop Methods

    public void start() {
        vao = glGenVertexArrays();
        glBindVertexArray(vao);

        vbo = glGenBuffers();
        glBindBuffer(GL_ARRAY_BUFFER, vbo);
        glBufferData(GL_ARRAY_BUFFER, (long) vertices.length * Float.BYTES, GL_DYNAMIC_DRAW);

        // Enable the vertex array attributes for position
        glVertexAttribPointer(0, VERTEX_SIZE / 2, GL_FLOAT, false, VERTEX_SIZE * Float.BYTES, 0);
        glEnableVertexAttribArray(0);
        // Vertex attributes for color
        glVertexAttribPointer(1, VERTEX_SIZE / 2, GL_FLOAT, false, VERTEX_SIZE * Float.BYTES, VERTEX_SIZE / 2 * Float.BYTES);
        glEnableVertexAttribArray(1);

        started = true;
    }

    public void beginFrame() {
        if (!started) start();

        // Remove dead edges
        for (int i = 0; i < lines.size(); i++) {
            if (lines.get(i).beginFrame() < 0) lines.remove(i--);
        }
    }

    @Override
    public void render(Graphics2D g2) {
        if (lines.isEmpty()) return;

        // Upload vertex attributes
        int index = 0;
        for (LinePrimitive edge : lines) {
            for (int i = 0; i < 2; i++) {
                Vector2f position = i == 0 ? edge.getStart() : edge.getEnd();
                Vector3f color = edge.getColor();

                // Load position and color
                float[] attributes = {
                        position.x, position.y, -10f,
                        color.x, color.y, color.z
                };

                System.arraycopy(attributes, 0, vertices, index, attributes.length);
//                vertices[index] = position.x;
//                vertices[index + 1] = position.y;
//                vertices[index + 2] = -10.0f;
//
//                vertices[index + 3] = color.x;
//                vertices[index + 4] = color.y;
//                vertices[index + 5] = color.z;
                index += VERTEX_SIZE;
            }
        }

        glBindBuffer(GL_ARRAY_BUFFER, vbo);
        glBufferSubData(GL_ARRAY_BUFFER, 0, Arrays.copyOfRange(vertices, 0, lines.size() * VERTEX_SIZE * 2));

        // Bind
        shader.bind();
        GLCamera camera = (GLCamera) getCamera();
        shader.uploadMat4("uProjection", camera.getProjectionMatrix());
        shader.uploadMat4("uView", camera.getViewMatrix());

        glBindVertexArray(vao);
        glEnableVertexAttribArray(0);
        glEnableVertexAttribArray(1);

        // Draw
        glEnable(GL_LINE_SMOOTH);
        glDisable(GL_BLEND); // overlapping lines causes a bit of blending
        glLineWidth(5); // doesn't work on macOS
        glDrawArrays(GL_LINES, 0, lines.size() * VERTEX_SIZE * 2);
        glDisable(GL_LINE_SMOOTH);
        glEnable(GL_BLEND);

        // Discard
        glDisableVertexAttribArray(0);
        glDisableVertexAttribArray(1);
        glBindVertexArray(0);
        shader.unbind();
    }

    @Override
    public void stop() {
        lines.clear();
    }

}
