package de.cirrus.polandball.particles;

import de.cirrus.polandball.Bitmap;

/**
 * User: Cirrus
 * Date: 25.05.13
 * Time: 20:21
 */
public class GlowDebris extends Debris {
	public int lifeTime;
	public int color;

	public GlowDebris(double x, double y, double z) {
		super(x, y, z);

		lifeTime = life /= 2;
		icon = 8 * 3 + color;
		gravity = -0.00;
	}

	public void renderShadows(Bitmap b, int xp, int yp) {

	}

	public void render(Bitmap b, int xp, int yp) {
		b.draw(getBitmap(), xp - 4, yp - 4);
	}
}
