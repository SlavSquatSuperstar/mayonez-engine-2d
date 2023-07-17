package mayonez.graphics.textures;

import mayonez.annotations.*;

import java.nio.IntBuffer;

/**
 * Contains basic metadata about an image file's dimensions and channels.
 *
 * @param width    the width
 * @param height   the height
 * @param channels the color channels
 * @author SlavSquatSuperstar
 */
@UsesEngine(EngineType.GL)
record ImageInfo(IntBuffer width, IntBuffer height, IntBuffer channels) {
}
