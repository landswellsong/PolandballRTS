package de.cirrus.polandball.units.order;

import de.cirrus.polandball.units.Mob;


public class Order {
	Mob unit;

	public void init (Mob unit) {
		this.unit = unit;
	}

	public void tick () {

	}

	public boolean finished () {
		return true;
	}

}
