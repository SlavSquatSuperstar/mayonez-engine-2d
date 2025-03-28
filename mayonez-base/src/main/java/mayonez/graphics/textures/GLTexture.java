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
public sealed class GLTexture extends Texture permits GLSpriteSheetTexture {

    // Constants
    public static final Vec2[] DEFAULT_TEX_COORDS
            = Rectangle.rectangleVerticesMinMax(new Vec2(0f), new Vec2(1f));

    // Image Fields
    private final STBImageData imageData;
    private int texID;
    private final Vec2[] texCoords;

    /**
     * Create a brand-new GLTexture with the given filename.
     *
     * @param filename the file location
     */
    public GLTexture(String filename) {
        super(filename);
        texCoords = DEFAULT_TEX_COORDS;
        imageData = readImage();
        createTexture();
    }

    /**
     * Create a GLTexture from a portion of another texture.
     *
     * @param filename      the file location
     * @param parentTexture the parent texture
     * @param region        the sub-image region
     */
    protected GLTexture(String filename, GLTexture parentTexture, ImageRegion region) {
        super(filename);
        this.imageData = parentTexture.imageData.getSubImageData(region); // Crop image data
        System.out.println(parentTexture.imageData.isImageFreed());
        // Error: parent buffer has already been freed
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

    private void createTexture() {
        // Create Texture on GPU
        if (imageData != null && GLHelper.isGLInitialized()) {
            texID = glGenTextures();
            glBindTexture(GL_TEXTURE_2D, texID);
            uploadImageToTexture(imageData, texID);
        } else {
            texID = GL_NONE;
        }
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
        if (texID != GL_NONE && GLHelper.isGLInitialized()) {
            glDeleteTextures(texID);
            texID = GL_NONE;
        }
    }

    // Image Getters

    @Override
    public GLTexture getSubTexture(ImageRegion region, String description) {
        return new GLSpriteSheetTexture(this, region, description);
    }

    @Override
    public STBImageData getImageData() {
        return imageData;
    }

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

    // Helper Methods

    private static Vec2[] getSubImageCoords(Vec2 sheetSize, ImageRegion region) {
        // Normalize image coordinates to between 0-1
        var subImgMin = region.origin().div(sheetSize);
        var subImgMax = region.origin().add(region.size()).div(sheetSize);
        return Rectangle.rectangleVerticesMinMax(subImgMin, subImgMax);
    }

}

