package de.cirrus.polandball.units;

import de.cirrus.polandball.weapons.FlameThrower;

public class Germany extends Unit {

	public Germany() {
		super(1);
		weapon = new FlameThrower(this);

		health = maxHealth = 150;
	}

}
