package mayonez.graphics;

/**
 * A library of common colors used to create games.
 * <p>
 * Sources:
 * <ul>
 * <li><a href="https://html-color.codes/">Basic HTML</a></li>
 * <li><a href="https://www.w3schools.com/colors/colors_x11.asp">X11</a></li>
 * <li><a href="https://htmlcolorcodes.com/color-names/">Extended HTML (HTMLColorCodes)</a></li>
 * <li><a href="https://en.wikipedia.org/wiki/Web_colors#Extended_colors">Extended HTML (Wikipedia)</a></li>
 * <li><a href="https://www.ralcolorchart.com/ral-classic">RAL Classic</a></li>
 * <li><a href="https://en.wikipedia.org/wiki/ISCC">ISCC-NBS</a></li>
 * </ul>
 *
 * @author SlavSquatSuperstar
 */
public final class Colors {

    private Colors() {}

    // Warm Colors
    public static final Color DARK_RED = new Color(128, 0, 0); // HTML Maroon, #800000

    public static final Color RED = new Color(238, 0, 0); // #EE0000

    public static final Color ORANGE = new Color(255, 165, 0); // HTML Orange, #FFA500

    public static final Color YELLOW = new Color(255, 238, 0); // #FFEE00

    // Shades of Green

    public static final Color LIGHT_GREEN = new Color(50, 205, 50); // HTML Lime Green, #32CD32

    public static final Color GREEN = new Color(0, 128, 0); // HTML Green, #008000

    public static final Color DARK_GREEN = new Color(0, 100, 0); // HTML Dark Green, #006400

    // Shades of Cyan

    public static final Color CYAN = new Color(0, 238, 255); // #00EEFF

    public static final Color TEAL = new Color(0, 128, 128); // HTML Teal, #008080

    // Shades of Blue
//    public static final Color SKY_BLUE = new Color(135, 206, 235); // HTML Sky Blue, #87CEEB
    public static final Color SKY_BLUE = new Color(0, 191, 255); // HTML Deep Sky Blue, #00BFFF

    public static final Color LIGHT_BLUE = new Color(30, 144, 255); // HTML Dodger Blue, #1E90FF

    public static final Color BLUE = new Color(0, 0, 205); // HTML Medium Blue, #0000CD

    public static final Color DARK_BLUE = new Color(0, 0, 128); // HTML Navy, #000080

    // Purple / Pink
    public static final Color PURPLE = new Color(102, 51, 153); // HTML Rebecca Purple, #663399

    public static final Color PINK = new Color(255, 105, 180); // HTML Hot Pink, #FF69B4

    // Brown
    public static final Color BROWN = new Color(139, 69, 19); // HTML Saddle Brown, #8b4513

    // Grayscale
    public static final Color WHITE = new Color(255, 255, 255); // HTML/Java White, #FFFFFF

    public static final Color LIGHT_GRAY = new Color(192, 192, 192); // HTML Silver/Java Light Gray, #C0C0C0

    public static final Color GRAY = new Color(128, 128, 128); // HTML/Java Gray, #808080

    public static final Color DARK_GRAY = new Color(64, 64, 64); // Java Dark Gray, #404040

    public static final Color BLACK = new Color(0, 0, 0); // HTML/Java Black, #000000

}
