package de.cirrus.polandball.particles;

import de.cirrus.polandball.Bitmap;

public class SmokeDebris extends Debris {
	public int lifeTime;
	
	public SmokeDebris(double x, double y, double z) {
		super(x, y, z);
		
		lifeTime = life /= 2;
		
		drag = 0.92;
		icon = random.nextInt(3) + 8;
		
		gravity = -0.02;
	}
	
	
	public void render(Bitmap b, int xp, int yp) {
		int tint = life * 127 / lifeTime + 0;
		b.blendDraw(getBitmap(), xp - 4, yp - 4, 0x10101 * tint);
	}
	
	public void renderShadows(Bitmap b, int xp, int yp) {
	}
}
