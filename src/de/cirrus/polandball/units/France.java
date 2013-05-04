package de.cirrus.polandball.units;

import de.cirrus.polandball.weapons.Shotgun;

public class France extends Unit {

	public France() {
		super(3);
		weapon = new Shotgun(this);
	}

}
