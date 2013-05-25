package de.cirrus.polandball.particles;

import de.cirrus.polandball.Bitmap;

public class FlameDebris extends Debris {

	public int maxLife;
	public boolean noSmoke;

	public FlameDebris(double x, double y, double z) {
		super(x, y, z);

		maxLife = life /= 2;

		drag = 0.92;
		icon = random.nextInt(3) + 8;
		gravity = 0;
	}

	public void tick() {
		super.tick();
		if (removed && random.nextInt(5) == 0 && !noSmoke) {
			Debris smoke = new SmokeDebris(x, y, z);
			smoke.xa *= 0.1;
			smoke.ya *= 0.1;
			smoke.za *= 0.1;

			smoke.xa += xa * 2;
			smoke.ya += ya * 2;
			smoke.za += za * 2;
			level.add(smoke);
		}
	}

	public void render(Bitmap b, int xp, int yp) {
		icon = 8 * 2 + 3 - 4 * life / maxLife;
		super.render(b, xp, yp);
	}

	public void renderShadows(Bitmap b, int xp, int yp) {
	}

	
}
