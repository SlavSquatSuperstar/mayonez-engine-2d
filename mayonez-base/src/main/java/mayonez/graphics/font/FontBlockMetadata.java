package mayonez.graphics.font;

import mayonez.util.Record;

/**
 * Specifies a contiguous block of characters and their glyph dimensions and
 * textures.
 *
 * @param name           the name or description of the block
 * @param textureFile    the filename of the glyph texture atlas
 * @param startCharacter the start character value
 * @param endCharacter   the end character value
 * @param spriteWidth    the width of a glyph sprite, in pixels
 * @param spriteHeight   the height of a glyph sprite, in pixels
 * @param glyphAscent    the height of a character glyph above the baseline, in pixels
 * @author SlavSquatSuperstar
 */
// TODO specify characters (for non-contiguous)
// TODO specify unicode
public record FontBlockMetadata(
        String name, String textureFile,
        char startCharacter, char endCharacter,
        int spriteWidth, int spriteHeight, int glyphAscent
) {

    public FontBlockMetadata(Record record) {
        this(
                record.getString("name"), record.getString("texture_file"),
                (char) record.getInt("start_character"),
                (char) record.getInt("end_character"),
                record.getInt("sprite_width"), record.getInt("sprite_height"),
                record.getInt("glyph_ascent")
        );
    }

    /**
     * Get the number of characters this font block provides.
     *
     * @return the number of characters
     */
    public int numCharacters() {
        return endCharacter - startCharacter + 1;
    }

}
