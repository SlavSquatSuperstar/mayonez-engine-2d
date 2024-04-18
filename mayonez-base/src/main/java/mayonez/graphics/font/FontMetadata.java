package mayonez.graphics.font;

import mayonez.util.Record;

/**
 * Contains characteristics about a font, including the name, characters, and dimensions.
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

}
