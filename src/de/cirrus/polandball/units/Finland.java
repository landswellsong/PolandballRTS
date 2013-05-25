package de.cirrus.polandball.units;

import de.cirrus.polandball.Player;
import de.cirrus.polandball.weapons.SniperRifle;

public class Finland extends Mob {

	public Finland(Player player) {
		super(6, player);
		weapon = new SniperRifle(this);
	}

}
