package mayonez.io

import mayonez.Preferences.textCharset
import java.io.*
import java.nio.charset.Charset
import java.nio.charset.StandardCharsets
import java.nio.charset.UnsupportedCharsetException

/**
 * Contains reusable methods from reading from and writing to classpath or external assets.
 *
 * @author SlavSquatSuperstar
 */
object IOUtils {

    private val DEFAULT_CHARSET = StandardCharsets.UTF_8
    private const val LINE_ENDING = "\n"

    // I/O Stream Methods

    @JvmStatic
    fun openInputStream(filename: String): InputStream? =
        Assets.getAsset(filename)?.inputStream()

    @JvmStatic
    fun openOutputStream(filename: String, append: Boolean): OutputStream? =
        Assets.getAsset(filename)?.outputStream(append)

    // Read Methods

    /**
     * Reads a single block of text from a [java.io.InputStream].
     *
     * Referenced from Apache Commons IO > IOUtils.readLines(InputStream, Charset).
     *
     * @throws FileNotFoundException if the file does not exist
     * @throws IOException if the file cannot be read
     * @param input a file's input stream
     * @return a text string
     */
    @JvmStatic
    @Throws(IOException::class, FileNotFoundException::class)
    fun readString(input: InputStream?): String {
        if (input == null) throw FileNotFoundException("Input stream is null")
        val reader = BufferedReader(InputStreamReader(input, getCharset(textCharset)))
        return reader.readText()
    }

    /**
     * Reads multiple lines of text from a [java.io.InputStream].
     *
     * @throws FileNotFoundException if the file does not exist
     * @throws IOException if the file cannot be read
     *
     * @param input a file's input stream
     * @return an array of strings
     */
    @JvmStatic
    @Throws(IOException::class, FileNotFoundException::class)
    fun readLines(input: InputStream?): Array<String> {
        if (input == null) throw FileNotFoundException("Input stream is null")
        val reader = BufferedReader(InputStreamReader(input, getCharset(textCharset)))
        return reader.readLines().toTypedArray()
    }

    /**
     * Reads a sequence of bytes from a [java.io.InputStream].
     *
     * @throws IOException if the file cannot be read
     * @param input a file's input stream
     * @return an array of bytes
     */
    @JvmStatic
    @Throws(IOException::class)
    fun readBytes(input: InputStream?): ByteArray? = input?.readAllBytes()

    // Write Methods

    /**
     * Saves a single block of text from a [java.io.OutputStream].
     *
     * Referenced from Apache Commons IO > IOUtils.write(String, OutputStream, Charset)
     *
     * @throws IOException if the file cannot be written to
     * @param output a file's output stream, set to write or append
     * @param text   a text string
     */
    @JvmStatic
    @Throws(IOException::class)
    fun writeString(output: OutputStream?, text: String?) {
        if (output == null) throw FileNotFoundException("Output stream is null")
        if (text != null) output.write(text.toByteArray(getCharset(textCharset)))
        output.write(LINE_ENDING.toByteArray(getCharset(textCharset)))
    }

    /**
     * Saves multiple lines of text to a [java.io.OutputStream].
     *
     * Referenced from Apache Commons IO > IOUtils.write(Collection<?>, String, OutputStream, Charset)
     *
     * @throws IOException if the file cannot be written to
     * @param output a file's output stream, set to write or append
     * @param lines  an array of strings
     */
    @JvmStatic
    @Throws(IOException::class)
    fun writeLines(output: OutputStream?, lines: Array<String?>?) {
        if (output == null) throw FileNotFoundException("Output stream is null")
        if (lines == null) return
        for (line in lines) {
            if (line != null) output.write(line.toString().toByteArray(getCharset(textCharset)))
            output.write(LINE_ENDING.toByteArray(getCharset(textCharset)))
        }
    }

    /**
     * Saves a sequence of bytes to a [java.io.OutputStream].
     *
     * @throws IOException if the file cannot be written to
     * @param output a file's output stream, set to write or append
     * @param bytes  an array of bytes
     */
    @JvmStatic
    @Throws(IOException::class)
    fun writeBytes(output: OutputStream?, bytes: ByteArray?) {
        output?.write(bytes ?: return)
    }

    private fun getCharset(charsetName: String?): Charset {
        return try {
            Charset.forName(charsetName)
        } catch (e: UnsupportedCharsetException) {
            DEFAULT_CHARSET
        }
    }

}