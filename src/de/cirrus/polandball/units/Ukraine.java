package de.cirrus.polandball.units;

import de.cirrus.polandball.Player;
import de.cirrus.polandball.weapons.StickyBombLauncher;

public class Ukraine extends Mob {

	public Ukraine(Player player) {
		super(5, player);
		maxHealth = health = 175;
		weapon = new StickyBombLauncher(this);
	}

}
