package slavsquatsuperstar.demos.geometrydash;

import mayonez.*;
import mayonez.graphics.sprites.*;
import mayonez.graphics.textures.*;
import mayonez.math.*;
import mayonez.physics.*;
import mayonez.physics.colliders.*;

/**
 * A grid-aligned tile placed in the world.
 *
 * @author SlavSquatSuperstar
 */
public class Block extends GameObject {

    private final Texture cursorTexture;

    public Block(String name, Vec2 mousePos, Texture cursorTexture) {
        super(name, mousePos);
        setZIndex(ZIndex.BLOCK);
        this.cursorTexture = cursorTexture;
    }

    @Override
    protected void init() {
        addComponent(Sprites.createSprite(cursorTexture));
        addComponent(new BoxCollider(new Vec2(1f)));
        addComponent(new Rigidbody(0f).setFixedRotation(true));
    }

}
