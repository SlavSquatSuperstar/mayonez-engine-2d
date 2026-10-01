package mayonez;

/**
 * A controllable and reusable behavior for a {@link mayonez.GameObject}.
 * <p>
 * See {@link mayonez.GameObject} and {@link mayonez.Script} for more information.
 *
 * @author SlavSquatSuperstar
 * @deprecated Use {@link Node} instead
 */
@Deprecated
public abstract class Script extends Component {

    public Script() {
        this(UpdateOrder.SCRIPT);
    }

    public Script(int updateOrder) {
        super(null, updateOrder);
    }

}
