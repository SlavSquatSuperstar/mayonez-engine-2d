package lib.math;

public class Vector {

	double[] components;

	public Vector(double x, double y) {
		components = new double[] { x, y };
	}

	public double getX() {
		return components[0];
	}

	public void setX(double x) {
		components[0] = x;
	}

	public double getY() {
		return components[1];
	}

	public void setY(double y) {
		components[1] = y;
	}

	public double magnitude() {
		return Math.sqrt(getX() * getX() + getY() * getY());
	}

//	public void change(double dx, double dy) {
//		setX(getX() + dx);
//		setY(getY() + dy);
//	}

	public Vector add(Vector v) {
		return new Vector(getX() + v.getX(), getY() + v.getY());
	}

	public Vector scale(double scalar) {
		return new Vector(getX() * scalar, getY() * scalar);
	}

	@Override
	public String toString() {
		return String.format("<%f, %f>", getX(), getY());
	}

	public static void main(String[] args) {

		Vector pos = new Vector(0, 0);
		Vector vel = new Vector(1, 2);
		System.out.println(pos.add(vel).scale(2));
		System.out.println(pos.scale(2).add(vel));
		
	}

}
