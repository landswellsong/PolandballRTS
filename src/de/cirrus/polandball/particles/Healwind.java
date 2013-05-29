package de.cirrus.polandball.particles;

import de.cirrus.polandball.Art;
import de.cirrus.polandball.Bitmap;
import de.cirrus.polandball.Team;
import de.cirrus.polandball.units.Mob;

import java.util.Random;

/**
 * User: Cirrus
 * Date: 25.05.13
 * Time: 20:12
 */
public class Healwind extends Particle {
	public static final Random random = new Random();

	public Mob from, to;
	public int life = 1;
	public double pos = 0, speed;
	public int br = random.nextInt(256);
	public double xo = (random.nextDouble() - 0.5) * 5;
	public double yo = (random.nextDouble() - 0.5) * 5;
	public double zo = (random.nextDouble() - 0.5) * 5;


	public Healwind (Mob from, Mob to) {
		this.from = from;
		this.to = to;
		life = random.nextInt(10) + 20;
		pos = random.nextDouble() * (1);
		speed = (random.nextDouble() + 0.5) * 0.02;
	}

	public void tick() {
		if (--life < 0 || pos >  1) {
			remove();
			return;
		}


		// Bézier!!!

		double xs = from.x + Math.cos(from.aimDir) * 4;
		double ys = from.y + Math.sin(from.aimDir) * 4;

		double xm = from.x + Math.cos(from.aimDir) * 32;
		double ym = from.y + Math.sin(from.aimDir) * 32;

		double x0 = xs + (xm - from.x) * pos;
		double y0 = ys + (ym - from.y) * pos;

		double x1 = xm + (to.x - xm) * pos;
		double y1 = ym + (to.y - ym) * pos;

		x = x0 + (x1 - x0) * pos + xo * pos;
		y = y0 + (y1 - y0) * pos + yo * pos;
		z = from.z + (to.z - from.z) * pos + 5 + zo * pos;
		pos+=speed;


	}

	public void render(Bitmap b, int xp, int yp) {
		b.blendDraw(Art.i.particles[from.team == Team.soviet ? 0 : 1][3], xp - 4, yp - 4, ((0x10101  * br) | 0xff000000) << 3);
	}

	public void renderShadows(Bitmap b, int xp, int yp) {
	}
}

