package mayonez.assets.image;

import mayonez.*;
import mayonez.assets.*;
import mayonez.io.image.*;
import org.lwjgl.BufferUtils;

import java.io.IOException;
import java.nio.ByteBuffer;

import static org.lwjgl.stb.STBImage.*;
import static org.lwjgl.system.MemoryUtil.memSlice;

/**
 * Stores the byte buffer contents of an image file or resource read by {@link org.lwjgl.stb.STBImage}.
 *
 * @author SlavSquatSuperstar
 */
public class STBImageData extends Asset {

    // Constants
    private static final int RGB_CHANNELS = 3;
    private static final int RGBA_CHANNELS = 4;

    // Fields
    private final ByteBuffer buffer;
    private int width, height;
    private boolean alpha, imageFreed;

    public STBImageData(String filename) throws IOException {
        super(filename);
        try {
            var imageBuffer = readImageBytes();
            buffer = loadImage(imageBuffer);
        } catch (ImageReadException | IOException e) {
            throw new IOException("Error reading STB image");
        }
    }

    // Read Image Methods

    private ByteBuffer readImageBytes() throws ImageReadException, IOException {
        var imageBytes = new ImageIOManager().read(openInputStream());
        if (imageBytes == null) {
            throw new ImageReadException("Image byte array is null");
        }

        var imageBuffer = BufferUtils.createByteBuffer(imageBytes.length);
        return memSlice(imageBuffer.put(imageBytes).flip());
    }

    private ByteBuffer loadImage(ByteBuffer imageBuffer) throws ImageReadException {
        var widthBuff = BufferUtils.createIntBuffer(1);
        var heightBuff = BufferUtils.createIntBuffer(1);
        var channelsBuff = BufferUtils.createIntBuffer(1);

        stbi_set_flip_vertically_on_load(true); // GL uses (0,0) as bottom left, unlike AWT
        var image = stbi_load_from_memory(imageBuffer, widthBuff, heightBuff, channelsBuff, 0);
        if (image == null) {
            Logger.error("OpenGL: Could not load image file %s", getFilename());
            String msg = "Reason for failure: " + stbi_failure_reason();
            Logger.error(msg);
            throw new ImageReadException(msg);
        }

        // Set image info
        width = widthBuff.get(0);
        height = heightBuff.get(0);

        // Set image alpha
        var channels = channelsBuff.get(0);
        switch (channels) {
            case RGB_CHANNELS -> alpha = false;
            case RGBA_CHANNELS -> alpha = true;
            default -> throw new ImageReadException("Unsupported number of channels: " + channels);
        }

        imageFreed = false;
        return image;
    }

    public void freeImage() {
        if (!imageFreed) { // stop Java from crashing if memory freed
            stbi_image_free(buffer);
            imageFreed = true;
        }
    }

    // Image Getters

    public ByteBuffer getBuffer() {
        return buffer;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public boolean hasAlpha() {
        return alpha;
    }

    // TODO Pixel Methods

}
