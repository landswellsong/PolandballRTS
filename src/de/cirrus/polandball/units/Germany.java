package de.cirrus.polandball.units;

import de.cirrus.polandball.Player;
import de.cirrus.polandball.weapons.FlameThrower;

public class Germany extends Mob {

	public Germany(Player player) {
		super(1, player);
		weapon = new FlameThrower(this);

		health = maxHealth = 150;
	}

}
