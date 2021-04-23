package mayonez;

import util.Vector2;

public class RigidBody extends Component {

	private Vector2 velocity;
	private double mass;

	public RigidBody(double mass) {
		this.mass = mass;
		this.velocity = Vector2.POINT_VECTOR;
	}

	// ForceMode.Force, should be used with keyboard axes
	public void addForce(Vector2 force) {
		// dv = F*t/m
		velocity = velocity.add(force.scale(Game.deltaTime).scale(1 / mass));
	}

	// ForceMode.Acceleration, should be used with keyboard axes
	public void addAcceleration(Vector2 acceleration) {
		// dv = a*t
		velocity = velocity.add(acceleration.scale(Game.deltaTime));
	}

	// ForceMode.Impulse, should be used with button presses
	public void addImpulse(Vector2 impulse) {
		// dv = dp/m
		velocity = velocity.add(impulse.scale(1 / mass));
	}

	// ForceMode.VelocityChange, should be used with button presses
	public void addVelocityChange(Vector2 velocityChange) {
		// dv
		velocity = velocity.add(velocityChange).scale(Game.deltaTime);
	}

	public void addDisplacement(Vector2 displacement) {
		velocity = displacement;
	}

	// Getters and Setters

	public Vector2 velocity() {
		return velocity;
	}
	
	public double speed() {
		return velocity.magnitude();
	}

	public double getMass() {
		return mass;
	}

}
