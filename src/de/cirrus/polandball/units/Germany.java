package de.cirrus.polandball.units;

import de.cirrus.polandball.weapons.FlameThrower;

public class Germany extends Unit {

	public Germany() {
		super(1);
		weapon = new FlameThrower(this);
		
		//Germany has the shortest range, so it has to have more life 
		//still goes down quickly
		health = maxHealth = 150;
	}

}
