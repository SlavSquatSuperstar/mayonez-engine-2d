package slavsquatsuperstar.mayonez.graphics.renderer;

import org.joml.Vector2f;
import org.joml.Vector3f;
import org.joml.Vector4f;
import org.lwjgl.BufferUtils;
import slavsquatsuperstar.math.Vec2;
import slavsquatsuperstar.mayonez.Mayonez;
import slavsquatsuperstar.mayonez.Preferences;
import slavsquatsuperstar.mayonez.Transform;
import slavsquatsuperstar.mayonez.graphics.sprites.GLSprite;
import slavsquatsuperstar.mayonez.io.GLTexture;
import slavsquatsuperstar.mayonez.physics.shapes.Rectangle;
import slavsquatsuperstar.util.StringUtils;

import java.nio.IntBuffer;
import java.util.ArrayList;
import java.util.List;

import static org.lwjgl.opengl.GL15.*;
import static org.lwjgl.opengl.GL20.glDisableVertexAttribArray;
import static org.lwjgl.opengl.GL20.glEnableVertexAttribArray;
import static org.lwjgl.opengl.GL30.*;

/**
 * A batch renderer stores vertex information to reduce the amount of GPU draw calls, combining many sprites into one
 * large mesh.
 *
 * @author SlavSquatSuperstar
 */
public class RenderBatch {

    // Vertex Parameters
    private final DrawPrimitive primitive;
    private final int vertexCount;
    private final int elementCount;
    private final int vertexSize;
    private final List<GLTexture> textures;

    // GPU Resources
    private int vao; // Vertex array object (VAO) ID for this batch
    private int vbo; // Vertex buffer object (VBO) ID for this batch
    private int ebo; // Element buffer object (EBO)/index buffer ID for this batch
    private final float[] vertices; // Quad vertex data for this batch, containing position, colors, and texture (UV) coordinates
    private int vertexOffset; // Current vertex index

    // Batch Characteristics
    private final int maxTextureSlots = Preferences.getMaxTextureSlots();
    private final int maxBatchSize;
    private final int zIndex;
    private final float worldScale;

    public RenderBatch(int maxBatchSize, int zIndex, DrawPrimitive primitive) {
        this.maxBatchSize = maxBatchSize;
        this.zIndex = zIndex;
        worldScale = Mayonez.getScene().getScale();

        this.primitive = primitive;
        this.vertexCount = primitive.vertexCount;
        this.elementCount = primitive.elementCount;
        this.vertexSize = primitive.vertexSize;

        // Renderer Fields
        textures = new ArrayList<>(maxTextureSlots);
        vertices = new float[maxBatchSize * vertexCount * vertexSize];

        create();
    }

    // Initialization Methods

    /**
     * Allocates GPU resources to this render batch. Generates the vertex array, vertex buffer, and index buffer.
     */
    private void create() {
        // Generate and bind VAO
        vao = glGenVertexArrays();
        glBindVertexArray(vao);

        // Allocate space for VBO
        vbo = glGenBuffers();
        glBindBuffer(GL_ARRAY_BUFFER, vbo);
        glBufferData(GL_ARRAY_BUFFER, (long) vertices.length * Float.BYTES, GL_DYNAMIC_DRAW);

        // Only upload EBO vertices once
        ebo = glGenBuffers();
        glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, ebo);
        glBufferData(GL_ELEMENT_ARRAY_BUFFER, generateIndices(), GL_STATIC_DRAW);

        // Allocate vertex index buffer
        int offset = 0;
        int[] attributeSizes = primitive.attributeSizes;
        for (int i = 0; i < attributeSizes.length; i++) {
            glVertexAttribPointer(i, attributeSizes[i], GL_FLOAT, false, vertexSize * Float.BYTES, offset);
            glEnableVertexAttribArray(i);
            offset += attributeSizes[i] * Float.BYTES;
        }
    }

    private IntBuffer generateIndices() {
        IntBuffer elements = BufferUtils.createIntBuffer(primitive.elementCount * maxBatchSize);
        for (int i = 0; i < maxBatchSize; i++) {
            int offset = vertexCount * i; // Next quad is just last + 4
            if (primitive == DrawPrimitive.SPRITE) {
                // Load element indices and create triangles
                int[] triangleVertices = {3, 2, 0, 0, 2, 1}; // next would be 7, 6, 4, 4, 6, 5
                for (int v : triangleVertices) elements.put(offset + v);
            } else if (primitive == DrawPrimitive.LINE) {
                int[] lineVertices = {0, 1};
                for (int v : lineVertices) elements.put(offset + v);
            }
        }
        elements.flip(); // need to flip an int buffer
        return elements;
    }

    // Renderer Methods

    /**
     * Empties all sprite vertices and textures from the batch and readies it for buffering.
     */
    public void clear() {
        // Pass VBO attribute pointers
        vertexOffset = 0;
        textures.clear();
    }

    /**
     * Upload all vertex data to the GPU after buffering.
     */
    public void upload() {
        glBindBuffer(GL_ARRAY_BUFFER, vbo);
        glBufferSubData(GL_ARRAY_BUFFER, 0, vertices);
    }

    public void render() {
//        System.out.println(this);
        // Bind textures and vertices
        for (int i = 0; i < textures.size(); i++) textures.get(i).bind(i);
        glBindVertexArray(vao);
        glEnableVertexAttribArray(0);
        glEnableVertexAttribArray(1);

        // Draw sprites
        int numVertices = (vertexOffset * elementCount) / (vertexSize * vertexCount);
        glDrawElements(primitive.primitiveType, numVertices, GL_UNSIGNED_INT, 0);

        // Unbind vertices and textures
        glDisableVertexAttribArray(0);
        glDisableVertexAttribArray(1);
        glBindVertexArray(0);
        textures.forEach(GLTexture::unbind);
    }

    /**
     * Free GPU resources upon stopping scene.
     */
    public void delete() {
        glDeleteBuffers(vbo);
        glDeleteBuffers(ebo);
        glDeleteVertexArrays(vao);
    }

    // Helper Methods
    /**
     * Adds a texture to this render batch if the texture is not present and returns the texture ID.
     *
     * @param tex the texture
     * @return the texture ID
     */
    public int addTexture(GLTexture tex) {
        if (tex == null) return 0; // if color return 0
        if (!hasTexture(tex)) textures.add(tex); // add if don't have texture
        return textures.indexOf(tex) + 1; // return tex ID
    }

    /**
     * Adds a sprite to this render batch and pushes its vertex data and texture.
     *
     * @param spr the sprite
     */
    public void addSprite(GLSprite spr) {
        // Add sprite vertex data
        Transform objXf = spr.getTransform();
        Vector4f color = spr.getColor();
        Vec2[] texCoords = spr.getTexCoords();
        int texID = addTexture(spr.getTexture());

        // Render sprite at object center and rotate according to object
        Vec2[] sprVertices = Rectangle.rectangleVertices(new Vec2(0), new Vec2(1), objXf.rotation);
        for (int i = 0; i < sprVertices.length; i++) {
            // sprite_pos = (obj_pos + vert_pos * obj_scale) * world_scale
            Vec2 spritePos = objXf.position.add(sprVertices[i].mul(objXf.scale)).mul(worldScale);
            pushVec2(spritePos);
            pushVec4(color);
            pushVec2(texCoords[i]);
            pushInt(texID);
        }
    }

    // Push Vertex Methods

    public void pushInt(int i) {
        vertices[vertexOffset++] = i;
    }

    public void pushFloat(float f) {
        vertices[vertexOffset++] = f;
    }

    public void pushVec2(Vec2 v) {
        pushFloat(v.x);
        pushFloat(v.y);
    }

    public void pushVec2(Vector2f v) {
        pushFloat(v.x);
        pushFloat(v.y);
    }

    public void pushVec3(Vector3f v) {
        pushFloat(v.x);
        pushFloat(v.y);
        pushFloat(v.z);
    }

    public void pushVec4(Vector4f v) {
        pushFloat(v.x);
        pushFloat(v.y);
        pushFloat(v.z);
        pushFloat(v.w);
    }

    // Getter Methods

    /**
     * If the render batch has capacity for another primitive object.
     *
     * @return if there are still unused vertices
     */
    boolean hasRoom() {
        return vertexOffset < vertices.length;
    }

    /**
     * If the render batch has capacity for another texture.
     *
     * @return if there are unused texture slots
     */
    boolean hasTextureRoom() {
        return textures.size() < maxTextureSlots;
    }

    boolean hasTexture(GLTexture tex) {
        return textures.contains(tex);
    }

    int getZIndex() {
        return zIndex;
    }

    @Override
    public String toString() {
//        return String.format("Render Batch (Capacity: %d/%d, Z-Index: %d)", vertexOffset / (vertexSize * vertexCount), maxBatchSize, zIndex);
        return String.format("Render Batch (%s)", StringUtils.capitalize(primitive.name()));
    }
}
