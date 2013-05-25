package de.cirrus.polandball;

import de.cirrus.polandball.level.Level;


public class Sprite {
	public boolean removed = false;
	public static final double SCALE_X = 1;
	public static final double SCALE_Y = 1;
	
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



	public final void render(Bitmap b) {
		int xp = (int)(Math.floor((x)*SCALE_X));
		int yp = (int)(Math.floor((y)*SCALE_Y - z));
		render(b, xp, yp);
	}
	
	public final void renderShadows(Bitmap b) {
		int xp = (int)(x);
		int yp = (int)(y);
		renderShadows(b, xp, yp);
	}


	public void render(Bitmap b, int xp, int yp) {

	}

	public void renderShadows(Bitmap b, int xp, int yp) {

	}
}
