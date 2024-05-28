package mayonez.assets.image;

import mayonez.*;
import mayonez.graphics.*;
import mayonez.io.image.*;
import mayonez.math.*;
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
public class STBImageData extends BaseImageData {

    // Constants
    private static final int SELECT_8_BYTES = 0xFF;

    // Fields
    private final ByteBuffer buffer;
    private int width, height, channels;
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
        channels = channelsBuff.get(0);
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

    @Override
    public void free() {
        freeImage();
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

    @Override
    public int getChannels() {
        return channels;
    }

    @Override
    public boolean hasAlpha() {
        return alpha;
    }

    // ByteBuffer Methods

    public ByteBuffer getBuffer() {
        return buffer;
    }

    // TODO get sub image

    // Pixel Methods

    @Override
    public Color getPixelColor(int x, int y) {
        var flippedY = (height - 1) - y;
        var index = (x + flippedY * width) * channels;
        int r = buffer.get(index) & SELECT_8_BYTES;
        int g = buffer.get(index + 1) & SELECT_8_BYTES;
        int b = buffer.get(index + 2) & SELECT_8_BYTES;
        int a = alpha ? buffer.get(index + 3) & SELECT_8_BYTES : 255;
        return new Color(r, g, b, a);
    }

    @Override
    public void setPixelColor(int x, int y, Color color) {
        var flippedY = (height - 1) - y;
        var index = (x + flippedY * width) * channels;
        buffer.put(index, (byte) color.getRed());
        buffer.put(index + 1, (byte) color.getRed());
        buffer.put(index + 2, (byte) color.getBlue());
        if (alpha) buffer.put(index + 3, (byte) color.getAlpha());
    }

}
