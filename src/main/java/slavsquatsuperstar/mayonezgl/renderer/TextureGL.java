package slavsquatsuperstar.mayonezgl.renderer;

import org.lwjgl.BufferUtils;
import slavsquatsuperstar.fileio.Assets;
import slavsquatsuperstar.mayonez.Logger;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.IntBuffer;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL13.GL_TEXTURE0;
import static org.lwjgl.opengl.GL13.glActiveTexture;
import static org.lwjgl.stb.STBImage.*;
import static org.lwjgl.system.MemoryUtil.memSlice;

/**
 * A LWJGL image file used by this program.<br> Sources:
 * <ul>
 *    <li>https://github.com/LWJGL/lwjgl3/blob/master/modules/samples/src/test/java/org/lwjgl/demo/stb/Image.java</li>
 *    <li>https://github.com/LWJGL/lwjgl3/blob/master/modules/samples/src/test/java/org/lwjgl/demo/util/IOUtil.java</li>
 *    <li>https://github.com/LWJGL/lwjgl3/blob/master/modules/samples/src/test/java/org/lwjgl/demo/glfw/GLFWUtil.java</li>
 * </ul>
 *
 * @author SlavSquatSuperstar
 */
public class TextureGL {

    private ByteBuffer image;
    private String filename;
    private int width, height, channels;
    private int texID;

    public TextureGL(String filename) {
        this.filename = filename;
        readImage();
        createTexture();
    }

    private void readImage() {
        try {
            byte[] imageData = Assets.readContents(filename);
            ByteBuffer imageBuffer = BufferUtils.createByteBuffer(imageData.length);
            imageBuffer = memSlice(imageBuffer.put(imageData).flip());

            IntBuffer w = BufferUtils.createIntBuffer(1);
            IntBuffer h = BufferUtils.createIntBuffer(1);
            IntBuffer ch = BufferUtils.createIntBuffer(1);

            if (!stbi_info_from_memory(imageBuffer, w, h, ch))
                throw new RuntimeException("Failed to read image information: " + stbi_failure_reason());
            else
                System.out.println("OK with reason: " + stbi_failure_reason());

            System.out.println("Image width: " + w.get(0));
            System.out.println("Image height: " + h.get(0));
            System.out.println("Image components: " + ch.get(0));
            System.out.println("Image is HDR: " + stbi_is_hdr_from_memory(imageBuffer));

            // Decode the image
            stbi_set_flip_vertically_on_load(true);
            image = stbi_load_from_memory(imageBuffer, w, h, ch, 0);
            if (image == null) {
                Logger.warn("Sprite: Could not load image \"%s\"", filename);
                Logger.warn("GL: " + stbi_failure_reason());
                throw new RuntimeException("Failed to load image: " + stbi_failure_reason());
            }

            width = w.get(0);
            height = h.get(0);
            channels = ch.get(0);
        } catch (IOException | NullPointerException e) {
            Logger.log("Could not read image \"%s\"", filename);
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
        Logger.log("Sprite: Loaded image \"%s\"", filename);
        stbi_image_free(image);
    }

    public void bind(int texSlot) {
        glActiveTexture(GL_TEXTURE0 + texSlot + 1);
        glBindTexture(GL_TEXTURE_2D, texID);
    }

    public void unbind() {
        glBindTexture(GL_TEXTURE_2D, 0);
    }

}
