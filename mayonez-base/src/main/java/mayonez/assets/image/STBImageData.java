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
public class STBImageData extends ImageData {

    // Constants
    private static final int SELECT_8_BYTES = 0xFF;

    // Fields
    private final ByteBuffer buffer;
    private int width, height, channels;
    private boolean alpha, imageFreed;

    public STBImageData(String filename) throws IOException {
        super(filename);
        try {
            var fileBuffer = readImageBytes();
            buffer = loadImage(fileBuffer);
        } catch (ImageReadException | IOException e) {
            throw new IOException("Error reading STB image");
        }
    }

    public STBImageData(String filename, ByteBuffer buffer, int width, int height) throws IOException {
        super(filename);
        this.buffer = buffer;
        this.width = width;
        this.height = height;
        try {
            channels = buffer.capacity() / (width * height);
            alpha = getAlphaFromChannels(channels);
            imageFreed = false;
        } catch (ImageReadException e) {
            throw new IOException("Creating reading STB image");
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

    private ByteBuffer loadImage(ByteBuffer fileBuffer) throws ImageReadException {
        var widthBuff = BufferUtils.createIntBuffer(1);
        var heightBuff = BufferUtils.createIntBuffer(1);
        var channelsBuff = BufferUtils.createIntBuffer(1);

        stbi_set_flip_vertically_on_load(true); // GL uses (0,0) as bottom left, unlike AWT
        var image = stbi_load_from_memory(fileBuffer, widthBuff, heightBuff, channelsBuff, 0);
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
        alpha = getAlphaFromChannels(channels);

        imageFreed = false;
        return image;
    }

    private static boolean getAlphaFromChannels(int channels) throws ImageReadException {
        boolean alpha;
        switch (channels) {
            case RGB_CHANNELS -> alpha = false;
            case RGBA_CHANNELS -> alpha = true;
            default -> throw new ImageReadException("Unsupported number of channels: " + channels);
        }
        return alpha;
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

    // Sub-Image Methods

    public ByteBuffer getBuffer() {
        return buffer;
    }

    public ByteBuffer getSubBuffer(Vec2 topLeft, Vec2 size) {
        var subImgX = (int) topLeft.x;
        var subImgY = (int) topLeft.y;
        var subImgWidth = (int) size.x;
        var subImgHeight = (int) size.y;

        var buffer = BufferUtils.createByteBuffer(subImgWidth * subImgHeight * channels);
        for (var y = subImgY; y < subImgY + subImgHeight; y++) {
            for (var x = subImgX; x < subImgX + subImgWidth; x++) {
                var flippedY = (height - 1) - y;
                var color = getPixelColor(x, flippedY);
                buffer.put((byte) color.getRed());
                buffer.put((byte) color.getGreen());
                buffer.put((byte) color.getBlue());
                if (alpha) buffer.put((byte) color.getAlpha());
            }
        }
        return buffer;
    }

    @Override
    public STBImageData getSubImageData(Vec2 topLeft, Vec2 size) {
        try {
            var filename = "%s Sub-Image (%s, %s)".formatted(getFilename(), topLeft, size);
            return new STBImageData(filename, getSubBuffer(topLeft, size), (int) size.x, (int) size.y);
        } catch (IOException e) {
            Logger.error("Could not create sub-image from %s with position %s and size %s",
                    toString(), topLeft, size);
            return null;
        }
    }

}
