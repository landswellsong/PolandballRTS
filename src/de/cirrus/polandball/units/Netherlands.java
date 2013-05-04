package de.cirrus.polandball.units;

import de.cirrus.polandball.weapons.RocketLauncher;

public class Netherlands extends Unit {

	public Netherlands() {
		super(2);
		weapon = new RocketLauncher(this);
	}

}
