package de.cirrus.polandball.units;

import de.cirrus.polandball.weapons.AssaultRifle;

public class Russia extends Unit {

	public Russia() {
		super(4);
		weapon = new AssaultRifle(this);
	}

}
