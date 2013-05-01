package de.cirrus.polandball;

import de.cirrus.polandball.level.Level;

public class Sprite {
	public boolean removed;
	
	public double x, y, z; 
	public double xr = 2;
	public double yr = 2;
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
