package mayonez.util

/**
 * A library of common colors used to create games.
 *
 * Sources:
 * - Basic HTML: https://html-color.codes/
 * - X11: https://www.w3schools.com/colors/colors_x11.asp
 * - Extended HTML: https://htmlcolorcodes.com/color-names/
 * - Extended HTML: https://en.wikipedia.org/wiki/Web_colors#Extended_colors
 * - RAL Classic: https://www.ralcolorchart.com/ral-classic
 * - ISCC-NBS: https://en.wikipedia.org/wiki/ISCC–NBS_system
 *
 * @author SlavSquatSuperstar
 */
object Colors {

    // Shades of Red
    @JvmField
    val MAROON = Color(128, 0, 0) // HTML Maroon, #800000

    @JvmField
    val BRIGHT_RED = Color(255, 0, 0) // HTML/Java Red, #FF0000

    @JvmField
    val RED = Color(238, 0, 0) // #EE0000

    // Other Warm Colors

    @JvmField
    val ORANGE = Color(255, 165, 0) // HTML Orange, #FFA500

    @JvmField
    val BRIGHT_YELLOW = Color(255, 255, 0) // HTML/Java Yellow, #FFFF00

    @JvmField
    val YELLOW = Color(255, 238, 0) // #FFEE00

    // Shades of Green
    @JvmField
    val BRIGHT_GREEN = Color(0, 255, 0) // HTML Lime/Java Green, #00FF00

    @JvmField
    val LIGHT_GREEN = Color(50, 205, 50) // HTML Lime Green, #32CD32

    @JvmField
    val GREEN = Color(0, 128, 0) // HTML Green, #008000

    @JvmField
    val DARK_GREEN = Color(0, 100, 0) // HTML Dark Green

    // Blue-Green Colors
    @JvmField
    val BRIGHT_CYAN = Color(0, 255, 255) // HTML/Java Cyan, #00FFFF

    @JvmField
    val CYAN = Color(0, 238, 255) // #00EEFF

    @JvmField
    val TEAL = Color(0, 128, 128) // HTML Teal, #008080

    // Shades of Blue
    @JvmField
    val SKY_BLUE = Color(0, 191, 255) // HTML Deep Sky Blue, #00BFFF

    @JvmField
    val LIGHT_BLUE = Color(30, 144, 255) // HTML Dodger Blue, #1E90FF

    @JvmField
    val MEDIUM_BLUE = Color(0, 0, 255) // HTML/Java Blue, #0000FF

    @JvmField
    val BLUE = Color(0, 0, 205) // HTML Medium Blue, #0000CD

    @JvmField
    val DARK_BLUE = Color(0, 0, 128) // HTML Navy, #000080

    // Shades of Purple / Pink
    @JvmField
    val VIOLET = Color(102, 51, 153) // HTML Rebecca Purple, #663399

    @JvmField
    val PURPLE = Color(128, 0, 128) // HTML Purple, #800080

    @JvmField
    val BRIGHT_MAGENTA = Color(255, 0, 255) // HTML/Java Magenta

    @JvmField
    val MAGENTA = Color(238, 0, 238) // #EE00EE

    @JvmField
    val HOT_PINK = Color(255, 105, 180) // HTML Hot Pink, #FF69B4

    @JvmField
    val PINK = Color(255, 192, 203) // HTML Pink, #FFC0CB

    // Shades of Brown
    @JvmField
    val LIGHT_BROWN = Color(153, 102, 0) // X11 Light Brown, #996600

    @JvmField
    val BROWN = Color(128, 70, 27) // Russet, #80461B
//    val BROWN = Color(160, 82, 45) // Sienna, #A0522D
//    val BROWN = Color(149, 69, 53) // Chestnut, #954535
//    val BROWN = Color(165, 42, 42) // X11 Red Brown, #A52A2A

    @JvmField
    val DARK_BROWN = Color(92, 64, 51) // X11 Dark Brown, #5C4033

    // Grayscale
    @JvmField
    val LIGHT_GRAY = Color(192, 192, 192) // HTML Silver/Java Light Gray, #C0C0C0

    @JvmField
    val MEDIUM_GRAY = Color(169, 169, 169) // HTML Dark Gray, #A9A9A9

    @JvmField
    val GRAY = Color(128, 128, 128) // HTML/Java Gray, #808080

    @JvmField
    val DIM_GRAY = Color(105, 105, 105) // HTML Dim Gray, #696969

    @JvmField
    val DARK_GRAY = Color(64, 64, 64) // Java Dark Gray, #404040

    // White and Black

    @JvmField
    val WHITE = Color(255, 255, 255) // HTML/Java White, #FFFFFF

    @JvmField
    val JET_BLACK = Color(14, 14, 16) // RAL Jet Black, #0E0E10

    @JvmField
    val BLACK = Color(0, 0, 0) // HTML/Java Black, #000000

}