package de.cirrus.polandball.units.order;

/**
 * User: Cirrus
 * Date: 11.05.13
 * Time: 00:34
 */
public class MoveOrder extends Order {

	public double x, y;

	public MoveOrder(double x, double y) {
		this.x = x;
		this.y = y;
	}

	public void tick () {
		if (unit.turnTowards(unit.angleTo(x, y))) {
			unit.moveForwards();
		}
	}

	public boolean finished() {
		return unit.distanceTo(x , y) < 8;
	}
}
