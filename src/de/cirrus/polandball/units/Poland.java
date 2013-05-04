package de.cirrus.polandball.units;

import de.cirrus.polandball.weapons.Revolver;

/**
 * POLAND STRONG!!!!!!
 * 
 * */

public class Poland extends Unit {

	
	public Poland() {
		super(0);
		weapon = new Revolver(this);
	}

}
