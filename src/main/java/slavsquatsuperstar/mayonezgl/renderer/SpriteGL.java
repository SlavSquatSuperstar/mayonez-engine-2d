package slavsquatsuperstar.mayonezgl.renderer;

import org.lwjgl.BufferUtils;
import slavsquatsuperstar.mayonez.Logger;

import java.nio.ByteBuffer;
import java.nio.IntBuffer;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL13.GL_TEXTURE0;
import static org.lwjgl.opengl.GL13.glActiveTexture;
import static org.lwjgl.stb.STBImage.*;

public class SpriteGL {

    private String filename;
    private int texID;

    public SpriteGL(String filename) {
        this.filename = filename;
        create();
    }

    private void create() {
        // Generate texture on CPU
        texID = glGenTextures();
        glBindTexture(GL_TEXTURE_2D, texID);

        // Set texture parameters
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_S, GL_REPEAT); // wrap if too big
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_T, GL_REPEAT);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_NEAREST); // pixelate when stretching
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_NEAREST); // pixelate when shrinking

        IntBuffer width = BufferUtils.createIntBuffer(1);
        IntBuffer height = BufferUtils.createIntBuffer(1);
        IntBuffer channels = BufferUtils.createIntBuffer(1);

        // Upload image to GPU
        stbi_set_flip_vertically_on_load(true);
        ByteBuffer image = stbi_load(filename, width, height, channels, 0);
        if (image != null) {
            int imgType;
            switch (channels.get(0)) {
                case 3:
                    imgType = GL_RGB;
                    break;
                case 4:
                    imgType = GL_RGBA;
                    break;
                default:
                    Logger.warn("Sprite: Unknown number of channels \"%d\"", channels.get(0));
                    return;
            }
            glTexImage2D(GL_TEXTURE_2D, 0, imgType, width.get(0), height.get(0),
                    0, imgType, GL_UNSIGNED_BYTE, image);
            Logger.log("Sprite: Loaded image \"%s\"", filename);
            stbi_image_free(image);
        } else {
            Logger.warn("Sprite: Could not load image \"%s\"", filename);
        }
    }

    public void bind() {
        glActiveTexture(GL_TEXTURE0);
        glBindTexture(GL_TEXTURE_2D, texID);
    }

    public void unbind() {
        glBindTexture(GL_TEXTURE_2D, 0);
    }

}
