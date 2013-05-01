package de.cirrus.polandball.entities;

import de.cirrus.polandball.Bitmap;
import de.cirrus.polandball.units.Unit;

public class Bullet extends Entity {
	public Unit owner;
	public double xo, yo, zo;

	public int speed = 16;

	// TODO specialize
	public Bullet(Unit owner, double x, double y, double z, double xa, double ya, double za) {
		this.owner = owner;
		this.xo = this.x = x;
		this.yo = this.y = y;
		this.zo = this.z = z;
		this.xa = xa * speed;
		this.ya = ya * speed;
		this.za = za * speed;

	}

	public boolean blocks(Entity e) {
		if (e == owner) return false;
		return true;
	}

	public void tick() {
		xo = x;
		yo = y;
		zo = z;
		super.tick();
		attemptMove();
	}

	public void render(Bitmap b) {
		double xd = xo - x;
		double yd = yo - y;
		double zd = zo - z;

		int steps = (int) (Math.sqrt(xd * xd + yd * yd + zd * zd) + 1);
		for (int i = 0; i < steps; i++) {
			double zz = z + zd * i / steps;
			b.setPixel((int) (x + xd * i / steps), (int) (y + yd * i / steps - zz), 0xFF00FF00);
		}
	}

	public void renderShadows(Bitmap b) {
		double xd = xo - x;
		double yd = yo - y;
		double zd = zo - z;

		int steps = (int) (Math.sqrt(xd * xd + yd * yd + zd * zd) + 1);
		for (int i = 0; i < steps; i++) {
			double zz = 0;
			b.setPixel((int) (x + xd * i / steps), (int) (y + yd * i / steps - zz), 1);
		}
	}

	public void collide(Entity e, double xxa, double yya, double zza) {
		remove();
	}
}
