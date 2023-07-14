package mayonez.io.image;

import mayonez.io.*;

import java.io.*;

/**
 * Reads and writes byte arrays to images.
 *
 * @author SlavSquatSuperstar
 */
public class ImageIOManager implements AssetReader<byte[]>, AssetWriter<byte[]> {

    @Override
    public byte[] read(InputStream input) throws IOException {
        if (input == null) {
            throw new FileNotFoundException("File does not exist");
        }

        try {
            return input.readAllBytes();
        } catch (IOException e) {
            throw new IOException("Error while reading image");
        }
    }

    @Override
    public void write(OutputStream output, byte[] bytes) throws IOException {
        if (output == null) {
            throw new FileNotFoundException("File does not exist");
        }
        if (bytes == null) return;

        try {
            output.write(bytes);
        } catch (IOException e) {
            throw new IOException("Error while writing to image");
        }
    }

}
