package mayonez.graphics.textures;

import mayonez.*;
import mayonez.assets.image.*;
import mayonez.graphics.*;
import mayonez.math.*;
import mayonez.math.shapes.*;
import org.lwjgl.BufferUtils;

import java.io.IOException;
import java.nio.ByteBuffer;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL12.GL_TEXTURE_WRAP_R;

/**
 * An image file used by the GL engine. This class should not be directly
 * instantiated. Instead, call {@link mayonez.graphics.textures.Textures#getTexture}.
 * See {@link mayonez.graphics.textures.Texture} for more information.
 * <p>
 * Sources:
 * <ul>
 *    <li><a href="https://github.com/LWJGL/lwjgl3/blob/master/modules/samples/src/test/java/org/lwjgl/demo/stb/Image.java">org.lwjgl.demo.stb.Image</a></li>
 *    <li><a href="https://github.com/LWJGL/lwjgl3/blob/master/modules/samples/src/test/java/org/lwjgl/demo/util/IOUtil.java">org.lwjgl.demo.stb.IOUtil</a></li>
 *    <li><a href="https://github.com/LWJGL/lwjgl3/blob/master/modules/samples/src/test/java/org/lwjgl/demo/glfw/GLFWUtil.java">org.lwjgl.demo.glfw.GLFWUtil</a></li>
 * </ul>
 *
 * @author SlavSquatSuperstar
 */
@UsesEngine(EngineType.GL)
public sealed class GLTexture extends Texture permits GLSpriteSheetTexture {

    // Constants
    public static final Vec2[] DEFAULT_TEX_COORDS
            = Rectangle.rectangleVertices(new Vec2(0.5f), new Vec2(1f), 0f);

    // Image Data Fields
    private STBImageData imageData;

    // GPU Fields
    private int texID;
    private final Vec2[] texCoords;

    /**
     * Create a brand-new GLTexture with the given filename.
     *
     * @param filename the file location
     */
    public GLTexture(String filename) {
        this(filename, DEFAULT_TEX_COORDS);
        readImage();
    }

    /**
     * Create a GLTexture from a portion of another texture.
     *
     * @param filename  the file location
     * @param texCoords the sub-image coordinates
     */
    protected GLTexture(String filename, Vec2[] texCoords) {
        super(filename);
        this.texCoords = texCoords;
    }

    // Read Image Methods

    @Override
    protected void readImage() {
        try {
            // Read image from file
            imageData = new STBImageData(getFilename());

            // Create texture on GPU
            generateTexID();
            setTextureParameters();
            uploadImageToTexture(imageData);
        } catch (IOException e) {
            Logger.error("Could not read image file %s", getFilename());
            Logger.printStackTrace(e);
            texID = GL_NONE;
        }
    }

    private void generateTexID() {
        texID = glGenTextures();
        glBindTexture(GL_TEXTURE_2D, texID);
    }

    private static void setTextureParameters() {
        // wrap if too big
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_R, GL_REPEAT);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_S, GL_REPEAT);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_T, GL_REPEAT);
        // pixelate when scaling
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_NEAREST);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_NEAREST);
    }

    private void uploadImageToTexture(STBImageData imageData) {
        int format = imageData.hasAlpha() ? GL_RGBA : GL_RGB;
        glTexImage2D(GL_TEXTURE_2D, 0, format, getWidth(), getHeight(),
                0, format, GL_UNSIGNED_BYTE, imageData.getBuffer());
        imageData.freeImage();
    }

    private ByteBuffer getImageFromTexture() {
        glBindTexture(GL_TEXTURE_2D, texID);
        var buffer = BufferUtils.createByteBuffer(getWidth() * getHeight() * imageData.getChannels());
        glGetTexImage(GL_TEXTURE_2D, 0, GL_RGBA, GL_UNSIGNED_BYTE, buffer);
        return buffer;
        // TODO construct image data
        // TODO convert AWT and STB
    }

    // Asset Methods

    @Override
    public void free() {
        glDeleteTextures(texID);
    }

    // Image Getters

    @Override
    public int getWidth() {
        return imageData.getWidth();
    }

    @Override
    public int getHeight() {
        return imageData.getHeight();
    }

    public Vec2[] getTexCoords() {
        return texCoords;
    }

    /**
     * A unique ID for this texture in OpenGL.
     *
     * @return the texture id
     */
    public int getTexID() {
        return texID;
    }

}

