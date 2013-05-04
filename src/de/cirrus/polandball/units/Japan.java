package de.cirrus.polandball.units;

import de.cirrus.polandball.weapons.Minigun;

public class Japan extends Unit {

	public Japan() {
		super(7);
		weapon = new Minigun(this);
	}

}
