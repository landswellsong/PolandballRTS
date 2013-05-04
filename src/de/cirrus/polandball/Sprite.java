package de.cirrus.polandball;

import de.cirrus.polandball.level.Level;

public class Sprite {
	public boolean removed = false;

	public static final double SCALE_X = 12.0 / 16.0;
	public static final double SCALE_Y = 6.0 / 16.0;
	
	public double x, y, z;
	public double xr = 5;
	public double yr = 5;
	public double zh = 5;

	public Level level;

	public final void init(Level level) {
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
