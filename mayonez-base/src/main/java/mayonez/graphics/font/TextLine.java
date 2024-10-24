package mayonez.graphics.font;

import java.util.*;

/**
 * A line of text rendered using a {@link TextLabel}. Stores glyph widths for
 * later use.
 *
 * @author SlavSquatSuperstar
 */
class TextLine {

    private final List<Glyph> glyphs; // Glyphs to print
    private final List<Float> glyphOffsets; // X-offsets from last glyph

    TextLine(List<Glyph> glyphs, List<Float> glyphOffsets) {
        this.glyphs = glyphs;
        this.glyphOffsets = glyphOffsets;
    }

    int numGlyphs() {
        return glyphs.size();
    }

    Glyph getGlyph(int index) {
        return glyphs.get(index);
    }

    Float getGlyphOffset(int index) {
        return glyphOffsets.get(index);
    }

    /**
     * Get the total width of all the glyphs in the line.
     *
     * @return the line width
     */
    float getLineWidth() {
        return glyphOffsets.stream().reduce(0f, Float::sum); // Sum all offsets
    }

}
