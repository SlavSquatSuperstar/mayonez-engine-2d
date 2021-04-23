package mayonez;

import util.Vector;

public class RigidBody extends Component {

	private Vector velocity;
	private double mass;

	public RigidBody(GameObject parent, double mass) {
		super(parent);
		this.mass = mass;
		this.velocity = Vector.POINT_VECTOR;
	}

	// ForceMode.Force, should be used with keyboard axes
	public void addForce(Vector force) {
		// dv = F*t/m
		velocity = velocity.add(force.scale(Game.deltaTime).scale(1 / mass));
	}

	// ForceMode.Acceleration, should be used with keyboard axes
	public void addAcceleration(Vector acceleration) {
		// dv = a*t
		velocity = velocity.add(acceleration.scale(Game.deltaTime));
	}

	// ForceMode.Impulse, should be used with button presses
	public void addImpulse(Vector impulse) {
		// dv = dp/m
		velocity = velocity.add(impulse.scale(1 / mass));
	}

	// ForceMode.VelocityChange, should be used with button presses
	public void addVelocityChange(Vector velocityChange) {
		// dv
		velocity = velocity.add(velocityChange).scale(Game.deltaTime);
	}

	public void addDisplacement(Vector displacement) {
		velocity = displacement;
	}

	// Getters and Setters

	public Vector velocity() {
		return velocity;
	}
	
	public double speed() {
		return velocity.magnitude();
	}

	public double getMass() {
		return mass;
	}

}
