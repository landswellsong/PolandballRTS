package de.cirrus.polandball.particles;

import de.cirrus.polandball.Bitmap;

public class BloodDebris extends Debris {

	public BloodDebris(double x, double y, double z) {
		super(x, y, z);

		drag = 0.96;
		bounce = 0.1;
		icon = 1;
	}

	public void renderShadows(Bitmap b, int xp, int yp) {
		b.fill(xp, yp, xp, yp, 1);
	}
}
