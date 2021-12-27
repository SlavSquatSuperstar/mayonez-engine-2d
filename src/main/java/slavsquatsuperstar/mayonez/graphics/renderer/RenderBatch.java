package slavsquatsuperstar.mayonez.graphics.renderer;

import org.joml.Vector2f;
import org.joml.Vector4f;
import slavsquatsuperstar.math.MathUtils;
import slavsquatsuperstar.math.Vec2;
import slavsquatsuperstar.mayonez.Mayonez;
import slavsquatsuperstar.mayonez.Preferences;
import slavsquatsuperstar.mayonez.fileio.Assets;
import slavsquatsuperstar.mayonez.graphics.GLCamera;
import slavsquatsuperstar.mayonez.graphics.GLSprite;
import slavsquatsuperstar.mayonez.fileio.GLTexture;
import slavsquatsuperstar.mayonez.fileio.Shader;

import java.util.ArrayList;
import java.util.List;

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
     * Pos (2), Color (4), Tex Coords (2), Tex ID (1)
     */
    private final int[] ATTRIB_SIZES = {2, 4, 2, 1};
    private final int VERTEX_SIZE = MathUtils.sum(ATTRIB_SIZES); // floats inside one vertex
    private final int VERTICES_PER_SPRITE = 4;
    private final int VERTICES_PER_QUAD = 6;

    // Sprite Renderer Fields
    private final GLSprite[] sprites;
    private int numSprites = 0;
    private int[] texSlots = new int[Preferences.MAX_TEXTURE_SLOTS]; // support multiple textures in batch

    // Renderer Data
    private final List<GLTexture> textures;
    private final GLCamera camera;
    private Shader shader;
    private float[] vertices; // quads
    private int vaoID, vboID;

    public RenderBatch(int maxBatchSize, GLCamera camera) {
        sprites = new GLSprite[maxBatchSize]; // shader array capacity
        shader = Assets.getAsset("assets/shaders/default.glsl", Shader.class);
        vertices = new float[maxBatchSize * VERTICES_PER_SPRITE * VERTEX_SIZE];
        for (int i = 0; i < texSlots.length; i++) texSlots[i] = i; // ints 0-7

        textures = new ArrayList<>();
        this.camera = camera;
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
        shader.uploadMat4("uProjection", camera.getProjectionMatrix());
        shader.uploadMat4("uView", camera.getViewMatrix());

        // Bind the texture
        for (int i = 0; i < textures.size(); i++) textures.get(i).bind(i);

        shader.uploadIntArray("uTextures", texSlots);

        // Upload vertices and draw triangles
        glBindVertexArray(vaoID);
        glEnableVertexAttribArray(0);
        glEnableVertexAttribArray(1);
        glDrawElements(GL_TRIANGLES, numSprites * VERTICES_PER_QUAD, GL_UNSIGNED_INT, 0);

        // Unbind everything
        glDisableVertexAttribArray(0);
        glDisableVertexAttribArray(1);
        glBindVertexArray(0);
        textures.forEach(GLTexture::unbind);
        shader.unbind();
    }

    // Sprite Renderer Methods

    public void addSprite(GLSprite spr) {
        int index = numSprites++;
        sprites[index] = spr;
        loadVertexProperties(index);

        GLTexture tex = spr.getTexture();
        if (tex != null && !hasTexture(tex)) textures.add(tex);
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
        GLSprite sprite = sprites[index];
        int offset = index * VERTICES_PER_SPRITE * VERTEX_SIZE; // 4 vertices per sprite

        Vector4f color = sprite.getColor();
        Vector2f[] texCoords = sprite.getTexCoords();

        GLTexture tex = sprite.getTexture();
        if (tex != null && !hasTexture(tex)) textures.add(tex);
        int texID = textures.indexOf(tex) + 1;
        // texID 0 means no texture

        // Load properties for each vertex
        Vec2[] quadVertices = {
                new Vec2(0.5f, 0.5f), new Vec2(0.5f, -0.5f), new Vec2(-0.5f, -0.5f), new Vec2(-0.5f, 0.5f)
        }; // Render sprite at object center
        for (int i = 0; i < quadVertices.length; i++) {
            Vec2 objPos = sprite.getTransform().position;
            Vec2 objScale = sprite.getTransform().scale;
            Vec2 vertPos = quadVertices[i];
            float worldScale = Mayonez.getScene().getCellSize();

            // sprite_pos = (obj_pos + vert_pos * obj_scale) * world_scale
            Vec2 spritePos = objPos.add(vertPos.mul(objScale)).mul(worldScale);
            float[] attributes = {
                    spritePos.x, spritePos.y,
                    color.x, color.y, color.z, color.w,
                    texCoords[i].x, texCoords[i].y,
                    texID
            };
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

    public boolean hasTextureRoom() {
        return textures.size() <= texSlots.length;
    }

    public boolean hasTexture(GLTexture t) {
        return textures.contains(t);
    }

}
