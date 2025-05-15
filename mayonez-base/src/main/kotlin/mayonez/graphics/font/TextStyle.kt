package mayonez.graphics.font

import mayonez.graphics.*

/**
 * Describes the style of a [mayonez.graphics.font.TextLabel].
 *
 * @author SlavSquatSuperstar
 */
@JvmRecord
data class TextStyle(
    val font: Font, val color: MColor,
    val fontSize: Int, val lineSpacing: Float,
    val alignment: TextAlignment
) {

    // Copy Methods

    /**
     * Create a copy of this style with a new font.
     *
     * @param font the new font
     */
    fun setFont(font: Font): TextStyle {
        return this.copy(font = font)
    }

    /**
     * Create a copy of this style with a new color.
     *
     * @param color the new color
     */
    fun setColor(color: MColor): TextStyle {
        return this.copy(color = color)
    }

    /**
     * Create a copy of this style with a new font size.
     *
     * @param fontSize the new font size
     */
    fun setFontSize(fontSize: Int): TextStyle {
        return this.copy(fontSize = fontSize)
    }

    /**
     * Create a copy of this style with a new line spacing.
     *
     * @param lineSpacing the new line spacing
     */
    fun setLineSpacing(lineSpacing: Float): TextStyle {
        return this.copy(lineSpacing = lineSpacing)
    }

    /**
     * Create a copy of this style with a new line text alignment.
     *
     * @param alignment the new text alignment
     */
    fun setAlignment(alignment: TextAlignment): TextStyle {
        return this.copy(alignment = alignment)
    }

    companion object {

        // Constants
        @JvmField
        val DEFAULT_STYLE: TextStyle = TextStyle(
            Fonts.DEFAULT_FONT, Colors.BLACK,
            12, 1f,
            TextAlignment.LEFT
        )

    }

}