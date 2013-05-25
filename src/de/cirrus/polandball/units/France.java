package de.cirrus.polandball.units;

import de.cirrus.polandball.Player;
import de.cirrus.polandball.weapons.Shotgun;

public class France extends Mob {

	public France(Player player) {
		super(3, player);
		weapon = new Shotgun(this);
	}

}
