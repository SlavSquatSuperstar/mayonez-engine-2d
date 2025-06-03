package mayonez.graphics.font;

import mayonez.util.Record;

import java.util.*;

/**
 * Specifies the name and character blocks of a bitmap font.
 *
 * @param name                the name of the font
 * @param blocks              the metadata of the font blocks
 * @param glyphHeight         the cap height of a character glyph, in texels
 * @param glyphSpacing        the space between adjacent glyphs, in texels
 * @param whitespaceCharacter the character value used for whitespace
 * @param whitespaceWidth     the width of the whitespace glyph, in texels
 * @author SlavSquatSuperstar
 */
// TODO multiple space characters
public record FontMetadata(
        String name, List<FontBlock> blocks,
        int glyphHeight, int glyphSpacing,
        char whitespaceCharacter, int whitespaceWidth
) {

    // Constructors

    public FontMetadata(Record record) {
        this(
                record.getString("name"),
                getBlocks(record.getArray("blocks")),
                record.getInt("glyph_height"),
                record.getInt("glyph_spacing"),
                (char) record.getInt("whitespace_character"),
                record.getInt("whitespace_width")
        );
    }

    // Getters

    private static List<FontBlock> getBlocks(List<Object> blocks) {
        if (blocks == null) return List.of();
        return blocks.stream()
                .map(Record::from)
                .filter(Objects::nonNull)
                .map(FontBlock::new)
                .toList();
    }

}
