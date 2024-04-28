package mayonez.graphics.font;

import mayonez.util.Record;

/**
 * Contains characteristics about a font, including the name, characters, and dimensions.
 *
 * @param name the name of the font
 * @param startCharacter the start character code point
 * @param endCharacter the end character code point
 * @param glyphHeight the total height of a character glyph in pixels
 * @param glyphAscent the height of a character glyph above the baseline in pixels
 * @param glyphSpacing the space between adjacent glyphs in pixels
 * @param spaceWidth the width of the space character in pixels
 *
 * @author SlavSquatSuperstar
 */
public record FontMetadata(
        String name,
        int startCharacter, int endCharacter,
        int glyphHeight, int glyphAscent, int glyphSpacing,
        int spaceWidth
) {

    // Constructors

    public FontMetadata(Record record) {
        this(
                record.getString("name"),
                record.getInt("start_character"), record.getInt("end_character"),
                record.getInt("glyph_height"), record.getInt("glyph_ascent"),
                record.getInt("glyph_spacing"),
                record.getInt("space_width")
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

//    public char startCharacterValue() {
//        return (char) startCharacter;
//    }
//
//    public char endCharacterValue() {
//        return (char) endCharacter;
//    }

}
