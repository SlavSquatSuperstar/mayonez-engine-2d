package mayonez.graphics.batch;

import mayonez.graphics.*;
import mayonez.graphics.textures.*;

import java.util.*;

import static org.lwjgl.opengl.GL11.GL_TEXTURE_2D;
import static org.lwjgl.opengl.GL11.glBindTexture;
import static org.lwjgl.opengl.GL13.GL_TEXTURE0;
import static org.lwjgl.opengl.GL13.glActiveTexture;

/**
 * Stores several {@link mayonez.graphics.textures.GLTexture} texture IDs  for a
 * {@link RenderBatch}.
 *
 * @author SlavSquatSuperstar
 */
@UsesEngine(EngineType.GL)
class TextureArray {

    // Constants
    private static final GLTexture COLOR_TEXTURE = null;
    private static final int NO_TEXTURE_ID = 0;

    // Array Fields
    private final int[] texIDs;
    private int size; // Current number of textures

    TextureArray(int textureCapacity) {
        this.texIDs = new int[textureCapacity];
        size = 0;
    }

    // Texture Methods

    void clear() {
        Arrays.fill(texIDs, NO_TEXTURE_ID);
        size = 0;
    }

    void addTexture(GLTexture tex) {
        if (size < capacity()) {
            texIDs[size++] = tex.getTexID();
        }
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
            if (texIDs[i] == tex.getTexID()) return i;
        }
        return -1;
    }

    // Bind Methods

    void bindTextures() {
        for (var i = 0; i < size; i++) {
            glActiveTexture(GL_TEXTURE0 + i + 1); // count from 1
            glBindTexture(GL_TEXTURE_2D, texIDs[i]);
        }
    }

    void unbindTextures() {
        glBindTexture(GL_TEXTURE_2D, NO_TEXTURE_ID);
    }

    // Array Getters

    boolean hasRoom() {
        return size < texIDs.length;
    }

    int capacity() {
        return texIDs.length;
    }

}
