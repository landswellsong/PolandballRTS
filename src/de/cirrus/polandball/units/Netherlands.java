package de.cirrus.polandball.units;

import de.cirrus.polandball.Player;
import de.cirrus.polandball.weapons.RocketLauncher;

public class Netherlands extends Mob {

	public Netherlands(Player player) {
		super(2, player);
		weapon = new RocketLauncher(this);
	}

}
