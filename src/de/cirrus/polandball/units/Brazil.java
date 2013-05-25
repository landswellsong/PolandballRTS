package de.cirrus.polandball.units;

import de.cirrus.polandball.Player;
import de.cirrus.polandball.weapons.HealingWand;

/**
 * User: Cirrus
 * Date: 25.05.13
 * Time: 19:35
 */
public class Brazil extends Mob {

	public Brazil (Player player) {
		super(8, player);

		weapon = new HealingWand(this); //I feel like brazil could heal with his forest magics
	}

}
