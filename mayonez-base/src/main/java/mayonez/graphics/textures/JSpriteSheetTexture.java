package mayonez.graphics.textures;

import mayonez.graphics.*;
import org.jetbrains.annotations.NotNull;

import java.awt.image.*;

/**
 * A GL texture created from a sprite sheet that represents a portion of another
 * texture.
 *
 * @author SlavSquatSuperstar
 */
@UsesEngine(EngineType.AWT)
public final class JSpriteSheetTexture extends JTexture {

    private final JTexture parentTexture;
    private final int spriteSheetIndex;

    public JSpriteSheetTexture(JTexture parentTexture, int spriteSheetIndex, BufferedImage image) {
        super(parentTexture.getFilename(), image);
        this.parentTexture = parentTexture;
        this.spriteSheetIndex = spriteSheetIndex;
    }

    // Asset Methods

    @Override
    public void free() { // Don't do anything since we may need the parent texture
    }

    // Image Getters

    public JTexture getParentTexture() {
        return parentTexture;
    }

    public int getParentWidth() {
        return parentTexture.getWidth();
    }

    public int getParentHeight() {
        return parentTexture.getHeight();
    }

    @NotNull
    @Override
    public String toString() {
        return "%s (Sprite %d)".formatted(super.toString(), spriteSheetIndex);
    }

}
