package mayonez.graphics.textures;

import mayonez.*;
import mayonez.assets.image.*;
import mayonez.graphics.*;
import mayonez.math.*;
import mayonez.math.shapes.*;
import org.lwjgl.BufferUtils;

import java.io.IOException;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL12.GL_TEXTURE_WRAP_R;

/**
 * An image file used by the GL engine. This class should not be directly
 * instantiated. Instead, call {@link mayonez.graphics.textures.Textures#getTexture}.
 * See {@link mayonez.graphics.textures.Texture} for more information.
 * <p>
 * Sources:
 * <ul>
 *    <li><a href="https://github.com/LWJGL/lwjgl3/blob/master/modules/samples/src/test/java/org/lwjgl/demo/stb/Image.java">
 *        GitHub - org.lwjgl.demo.stb.Image</a></li>
 *    <li><a href="https://github.com/LWJGL/lwjgl3/blob/master/modules/samples/src/test/java/org/lwjgl/demo/util/IOUtil.java">
 *        GitHub - org.lwjgl.demo.stb.IOUtil</a></li>
 *    <li><a href="https://github.com/LWJGL/lwjgl3/blob/master/modules/samples/src/test/java/org/lwjgl/demo/glfw/GLFWUtil.java">
 *        GitHub - org.lwjgl.demo.glfw.GLFWUtil</a></li>
 * </ul>
 *
 * @author SlavSquatSuperstar
 */
@UsesEngine(EngineType.GL)
public final class GLTexture extends Texture {

    // Constants
    public static final Vec2[] DEFAULT_TEX_COORDS
            = Rectangle.rectangleVerticesMinMax(new Vec2(0f), new Vec2(1f));

    // Image Data Fields
    private final STBImageData imageData;
    private final int width, height;
    private final GLTexture parentTexture;

    // GPU Fields
    private int texID;
    private final Vec2[] texCoords;

    /**
     * Create a brand-new GLTexture with the given filename.
     *
     * @param filename the file location
     */
    @SuppressWarnings("unused") // Needed for Assets.getGLTexture()
    public GLTexture(String filename) {
        super(filename);
        imageData = readImage();
        if (imageData != null) {
            width = imageData.getWidth();
            height = imageData.getHeight();
        } else {
            width = 0;
            height = 0;
        }
        parentTexture = null;
        texID = createTexture();
        texCoords = DEFAULT_TEX_COORDS;
    }

    /**
     * Create a GLTexture from a portion of another texture.
     *
     * @param parentTexture the parent texture
     * @param region        the sub-image region
     * @param description   the description of the sub-image
     */
    private GLTexture(GLTexture parentTexture, ImageRegion region, String description) {
        super("%s (%s)".formatted(parentTexture.getFilename(), description));
        // Parent buffer is already freed if not testing
        this.imageData = parentTexture.getImageData().getSubImageData(region); // Crop image data
        // Get new image size in px
        this.width = region.getWidth();
        this.height = region.getHeight();
        this.parentTexture = parentTexture;

        this.texID = parentTexture.texID;
        this.texCoords = getSubImageCoords(parentTexture.getSize(), region);
    }

    // Read Image Methods

    @Override
    protected STBImageData readImage() {
        try {
            // Read image from file
            var imageData = new STBImageData(getFilename());
            Logger.debug("Loaded image %s", getFilename());
            return imageData;
        } catch (IOException e) {
            Logger.error("Could not read image file %s", getFilename());
            Logger.printStackTrace(e);
            return null;
        }
    }

    private int createTexture() {
        // Create Texture on GPU
        if (imageData != null && GLHelper.isGLInitialized()) {
            var texID = glGenTextures();
            glBindTexture(GL_TEXTURE_2D, texID);
            uploadImageToTexture(imageData, texID);
            return texID;
        } else {
            // Make sure GL tests don't crash
            return GL_NONE;
        }
    }

    private static void setTextureParameters() {
        // Wrap if too big
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_R, GL_REPEAT);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_S, GL_REPEAT);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_T, GL_REPEAT);
        // Pixelate when scaling
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_NEAREST);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_NEAREST);
    }

    private static void uploadImageToTexture(STBImageData imageData, int texID) {
        setTextureParameters();
        glBindTexture(GL_TEXTURE_2D, texID);

        int format = imageData.hasAlpha() ? GL_RGBA : GL_RGB;
        glTexImage2D(GL_TEXTURE_2D, 0, format, imageData.getWidth(), imageData.getHeight(),
                0, format, GL_UNSIGNED_BYTE, imageData.getBuffer());
        imageData.freeImage();
    }

    // TODO can't test without initializing GL
    public STBImageData getImageFromTexture() {
        // Save texture into buffer
        glBindTexture(GL_TEXTURE_2D, texID);
        var buffer = BufferUtils.createByteBuffer(getWidth() * getHeight() * imageData.getChannels());
        glGetTexImage(GL_TEXTURE_2D, 0, GL_RGBA, GL_UNSIGNED_BYTE, buffer); // TODO check rgba or rgb
        try {
            // Create image data
            // Set dimensions in case we have tex coords
            return new STBImageData(getFilename(), buffer, getWidth(), getHeight());
        } catch (IOException e) {
            Logger.error("Could get image data from texture %s", toString());
            return null;
        }
    }

    // Asset Methods

    @Override
    public void free() {
        if (getParentTexture() != null) return; // Don't free parent texture
        if (texID != GL_NONE && GLHelper.isGLInitialized()) {
            glDeleteTextures(texID);
            texID = GL_NONE;
        }
    }

    // Image Getters

    @Override
    public GLTexture getSubTexture(ImageRegion region, String description) {
        return new GLTexture(this, region, description);
    }

    @Override
    public STBImageData getImageData() {
        return imageData;
    }

    @Override
    public GLTexture getParentTexture() {
        return parentTexture;
    }

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

    // Helper Methods

    private static Vec2[] getSubImageCoords(Vec2 sheetSize, ImageRegion region) {
        // Normalize image coordinates to between 0-1
        var subImgMin = region.origin().div(sheetSize);
        var subImgMax = region.origin().add(region.size()).div(sheetSize);
        return Rectangle.rectangleVerticesMinMax(subImgMin, subImgMax);
    }

}

