package mayonez.graphics.textures;

import mayonez.*;
import mayonez.graphics.*;
import mayonez.io.image.*;
import mayonez.math.*;
import mayonez.math.shapes.*;
import org.lwjgl.BufferUtils;

import java.io.IOException;
import java.nio.ByteBuffer;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL12.GL_TEXTURE_WRAP_R;
import static org.lwjgl.stb.STBImage.*;
import static org.lwjgl.system.MemoryUtil.memSlice;

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
    private static final int RGB_CHANNELS = 3;
    private static final int RGBA_CHANNELS = 4;

    // Image Data Fields
    private ByteBuffer image;
    private int width, height, channels;
    private boolean imageFreed;

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
        imageFreed = false;
    }

    // Read Image Methods

    @Override
    protected void readImage() {
        try {
            generateTexID();
            setTextureParameters();
            var imageBuffer = readImageBytes();
            image = loadImage(imageBuffer);
            uploadImage(image);
        } catch (TextureException | IOException e) {
            Logger.error("Could not read image file %s", getFilename());
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

    private ByteBuffer readImageBytes() throws TextureException, IOException {
        var imageBytes = new ImageIOManager().read(openInputStream());
        if (imageBytes == null) {
            throw new TextureException("Image data is null");
        }

        var imageBuffer = BufferUtils.createByteBuffer(imageBytes.length);
        return memSlice(imageBuffer.put(imageBytes).flip());
    }

    private ByteBuffer loadImage(ByteBuffer imageBuffer) throws TextureException {
        var width = BufferUtils.createIntBuffer(1);
        var height = BufferUtils.createIntBuffer(1);
        var channels = BufferUtils.createIntBuffer(1);

        stbi_set_flip_vertically_on_load(true); // GL uses (0,0) as bottom left, unlike AWT
        var image = stbi_load_from_memory(imageBuffer, width, height, channels, 0);

        if (image == null) {
            Logger.error("OpenGL: Could not load image file %s", getFilename());
            throwExceptionOnSTBFailure();
        }

        // Set image info
        this.width = width.get(0);
        this.height = height.get(0);
        this.channels = channels.get(0);
        return image;
    }

    private static void throwExceptionOnSTBFailure() throws TextureException {
        String msg = "Reason for failure: " + stbi_failure_reason();
        Logger.error(msg);
        throw new TextureException(msg);
    }

    private void uploadImage(ByteBuffer image) {
        int format = getImageFormat(channels);
        glTexImage2D(GL_TEXTURE_2D, 0, format, width, height, 0, format, GL_UNSIGNED_BYTE, image);
        freeImage(image);
    }

    private void freeImage(ByteBuffer image) throws TextureException {
        if (!imageFreed) { // stop Java from crashing if memory freed
            stbi_image_free(image);
            imageFreed = true;
        }
    }

    private static int getImageFormat(int channels) throws TextureException {
        switch (channels) {
            case RGB_CHANNELS -> {
                return GL_RGB;
            }
            case RGBA_CHANNELS -> {
                return GL_RGBA;
            }
            default -> throw new TextureException("Invalid number of channels: " + channels);
        }
    }

    // Asset Methods

    @Override
    public void free() {
        glDeleteTextures(texID);
    }

    // Image Getters

    @Override
    public int getWidth() {
        return width;
    }

    @Override
    public int getHeight() {
        return height;
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

