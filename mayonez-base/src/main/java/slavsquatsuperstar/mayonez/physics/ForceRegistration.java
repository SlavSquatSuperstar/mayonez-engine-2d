package slavsquatsuperstar.mayonez.physics;

/**
 * An interaction between a physical object and a force.
 *
 * @author SlavSquatSuperstar
 */
public class ForceRegistration {

    ForceGenerator fg;
    Rigidbody rb;

    public ForceRegistration(ForceGenerator force, Rigidbody rb) {
        this.fg = force;
        this.rb = rb;
    }

    @Override
    public boolean equals(Object other) {
        if (other == null) return false;
        if (other.getClass() != ForceRegistration.class) return false;

        ForceRegistration fr = (ForceRegistration)other;
        return fr.rb == this.rb && fr.fg == this.fg;
    }
}
