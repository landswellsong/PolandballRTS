package de.cirrus.polandball.units.order;

import de.cirrus.polandball.units.Mob;

/**
 * User: Cirrus
 * Date: 11.05.13
 * Time: 00:32
 * ZERE MUST BE ORDER
 */
public class Order {
	public Mob unit;

	public void init (Mob unit) {
		this.unit = unit;
	}

	public void tick () {

	}

	public boolean finished () {
		return true;
	}

}
