package mayonez.input

import mayonez.util.*
import org.lwjgl.glfw.GLFW
import java.awt.event.*

/**
 * Stores keyboard codes commonly used in this program under a user-friendly key name.
 *
 * @author SlavSquatSuperstar
 */
enum class Key(internal val awtCode: Int, internal val glCode: Int, private val keyName: String? = null) {

    // Special Keys
    UNKNOWN(KeyEvent.VK_UNDEFINED, GLFW.GLFW_KEY_UNKNOWN),
    ESCAPE(KeyEvent.VK_ESCAPE, GLFW.GLFW_KEY_ESCAPE),
    ENTER(KeyEvent.VK_ENTER, GLFW.GLFW_KEY_ENTER),
    SPACE(KeyEvent.VK_SPACE, GLFW.GLFW_KEY_SPACE),
    TAB(KeyEvent.VK_TAB, GLFW.GLFW_KEY_TAB),

    // Modifier Keys
    LEFT_SHIFT(KeyEvent.VK_SHIFT, GLFW.GLFW_KEY_LEFT_SHIFT),
    RIGHT_SHIFT(KeyEvent.VK_SHIFT, GLFW.GLFW_KEY_RIGHT_SHIFT),

    // Numbers
    NUM_0(KeyEvent.VK_0, GLFW.GLFW_KEY_0, keyName = "0"),
    NUM_1(KeyEvent.VK_1, GLFW.GLFW_KEY_1, keyName = "1"),
    NUM_2(KeyEvent.VK_2, GLFW.GLFW_KEY_2, keyName = "2"),
    NUM_3(KeyEvent.VK_3, GLFW.GLFW_KEY_3, keyName = "3"),
    NUM_4(KeyEvent.VK_4, GLFW.GLFW_KEY_4, keyName = "4"),
    NUM_5(KeyEvent.VK_5, GLFW.GLFW_KEY_5, keyName = "5"),
    NUM_6(KeyEvent.VK_6, GLFW.GLFW_KEY_6, keyName = "6"),
    NUM_7(KeyEvent.VK_7, GLFW.GLFW_KEY_7, keyName = "7"),
    NUM_8(KeyEvent.VK_8, GLFW.GLFW_KEY_8, keyName = "8"),
    NUM_9(KeyEvent.VK_9, GLFW.GLFW_KEY_9, keyName = "9"),

    // Letters
    A(KeyEvent.VK_A, GLFW.GLFW_KEY_A),
    B(KeyEvent.VK_B, GLFW.GLFW_KEY_B),
    C(KeyEvent.VK_C, GLFW.GLFW_KEY_C),
    D(KeyEvent.VK_D, GLFW.GLFW_KEY_D),
    E(KeyEvent.VK_E, GLFW.GLFW_KEY_E),
    F(KeyEvent.VK_F, GLFW.GLFW_KEY_F),
    G(KeyEvent.VK_G, GLFW.GLFW_KEY_G),
    H(KeyEvent.VK_H, GLFW.GLFW_KEY_H),
    I(KeyEvent.VK_I, GLFW.GLFW_KEY_I),
    J(KeyEvent.VK_J, GLFW.GLFW_KEY_J),
    K(KeyEvent.VK_K, GLFW.GLFW_KEY_K),
    L(KeyEvent.VK_L, GLFW.GLFW_KEY_L),
    M(KeyEvent.VK_M, GLFW.GLFW_KEY_M),
    N(KeyEvent.VK_N, GLFW.GLFW_KEY_N),
    O(KeyEvent.VK_O, GLFW.GLFW_KEY_O),
    P(KeyEvent.VK_P, GLFW.GLFW_KEY_P),
    Q(KeyEvent.VK_Q, GLFW.GLFW_KEY_Q),
    R(KeyEvent.VK_R, GLFW.GLFW_KEY_R),
    S(KeyEvent.VK_S, GLFW.GLFW_KEY_S),
    T(KeyEvent.VK_T, GLFW.GLFW_KEY_T),
    U(KeyEvent.VK_U, GLFW.GLFW_KEY_U),
    V(KeyEvent.VK_V, GLFW.GLFW_KEY_V),
    W(KeyEvent.VK_W, GLFW.GLFW_KEY_W),
    X(KeyEvent.VK_X, GLFW.GLFW_KEY_X),
    Y(KeyEvent.VK_Y, GLFW.GLFW_KEY_Y),
    Z(KeyEvent.VK_Z, GLFW.GLFW_KEY_Z),

    // Punctuation and Symbols
    BACKSLASH(KeyEvent.VK_BACK_SLASH, GLFW.GLFW_KEY_BACKSLASH, "\\"),
    COMMA(KeyEvent.VK_COMMA, GLFW.GLFW_KEY_COMMA, ","),
    EQUALS(KeyEvent.VK_EQUALS, GLFW.GLFW_KEY_EQUAL, "="),
    LEFT_BRACKET(KeyEvent.VK_OPEN_BRACKET, GLFW.GLFW_KEY_LEFT_BRACKET, "["),
    MINUS(KeyEvent.VK_MINUS, GLFW.GLFW_KEY_MINUS, "-"),
    PERIOD(KeyEvent.VK_PERIOD, GLFW.GLFW_KEY_PERIOD, "."),
    PLUS(KeyEvent.VK_EQUALS, GLFW.GLFW_KEY_EQUAL, "+"),
    RIGHT_BRACKET(KeyEvent.VK_CLOSE_BRACKET, GLFW.GLFW_KEY_RIGHT_BRACKET, "]"),
    SLASH(KeyEvent.VK_SLASH, GLFW.GLFW_KEY_SLASH, "/"),

    // Cursor Keys
    DOWN(KeyEvent.VK_DOWN, GLFW.GLFW_KEY_DOWN),
    LEFT(KeyEvent.VK_LEFT, GLFW.GLFW_KEY_LEFT),
    RIGHT(KeyEvent.VK_RIGHT, GLFW.GLFW_KEY_RIGHT),
    UP(KeyEvent.VK_UP, GLFW.GLFW_KEY_UP);

    /**
     * Returns the key name in title case and replaces underscores with spaces.
     */
    override fun toString(): String {
        return keyName ?: StringUtils.capitalizeAllWords(name.replace('_', ' '))
    }

    companion object {
        internal fun findWithName(keyName: String): Key? {
            return StringUtils.findConstantWithName(entries.toTypedArray(), keyName)
        }
    }

}