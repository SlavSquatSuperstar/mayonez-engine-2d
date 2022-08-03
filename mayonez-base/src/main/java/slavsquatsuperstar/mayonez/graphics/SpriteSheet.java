package slavsquatsuperstar.mayonez.graphics;

/**
 * Stores multiple textures and creates multiple sprites from a larger image.
 *
 * @author SlavSquatSuperstar
 */
public abstract class SpriteSheet {
    // TODO make asset?

    public abstract Sprite getSprite(int index);

//    public abstract Texture getTexture(int index);

    public abstract int numSprites();

}
