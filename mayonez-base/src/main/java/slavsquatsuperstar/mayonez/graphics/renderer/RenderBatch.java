package slavsquatsuperstar.mayonez.graphics.renderer;

import org.joml.Vector4f;
import slavsquatsuperstar.math.Mat22;
import slavsquatsuperstar.math.MathUtils;
import slavsquatsuperstar.math.Vec2;
import slavsquatsuperstar.mayonez.Mayonez;
import slavsquatsuperstar.mayonez.graphics.sprites.GLSprite;
import slavsquatsuperstar.mayonez.io.Assets;
import slavsquatsuperstar.mayonez.io.GLTexture;
import slavsquatsuperstar.mayonez.io.Shader;
import slavsquatsuperstar.mayonez.physics.shapes.Rectangle;

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
    /* Pos (2), Color (4), Tex Coords (2), Tex ID (1) */
    private final int[] ATTRIB_SIZES = {2, 4, 2, 1}; // separate method for pushing attributes
    private final int VERTEX_SIZE = MathUtils.sum(ATTRIB_SIZES); // floats inside one vertex
    private final int VERTICES_PER_SPRITE = 4;
    private final int VERTICES_PER_QUAD = 6;

    // Renderer Fields
    private final Shader shader;
    private final int[] textureSlots; // support multiple texture IDs in batch
    private final List<GLTexture> textures;
    private int numSprites = 0;

    // GPU Resources
    /**
     * The vertex array object (VAO) ID for this batch.
     */
    private int vao;
    /**
     * The vertex buffer object (VBO) ID for this batch.
     */
    private int vbo;
    /**
     * The element buffer object (EBO), or index buffer, ID for this batch.
     */
    private int ebo;
    /**
     * The vertex data for this batch, which describes position, colors, and texture (UV) coordinates.
     */
    private final float[] vertices; // quads
    private int vertexOffset; // the current index of vertices

    // Batch Characteristics
    private final int maxBatchSize, maxTextureSlots;

    private final int zIndex;
    private final float worldScale;

    public RenderBatch(int maxBatchSize, int maxTextureSlots, int zIndex) {
        this.maxBatchSize = maxBatchSize;
        this.maxTextureSlots = maxTextureSlots;
        this.zIndex = zIndex;
        worldScale = Mayonez.getScene().getScale();

        shader = Assets.getShader("assets/shaders/default.glsl");
        textures = new ArrayList<>(maxTextureSlots);
        this.textureSlots = new int[maxTextureSlots];
        for (int i = 0; i < this.textureSlots.length; i++) this.textureSlots[i] = i; // ints 0-7
        vertices = new float[maxBatchSize * VERTICES_PER_SPRITE * VERTEX_SIZE];

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
        for (int i = 0; i < ATTRIB_SIZES.length; i++) {
            glVertexAttribPointer(i, ATTRIB_SIZES[i], GL_FLOAT, false, VERTEX_SIZE * Float.BYTES, offset);
            glEnableVertexAttribArray(i);
            offset += ATTRIB_SIZES[i] * Float.BYTES;
        }
    }

    private int[] generateIndices() {
        int[] elements = new int[VERTICES_PER_QUAD * maxBatchSize]; // 6 vertices per quad
        for (int i = 0; i < maxBatchSize; i++) {
            // Load element indices and create triangles
            int startIndex = VERTICES_PER_QUAD * i; // 6 vertices
            int offset = VERTICES_PER_SPRITE * i; // Next quad is just last + 4
            int[] triangleVertices = {3, 2, 0, 0, 2, 1}; // next would be 7, 6, 4, 4, 6, 5
            for (int j = 0; j < triangleVertices.length; j++)
                elements[startIndex + j] = triangleVertices[j] + offset;
        }
        return elements;
    }

    // Renderer Methods

    /**
     * Empties all sprite vertices and textures from the batch.
     */
    public void clear() {
        // Pass VBO attribute pointers
        vertexOffset = 0;
        numSprites = 0;
        textures.clear();
    }

    public void pushSpriteData(GLSprite spr) {
        // Get texture ID and add if necessary
        GLTexture tex = spr.getTexture();
        if (tex != null && !hasTexture(tex)) textures.add(tex);
        int texID = (tex == null) ? 0 : textures.indexOf(tex) + 1; // ID of 0 means no texture (use color)

        // Add sprite vertex data
        Vec2 objPos = spr.getTransform().position;
        Mat22 objRot = new Mat22(spr.getTransform().rotation);
        Vec2 objScl = spr.getTransform().scale;

        Vector4f color = spr.getColor();
        Vec2[] texCoords = spr.getTexCoords();

        // Render sprite at object center
        Vec2[] quadVertices = Rectangle.rectangleVertices(new Vec2(0), new Vec2(1), 0);

        for (int i = 0; i < quadVertices.length; i++) {
            Vec2 vertPos = objRot.times(quadVertices[i]); // Rotate according to object

            // sprite_pos = (obj_pos + vert_pos * obj_scale) * world_scale
            Vec2 spritePos = objPos.add(vertPos.mul(objScl)).mul(worldScale);
            float[] attributes = {
                    spritePos.x, spritePos.y,
                    color.x, color.y, color.z, color.w,
                    texCoords[i].x, texCoords[i].y,
                    texID
            };
            System.arraycopy(attributes, 0, vertices, vertexOffset, attributes.length); // Copy attributes to VAO
            vertexOffset += VERTEX_SIZE;
        }
        numSprites++;
    }

    /**
     * Upload all vertex data to the GPU
     */
    public void upload() {
        glBindBuffer(GL_ARRAY_BUFFER, vbo);
        glBufferSubData(GL_ARRAY_BUFFER, 0, vertices);
    }

    public void render() {
        // Bind Texture
        for (int i = 0; i < textures.size(); i++) textures.get(i).bind(i);
        shader.uploadIntArray("uTextures", textureSlots); // need to bind textures before uploading

        // Upload vertices and draw triangles
        glBindVertexArray(vao);
        glEnableVertexAttribArray(0);
        glEnableVertexAttribArray(1);
        glDrawElements(GL_TRIANGLES, numSprites * VERTICES_PER_QUAD, GL_UNSIGNED_INT, 0);

        // Unbind Textures
        glDisableVertexAttribArray(0);
        glDisableVertexAttribArray(1);
        glBindVertexArray(0);
        textures.forEach(GLTexture::unbind);
    }

    public void delete() {
        glDeleteBuffers(vbo);
        glDeleteBuffers(ebo);
        glDeleteVertexArrays(vao);
    }

    // Getter Methods

    boolean hasSpriteRoom() {
        return numSprites < maxBatchSize;
    }

    boolean hasTextureRoom() {
        return textures.size() <= maxTextureSlots;
    }

    boolean hasTexture(GLTexture tex) {
        return textures.contains(tex);
    }

    int getZIndex() {
        return zIndex;
    }

    @Override
    public String toString() {
        return String.format("Render Batch (Capacity: %d/%d, Z-Index: %d)", numSprites, maxBatchSize, zIndex);
    }
}
