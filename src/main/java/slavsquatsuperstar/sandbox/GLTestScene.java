package slavsquatsuperstar.sandbox;

import org.joml.Vector2f;
import org.lwjgl.BufferUtils;
import slavsquatsuperstar.math.MathUtils;
import slavsquatsuperstar.mayonezgl.GameGL;
import slavsquatsuperstar.mayonezgl.SceneGL;
import slavsquatsuperstar.mayonezgl.renderer.CameraGL;
import slavsquatsuperstar.mayonezgl.renderer.Shader;
import slavsquatsuperstar.mayonezgl.renderer.SpriteGL;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;

import static org.lwjgl.opengl.GL20.*;
import static org.lwjgl.opengl.GL30.glBindVertexArray;
import static org.lwjgl.opengl.GL30.glGenVertexArrays;

public class GLTestScene extends SceneGL {

    private Shader shader;
    private SpriteGL texture;

    private float[] vertexArray = { // VBO: position, color, uv coords
            100f, -0.5f, 0f, 1f,    0f, 0f, 1f,     1f, 0f, // Bottom right 0
            -0.5f, 100f, 0f, 0f,    1f, 0f, 1f,     0f, 1f, // Top left     1
            100f, 100f, 0f, 0f,     0f, 1f, 1f,     1f, 1f, // Top right    2
            -0.5f, -0.5f, 0f, 1f,   1f, 0f, 1f,     0f, 0f // Bottom left  3
    };

    private int[] elementArray = { // counter clockwise
            /*
             *  1   2
             *  3   0
             */
            2, 1, 0, // top right triangle
            0, 1, 3  // bottom left triangle
    };
    private int vaoID, vboID, eboID;

    public GLTestScene() {
        super("LWJGL Test Scene");
    }

    public static void main(String[] args) {
        GameGL game = GameGL.instance();
        GameGL.setScene(new GLTestScene());
        game.start();
    }

    @Override
    public void init() {
        // Load resources
        shader = new Shader("assets/shaders/default.glsl");
        texture = new SpriteGL("src/main/resources/assets/mario.png");

        // Set camera position
        camera = new CameraGL(new Vector2f(-200, -300));

        // Generate VAO and fix transparency
        glEnable(GL_BLEND);
        glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
        vaoID = glGenVertexArrays();
        glBindVertexArray(vaoID);

        // Send vertices to GL
        FloatBuffer vertexBuffer = BufferUtils.createFloatBuffer(vertexArray.length);
        vertexBuffer.put(vertexArray).flip(); // flip into screen coordinates

        // Create VBO with vertices
        vboID = glGenBuffers();
        glBindBuffer(GL_ARRAY_BUFFER, vboID);
        glBufferData(GL_ARRAY_BUFFER, vertexBuffer, GL_STATIC_DRAW);

        // Create EBO
        IntBuffer elementBuffer = BufferUtils.createIntBuffer(elementArray.length);
        elementBuffer.put(elementArray).flip();
        eboID = glGenBuffers();
        glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, eboID);
        glBufferData(GL_ELEMENT_ARRAY_BUFFER, elementBuffer, GL_STATIC_DRAW);

        // Specify vertex attributes
        int[] sizes = {3, 4, 2}; // position, colors, uv
        int vertexSizeBytes = MathUtils.sum(sizes) * Float.BYTES;
        int start = 0;

        // Pass position/colors attributes
        for (int i = 0; i < sizes.length; i++) {
            glVertexAttribPointer(i, sizes[i], GL_FLOAT, false, vertexSizeBytes, start);
            glEnableVertexAttribArray(i);
            start += sizes[i] * Float.BYTES;
        }

    }

    @Override
    public void update(float dt) {
        super.update(dt);
        camera.position.x -= dt * 100;
        camera.position.y -= dt * 40;
    }

    @Override
    public void render() {
        super.render();

        // Bind shader and VAO
        shader.bind();

        // Upload texture to shader
        shader.uploadTexture("TEX_SAMPLER", 0);
        texture.bind();

        // Apply camera transforms
        shader.uploadMat4f("uProjection", camera.getProjectionMatrix());
        shader.uploadMat4f("uView", camera.getViewMatrix());
        glBindVertexArray(vaoID);

        // Enable vertex attribute pointers
        glEnableVertexAttribArray(0);
        glEnableVertexAttribArray(1);

        // Draw the triangles
        glDrawElements(GL_TRIANGLES, elementArray.length, GL_UNSIGNED_INT, 0);

        // Unbind everything
        shader.unbind();
    }

}
