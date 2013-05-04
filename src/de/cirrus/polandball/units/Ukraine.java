package de.cirrus.polandball.units;

import de.cirrus.polandball.weapons.StickyBombLauncher;

public class Ukraine extends Unit {

	public Ukraine() {
		super(5);
		weapon = new StickyBombLauncher(this);
	}

}
