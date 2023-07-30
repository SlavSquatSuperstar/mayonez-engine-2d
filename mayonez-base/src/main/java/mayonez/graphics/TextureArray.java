package mayonez.graphics;

import mayonez.annotations.*;
import mayonez.graphics.textures.*;

import java.util.*;

import static org.lwjgl.opengl.GL11.GL_TEXTURE_2D;
import static org.lwjgl.opengl.GL11.glBindTexture;
import static org.lwjgl.opengl.GL13.GL_TEXTURE0;
import static org.lwjgl.opengl.GL13.glActiveTexture;

/**
 * An array storing {@link mayonez.graphics.textures.GLTexture} objects for a
 * {@link mayonez.graphics.RenderBatch}.
 *
 * @author SlavSquatSuperstar
 */
@UsesEngine(EngineType.GL)
class TextureArray {

    private static final GLTexture COLOR_TEXTURE = null;
    private static final int NO_TEXTURE_ID = 0;

    // TODO store texID instead of objects
    private final GLTexture[] textures;
    private int size; // Current number of textures

    TextureArray(int textureCapacity) {
        this.textures = new GLTexture[textureCapacity];
        size = 0;
    }

    // Texture Methods

    void clear() {
        Arrays.fill(textures, null);
        size = 0;
    }

    void addTexture(GLTexture tex) {
        if (size < capacity()) textures[size++] = tex;
    }

    boolean containsTexture(GLTexture tex) {
        if (tex == COLOR_TEXTURE) return true;
        else return indexOfTexture(tex) > -1;
    }

    int getTextureID(GLTexture tex) {
        if (tex == COLOR_TEXTURE) return NO_TEXTURE_ID;
        else return indexOfTexture(tex) + 1;
    }

    private int indexOfTexture(GLTexture tex) {
        for (var i = 0; i < size; i++) {
            if (textures[i].equals(tex)) return i;
        }
        return -1;
    }

    // Bind Methods

    void bindTextures() {
        for (var i = 0; i < size; i++) {
            glActiveTexture(GL_TEXTURE0 + i + 1); // count from 1
            glBindTexture(GL_TEXTURE_2D, textures[i].getTexID());
        }
    }

    void unbindTextures() {
        glBindTexture(GL_TEXTURE_2D, NO_TEXTURE_ID);
    }

    // Array Getters

    boolean hasRoom() {
        return size < textures.length;
    }

    int capacity() {
        return textures.length;
    }

}
