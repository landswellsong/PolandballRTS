package de.cirrus.polandball;

import de.cirrus.polandball.level.Level;

/**
 * A pseudo abstract superclass for (almost) everything that is represented by a sprite, which means, a Bitmap object displayed
 * in the game.
 * It defines the basic methods every subclass should override and the most important and universal variables like the 
 * actual position in the game (x, y, z). Includes a reference to the Level class so it's accessable by every subclass.
 * 
 * 
 * */

public class Sprite {
	public boolean removed = false;

	
	public double x, y, z;
	public double xr = 2;
	public double yr = 2;
	public double zh = 5;

	public Level level;

	public void init(Level level) {
		init();
		this.level = level;
	}

	public void init() {
	}

	public void tick() {

	}

	public void remove() {
		removed = true;
	}



	public void render(Bitmap b) {
		
	}
	
	public void renderShadows(Bitmap b) {

	}
}
