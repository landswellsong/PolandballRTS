package de.cirrus.polandball.units.order;

import de.cirrus.polandball.units.Unit;

/**
 * User: Cirrus
 * Date: 11.05.13
 * Time: 00:32
 * ZERE MUST BE ORDER
 */
public class Order {
	Unit unit;

	public void init (Unit unit) {
		this.unit = unit;
	}

	public void tick () {

	}

	public boolean finished () {
		return true;
	}

	//brb another beer
	// :3 love it
}
