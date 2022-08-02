package slavsquatsuperstar.mayonez.io

import slavsquatsuperstar.mayonez.Preferences.textCharset
import slavsquatsuperstar.util.StringUtils
import java.io.*
import java.nio.charset.Charset
import java.nio.charset.StandardCharsets
import java.nio.charset.UnsupportedCharsetException

/**
 * Contains reusable methods from reading from and writing to assets.
 *
 * @author SlavSquatSuperstar
 */
object IOUtils {

    private val DEFAULT_CHARSET = StandardCharsets.UTF_8
    private const val LINE_ENDING = "\n"

    // todo bytes

    // I/O Stream Methods

    @JvmStatic
    fun openInputStream(filename: String): InputStream? =
        Assets.getAsset(filename)?.inputStream()

    @JvmStatic
    fun openOutputStream(filename: String, append: Boolean): OutputStream? =
        Assets.getAsset(filename)?.outputStream(append)

    // Read Methods

    @JvmStatic
    @Throws(IOException::class, FileNotFoundException::class)
    fun read(input: InputStream?): String = StringUtils.fromLines(readLines(input))

    // From Apache Commons IO > IOUtils > readLines(InputStream, Charset)
    @JvmStatic
    @Throws(IOException::class, FileNotFoundException::class)
    fun readLines(input: InputStream?): Array<String> {
        if (input == null) throw FileNotFoundException("Input stream is null")
        val reader = BufferedReader(InputStreamReader(input, getCharset(textCharset)))
        return reader.readLines().toTypedArray()
    }

    @JvmStatic
    @Throws(IOException::class)
    fun readBytes(input: InputStream?): ByteArray? = input?.readAllBytes()

    // Write Methods

    // From Apache Commons IO > IOUtils > write(String, OutputStream, Charset)
    @JvmStatic
    @Throws(IOException::class)
    fun write(output: OutputStream?, text: String?) {
        if (output == null) throw FileNotFoundException("Output stream is null")
        if (text != null) output.write(text.toByteArray(getCharset(textCharset)))
        output.write(LINE_ENDING.toByteArray(getCharset(textCharset)))
    }

    // From Apache Commons IO > IOUtils > write(Collection<?>, String, OutputStream, Charset)
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

    @JvmStatic
    @Throws(IOException::class)
    fun writeBytes(output: OutputStream?, bytes: ByteArray?) {
        output?.write(bytes?: return)
    }

    private fun getCharset(charsetName: String?): Charset {
        return try {
            Charset.forName(charsetName)
        } catch (e: UnsupportedCharsetException) {
            DEFAULT_CHARSET
        }
    }

}