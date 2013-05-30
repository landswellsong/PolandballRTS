package de.cirrus.polandball.units;

import de.cirrus.polandball.Player;
import de.cirrus.polandball.weapons.Revolver;

/**
 * POLAND STRONG!!!!!!
 * 
 * */

public class Poland extends Mob {

	
	public Poland(Player player) {
		super(0, player);
		maxHealth = health = 125;
		
		weapon = new Revolver(this);
	}

}
