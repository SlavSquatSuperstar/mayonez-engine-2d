package slavsquatsuperstar.mayonez.io;

import org.lwjgl.BufferUtils;
import slavsquatsuperstar.mayonez.Logger;
import slavsquatsuperstar.mayonez.annotations.UsesEngine;
import slavsquatsuperstar.mayonez.annotations.EngineType;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.IntBuffer;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL13.GL_TEXTURE0;
import static org.lwjgl.opengl.GL13.glActiveTexture;
import static org.lwjgl.stb.STBImage.*;
import static org.lwjgl.system.MemoryUtil.memSlice;

/**
 * A image file used by the GL engine.
 * <br>
 * Sources:
 * <ul>
 *    <li>https://github.com/LWJGL/lwjgl3/blob/master/modules/samples/src/test/java/org/lwjgl/demo/stb/Image.java</li>
 *    <li>https://github.com/LWJGL/lwjgl3/blob/master/modules/samples/src/test/java/org/lwjgl/demo/util/IOUtil.java</li>
 *    <li>https://github.com/LWJGL/lwjgl3/blob/master/modules/samples/src/test/java/org/lwjgl/demo/glfw/GLFWUtil.java</li>
 * </ul>
 *
 * @author SlavSquatSuperstar
 */
@UsesEngine(EngineType.GL)
public class GLTexture extends Texture {

    private ByteBuffer image;
    private int width, height, channels;
    private int texID;

    public GLTexture(String filename) {
        super(filename);
        readImage();
        createTexture();
    }

    @Override
    protected void readImage() {
        try {
            byte[] imageData = IOUtils.readBytes(inputStream());
            ByteBuffer imageBuffer = BufferUtils.createByteBuffer(imageData.length);
            imageBuffer = memSlice(imageBuffer.put(imageData).flip());

            IntBuffer w = BufferUtils.createIntBuffer(1);
            IntBuffer h = BufferUtils.createIntBuffer(1);
            IntBuffer comp = BufferUtils.createIntBuffer(1);

            if (!stbi_info_from_memory(imageBuffer, w, h, comp)) showFailureMessage();
            else Logger.debug("OpenGL: Loaded image \"%s\"", getFilename());

            // Decode the image
            stbi_set_flip_vertically_on_load(true); // GL uses (0,0) as bottom left, unlike AWT
            image = stbi_load_from_memory(imageBuffer, w, h, comp, 0);
            if (image == null) {
                Logger.error("OpenGL: Could not load image file \"%s\"", getFilename());
                showFailureMessage();
            }

            width = w.get(0);
            height = h.get(0);
            channels = comp.get(0);
        } catch (IOException | NullPointerException e) {
            Logger.error("I/O: Could not read image file \"%s\"", getFilename());
        }
    }

    private void createTexture() {
        // Generate texture on CPU
        texID = glGenTextures();
        glBindTexture(GL_TEXTURE_2D, texID);

        // Set texture parameters
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_S, GL_REPEAT); // wrap if too big
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_T, GL_REPEAT);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_NEAREST); // pixelate when stretching
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_NEAREST); // pixelate when shrinking

        // Upload image to GPU
        int format;
        if (channels == 3) { // jpg image, no transparency
//            if ((width & 3) != 0)
//                glPixelStorei(GL_UNPACK_ALIGNMENT, 2 - (width & 1));
            format = GL_RGB;
        } else { // png image, has transparency
            glEnable(GL_BLEND);
            glBlendFunc(GL_ONE, GL_ONE_MINUS_SRC_ALPHA);
            format = GL_RGBA;
        }

        glTexImage2D(GL_TEXTURE_2D, 0, format, width, height, 0, format, GL_UNSIGNED_BYTE, image);
        Logger.debug("OpenGL: Loaded image \"%s\"", getFilename());
        stbi_image_free(image);
    }

    public void bind(int texSlot) {
        glActiveTexture(GL_TEXTURE0 + texSlot + 1);
        glBindTexture(GL_TEXTURE_2D, texID);
    }

    public void unbind() {
        glBindTexture(GL_TEXTURE_2D, 0);
    }

    @Override
    public int getWidth() {
        return width;
    }

    @Override
    public int getHeight() {
        return height;
    }

    private static void showFailureMessage() throws RuntimeException {
        Logger.error("Reason for failure: " + stbi_failure_reason());
        throw new RuntimeException("Reason for failure: " + stbi_failure_reason());
    }

}

