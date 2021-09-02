package slavsquatsuperstar.mayonezgl.renderer;

import org.joml.Vector4f;
import slavsquatsuperstar.fileio.Assets;
import slavsquatsuperstar.math.MathUtils;
import slavsquatsuperstar.math.Vec2;
import slavsquatsuperstar.mayonezgl.GameGL;

import static org.lwjgl.opengl.GL11.GL_FLOAT;
import static org.lwjgl.opengl.GL15.*;
import static org.lwjgl.opengl.GL20.*;
import static org.lwjgl.opengl.GL30.glBindVertexArray;
import static org.lwjgl.opengl.GL30.glGenVertexArrays;

/**
 * A batch renderer stores vertex information to reduce the amount of GPU draw calls, combining many sprites into one
 * large mesh.
 *
 * @author SlavSquatSuperstar
 */
public class RenderBatch {

    // Vertex Parameters
    /*
     * Pos              Color
     * float, float     float, float, float, float
     */
    private final int[] ATTRIB_SIZES = {2, 4}; // position, colors
    private final int VERTEX_SIZE = MathUtils.sum(ATTRIB_SIZES); // floats inside one vertex
    private final int VERTICES_PER_SPRITE = 4;
    private final int VERTICES_PER_QUAD = 6;

    // Sprite Renderer Fields
    private final SpriteRenderer[] sprites;
    private int numSprites = 0;

    // Renderer Data
    private Shader shader;
    private float[] vertices; // quads
    private int vaoID, vboID;

    public RenderBatch(int maxBatchSize) {
        sprites = new SpriteRenderer[maxBatchSize]; // shader array capacity
        shader = Assets.getAsset("assets/shaders/default.glsl", Shader.class);
        vertices = new float[maxBatchSize * VERTICES_PER_SPRITE * VERTEX_SIZE];
    }

    // Game Loop Methods

    public void start() {
        // Generate and bind VAO
//        glEnable(GL_BLEND);
//        glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
        vaoID = glGenVertexArrays();
        glBindVertexArray(vaoID);

        // Allocate space for VBO
        vboID = glGenBuffers();
        glBindBuffer(GL_ARRAY_BUFFER, vboID);
        glBufferData(GL_ARRAY_BUFFER, (long) vertices.length * Float.BYTES, GL_DYNAMIC_DRAW);

        // Only upload EBO vertices once
        int eboID = glGenBuffers();
        int[] indices = generateIndices();
        glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, eboID);
        glBufferData(GL_ELEMENT_ARRAY_BUFFER, indices, GL_STATIC_DRAW);

        // Pass VBO attribute pointers
        int offset = 0;
        for (int i = 0; i < ATTRIB_SIZES.length; i++) {
            glVertexAttribPointer(i, ATTRIB_SIZES[i], GL_FLOAT, false, VERTEX_SIZE * Float.BYTES, offset);
            glEnableVertexAttribArray(i);
            offset += ATTRIB_SIZES[i] * Float.BYTES;
        }
    }

    public void render() {
        // Refresh buffer data
        glBindBuffer(GL_ARRAY_BUFFER, vboID);
        glBufferSubData(GL_ARRAY_BUFFER, 0, vertices);

        // Use the shader
        shader.bind();
        shader.uploadMat4("uProjection", GameGL.getScene().getCamera().getProjectionMatrix());
        shader.uploadMat4("uView", GameGL.getScene().getCamera().getViewMatrix());

        // Upload vertices and draw triangles
        glBindVertexArray(vaoID);
        glEnableVertexAttribArray(0);
        glEnableVertexAttribArray(1);
        glDrawElements(GL_TRIANGLES, numSprites * VERTICES_PER_QUAD, GL_UNSIGNED_INT, 0);

        // Unbind everything
        glDisableVertexAttribArray(0);
        glDisableVertexAttribArray(1);
        glBindVertexArray(0);
        shader.unbind();
    }

    // Sprite Renderer Methods

    public void addSprite(SpriteRenderer sr) {
        int index = numSprites++;
        sprites[index] = sr;
        loadVertexProperties(index);
    }

    // Helper Methods

    private int[] generateIndices() {
        int[] elements = new int[VERTICES_PER_QUAD * getMaxBatchSize()]; // 6 vertices per quad
        for (int i = 0; i < getMaxBatchSize(); i++) {
            // Load element indices and create triangles
            int startIndex = VERTICES_PER_QUAD * i; // 6 vertices
            int offset = VERTICES_PER_SPRITE * i; // Next quad is just last + 4
            int[] triangleVertices = {3, 2, 0, 0, 2, 1}; // next would be 7, 6, 4, 4, 6, 5
            for (int j = 0; j < triangleVertices.length; j++)
                elements[startIndex + j] = triangleVertices[j] + offset;
        }
        return elements;
    }

    private void loadVertexProperties(int index) { // create vertices for a sprite
        SpriteRenderer sprite = sprites[index];
        int offset = index * VERTICES_PER_SPRITE * VERTEX_SIZE; // 4 vertices per sprite
        Vector4f color = sprite.getColor();

        // Add vertices with the appropriate properties
        Vec2[] quadVertices = {new Vec2(1, 1), new Vec2(1, 0), new Vec2(0, 0), new Vec2(0, 1)};
        for (Vec2 v : quadVertices) {
            Vec2 position = sprite.getTransform().position.add(v.mul(sprite.getTransform().scale)); // pos = obj_pos + vert_pos * obj_scale
            float[] attributes = {position.x, position.y, color.x, color.y, color.z, color.w};
            // Load position and color
            System.arraycopy(attributes, 0, vertices, offset, attributes.length);
            offset += VERTEX_SIZE;
        }
    }

    // Getter Methods

    public int getMaxBatchSize() {
        return sprites.length;
    }

    public boolean hasRoom() {
        return numSprites < getMaxBatchSize();
    }

}
