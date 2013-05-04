package de.cirrus.polandball.units;

import de.cirrus.polandball.weapons.SniperRifle;

public class Finland extends Unit {

	public Finland() {
		super(6);
		weapon = new SniperRifle(this);
	}

}
