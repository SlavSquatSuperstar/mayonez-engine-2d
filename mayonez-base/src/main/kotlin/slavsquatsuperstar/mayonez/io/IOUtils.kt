package slavsquatsuperstar.mayonez.io

import slavsquatsuperstar.mayonez.Preferences.fileCharset
import slavsquatsuperstar.util.StringUtils
import java.io.*
import java.nio.charset.Charset
import java.nio.charset.StandardCharsets
import java.nio.charset.UnsupportedCharsetException

object IOUtils {

    private val DEFAULT_CHARSET = StandardCharsets.UTF_8
    private const val LINE_ENDING = "\n"

    // todo bytes
    @JvmStatic
    @Throws(IOException::class, FileNotFoundException::class)
    fun readText(input: InputStream?): String = StringUtils.fromLines(readLines(input))

    // From Apache Commons IO > IOUtils > readLines(InputStream, Charset)
    @JvmStatic
    @Throws(IOException::class, FileNotFoundException::class)
    fun readLines(input: InputStream?): Array<String> {
        if (input == null) throw FileNotFoundException("Input stream is null")
        val reader = BufferedReader(InputStreamReader(input, getCharset(fileCharset)))
        return reader.readLines().toTypedArray()
    }

    // From Apache Commons IO > IOUtils > write(String, OutputStream, Charset)
    @JvmStatic
    @Throws(IOException::class)
    fun write(output: OutputStream?, text: String?) {
        if (output == null) throw FileNotFoundException("Output stream is null")
        if (text != null) output.write(text.toByteArray(getCharset(fileCharset)))
    }

    // From Apache Commons IO > IOUtils > write(Collection<?>, String, OutputStream, Charset)
    @JvmStatic
    @Throws(IOException::class)
    fun writeLines(output: OutputStream?, lines: Array<String?>?) {
        if (output == null) throw FileNotFoundException("Output stream is null")
        if (lines == null) return
        val cs = getCharset(fileCharset)
        for (line in lines) {
            if (line != null) output.write(line.toString().toByteArray(cs))
            output.write(LINE_ENDING.toByteArray(cs))
        }
    }

    private fun getCharset(charsetName: String?): Charset {
        return try {
            Charset.forName(charsetName)
        } catch (e: UnsupportedCharsetException) {
            DEFAULT_CHARSET
        }
    }

}