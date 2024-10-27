package mayonez.input

import mayonez.util.*
import org.lwjgl.glfw.GLFW.*
import java.awt.event.KeyEvent.*

/**
 * Stores keyboard codes commonly used in this program under a
 * user-friendly key name.
 *
 * @author SlavSquatSuperstar
 */
enum class Key(val awtCode: Int, val glCode: Int, private val keyName: String? = null) {

    UNKNOWN(VK_UNDEFINED, GLFW_KEY_UNKNOWN),

    // Special Keys
    BACKSPACE(VK_BACK_SPACE, GLFW_KEY_BACKSPACE),
    DELETE(VK_DELETE, GLFW_KEY_DELETE),
    ESCAPE(VK_ESCAPE, GLFW_KEY_ESCAPE),
    ENTER(VK_ENTER, GLFW_KEY_ENTER),
    SPACE(VK_SPACE, GLFW_KEY_SPACE),
    TAB(VK_TAB, GLFW_KEY_TAB),

    // Modifier Keys
    LEFT_SHIFT(VK_SHIFT, GLFW_KEY_LEFT_SHIFT),
    RIGHT_SHIFT(VK_SHIFT, GLFW_KEY_RIGHT_SHIFT),

    // Numbers
    KEY_0(VK_0, GLFW_KEY_0, keyName = "0"),
    KEY_1(VK_1, GLFW_KEY_1, keyName = "1"),
    KEY_2(VK_2, GLFW_KEY_2, keyName = "2"),
    KEY_3(VK_3, GLFW_KEY_3, keyName = "3"),
    KEY_4(VK_4, GLFW_KEY_4, keyName = "4"),
    KEY_5(VK_5, GLFW_KEY_5, keyName = "5"),
    KEY_6(VK_6, GLFW_KEY_6, keyName = "6"),
    KEY_7(VK_7, GLFW_KEY_7, keyName = "7"),
    KEY_8(VK_8, GLFW_KEY_8, keyName = "8"),
    KEY_9(VK_9, GLFW_KEY_9, keyName = "9"),

    // Letters
    A(VK_A, GLFW_KEY_A),
    B(VK_B, GLFW_KEY_B),
    C(VK_C, GLFW_KEY_C),
    D(VK_D, GLFW_KEY_D),
    E(VK_E, GLFW_KEY_E),
    F(VK_F, GLFW_KEY_F),
    G(VK_G, GLFW_KEY_G),
    H(VK_H, GLFW_KEY_H),
    I(VK_I, GLFW_KEY_I),
    J(VK_J, GLFW_KEY_J),
    K(VK_K, GLFW_KEY_K),
    L(VK_L, GLFW_KEY_L),
    M(VK_M, GLFW_KEY_M),
    N(VK_N, GLFW_KEY_N),
    O(VK_O, GLFW_KEY_O),
    P(VK_P, GLFW_KEY_P),
    Q(VK_Q, GLFW_KEY_Q),
    R(VK_R, GLFW_KEY_R),
    S(VK_S, GLFW_KEY_S),
    T(VK_T, GLFW_KEY_T),
    U(VK_U, GLFW_KEY_U),
    V(VK_V, GLFW_KEY_V),
    W(VK_W, GLFW_KEY_W),
    X(VK_X, GLFW_KEY_X),
    Y(VK_Y, GLFW_KEY_Y),
    Z(VK_Z, GLFW_KEY_Z),

    // Punctuation and Symbols
    BACKSLASH(VK_BACK_SLASH, GLFW_KEY_BACKSLASH, "\\"),
    COMMA(VK_COMMA, GLFW_KEY_COMMA, ","),
    EQUALS(VK_EQUALS, GLFW_KEY_EQUAL, "="),
    LEFT_BRACKET(VK_OPEN_BRACKET, GLFW_KEY_LEFT_BRACKET, "["),
    MINUS(VK_MINUS, GLFW_KEY_MINUS, "-"),
    PERIOD(VK_PERIOD, GLFW_KEY_PERIOD, "."),
    PLUS(VK_EQUALS, GLFW_KEY_EQUAL, "+"),
    RIGHT_BRACKET(VK_CLOSE_BRACKET, GLFW_KEY_RIGHT_BRACKET, "]"),
    SEMICOLON(VK_SEMICOLON, GLFW_KEY_SEMICOLON, ";"),
    SLASH(VK_SLASH, GLFW_KEY_SLASH, "/"),

    // Cursor Keys
    DOWN(VK_DOWN, GLFW_KEY_DOWN),
    LEFT(VK_LEFT, GLFW_KEY_LEFT),
    RIGHT(VK_RIGHT, GLFW_KEY_RIGHT),
    UP(VK_UP, GLFW_KEY_UP);

    /** Returns the key name in title case and replaces underscores with spaces. */
    override fun toString(): String {
        return keyName ?: StringUtils.capitalizeAllWords(name.replace('_', ' '))
    }

    companion object {
        @JvmStatic
        fun findWithName(keyName: String): Key? {
            return StringUtils.findWithName(entries, keyName)
        }
    }

}