package mayonez.graphics.textures;

import mayonez.*;
import mayonez.annotations.*;
import mayonez.io.image.*;
import mayonez.math.*;
import mayonez.math.shapes.*;
import org.lwjgl.BufferUtils;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.IntBuffer;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL13.GL_TEXTURE0;
import static org.lwjgl.opengl.GL13.glActiveTexture;
import static org.lwjgl.stb.STBImage.*;
import static org.lwjgl.system.MemoryUtil.memSlice;

/**
 * An image file used by the GL engine.
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
public final class GLTexture extends Texture {

    // Image Data Fields
    private ByteBuffer image;
    private int width, height, channels;

    // GPU Fields
    private int texID;
    private final Vec2[] texCoords;
    public static final Vec2[] DEFAULT_TEX_COORDS = Rectangle.rectangleVertices(new Vec2(0.5f), new Vec2(1f), 0f);

    /**
     * Create a brand-new GLTexture with the given filename.
     *
     * @param filename the file location
     */
    public GLTexture(String filename) {
        super(filename);
        texCoords = DEFAULT_TEX_COORDS;
        readImage();
        createTexture();
    }

    /**
     * Create a GLTexture from a portion of another texture.
     *
     * @param texture   another texture
     * @param texCoords the image region
     */
    public GLTexture(GLTexture texture, Vec2[] texCoords) {
        super(texture.getFilename());
        this.texID = texture.texID;
        this.texCoords = texCoords;

        this.image = texture.image;
        var size = texCoords[2].sub(texCoords[0]); // relative size
        this.width = (int) (texture.width * size.x); // get new image size
        this.height = (int) (texture.height * size.y);
        this.channels = texture.channels;
    }

    // Read Image File Methods

    @Override
    protected void readImage() {
        try {
            ByteBuffer imageBuffer = readImageBuffer();
            var width = BufferUtils.createIntBuffer(1);
            var height = BufferUtils.createIntBuffer(1);
            var channels = BufferUtils.createIntBuffer(1);

            getImageInfo(imageBuffer, width, height, channels);
            loadImage(imageBuffer, width, height, channels);
        } catch (Exception e) {
            Logger.error("Could not read image file \"%s\"", getFilename());
        }
    }

    private ByteBuffer readImageBuffer() throws TextureException, IOException {
        var imageData = new ImageIOManager().read(openInputStream());
        if (imageData == null) {
            throw new TextureException("Image data is null");
        }
        var imageBuffer = BufferUtils.createByteBuffer(imageData.length);
        imageBuffer = memSlice(imageBuffer.put(imageData).flip());
        return imageBuffer;
    }

    private void getImageInfo(ByteBuffer imageBuffer, IntBuffer width, IntBuffer height, IntBuffer channels)
            throws TextureException {
        if (!stbi_info_from_memory(imageBuffer, width, height, channels)) {
            throwExceptionOnFailure();
        } else {
            Logger.debug("OpenGL: Loaded image \"%s\"", getFilename());
        }

        this.width = width.get(0);
        this.height = height.get(0);
        this.channels = channels.get(0);
    }

    private void loadImage(ByteBuffer imageBuffer, IntBuffer width, IntBuffer height, IntBuffer channels)
            throws TextureException {
        stbi_set_flip_vertically_on_load(true); // GL uses (0,0) as bottom left, unlike AWT
        image = stbi_load_from_memory(imageBuffer, width, height, channels, 0);
        if (image == null) {
            Logger.error("OpenGL: Could not load image file \"%s\"", getFilename());
            throwExceptionOnFailure();
        }
    }

    private static void throwExceptionOnFailure() throws TextureException {
        String msg = "Reason for failure: " + stbi_failure_reason();
        Logger.error(msg);
        throw new TextureException(msg);
    }

    // Create Texture Methods

    private void createTexture() {
        generateTexID();
        setTextureParameters();
        uploadImage();
    }

    private void generateTexID() {
        texID = glGenTextures();
        glBindTexture(GL_TEXTURE_2D, texID);
    }

    private static void setTextureParameters() {
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_S, GL_REPEAT); // wrap if too big
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_T, GL_REPEAT);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_NEAREST); // pixelate when stretching
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_NEAREST); // pixelate when shrinking
    }

    private void uploadImage() {
        int format = getImageFormat();
        glTexImage2D(GL_TEXTURE_2D, 0, format, width, height, 0, format, GL_UNSIGNED_BYTE, image);
        Logger.debug("OpenGL: Loaded image \"%s\"", getFilename());
        stbi_image_free(image);
    }

    private int getImageFormat() {
        if (channels == 3) { // jpg image, no transparency
            return GL_RGB;
        } else { // png image, has transparency
            glEnable(GL_BLEND);
            glBlendFunc(GL_ONE, GL_ONE_MINUS_SRC_ALPHA);
            return GL_RGBA;
        }
    }

    // Render Batch Methods

    public void bind(int texSlot) {
        glActiveTexture(GL_TEXTURE0 + texSlot + 1);
        glBindTexture(GL_TEXTURE_2D, texID);
    }

    public void unbind() {
        glBindTexture(GL_TEXTURE_2D, 0);
    }

    // Image Data Methods
    // TODO recolor texture

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

}

