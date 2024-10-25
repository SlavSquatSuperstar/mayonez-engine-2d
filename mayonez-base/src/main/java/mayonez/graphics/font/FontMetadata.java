package mayonez.graphics.font;

import mayonez.util.Record;

/**
 * Contains characteristics about a font, including the name, characters, and dimensions.
 *
 * @param name                the name of the font
 * @param startCharacter      the start character value
 * @param endCharacter        the end character value
 * @param glyphHeight         the total height of a character glyph in pixels
 * @param glyphAscent         the height of a character glyph above the baseline in pixels
 * @param glyphSpacing        the space between adjacent glyphs in pixels
 * @param whitespaceCharacter the character value used for whitespace
 * @param whitespaceWidth     the width of the whitespace glyph
 * @author SlavSquatSuperstar
 */
// TODO specify characters (for non-contiguous)
// TODO specify space characters
public record FontMetadata(
        String name,
        char startCharacter, char endCharacter,
        int glyphHeight, int glyphAscent, int glyphSpacing,
        char whitespaceCharacter, int whitespaceWidth
) {

    // Constructors

    public FontMetadata(Record record) {
        this(
                record.getString("name"),
                (char) record.getInt("start_character"), (char) record.getInt("end_character"),
                record.getInt("glyph_height"), record.getInt("glyph_ascent"), record.getInt("glyph_spacing"),
                (char) record.getInt("whitespace_character"), record.getInt("whitespace_width")
        );
    }

    // Getters

    /**
     * Get the number of characters this font provides.
     *
     * @return the number of characters
     */
    public int numCharacters() {
        return endCharacter - startCharacter + 1;
    }

}
