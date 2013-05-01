package de.cirrus.polandball.units;

import java.util.Random;

import de.cirrus.polandball.Art;
import de.cirrus.polandball.Bitmap;
import de.cirrus.polandball.entities.Bullet;
import de.cirrus.polandball.entities.Mob;
import de.cirrus.polandball.particles.MeatDebris;

public class Unit extends Mob {
	public static final Random random = new Random();

	public static final int UNIT_POLAND = 0;
	public static final int UNIT_GERMANY = 0;
	public static final int UNIT_RUSSIA = 0;
	public static final int UNIT_BRITAIN = 0;
	public static final int UNIT_USA = 0;
	public static final int UNIT_FRANCE = 0;

	private int ySpriteIndex;

	public int team;
	public double dir;
	public double dira;
	public double walkStep;
	public int shootDelay;
	public int shootTime = 0;
	public int speed = 100;
	public int maxHealth = 125;
	public int health = 125;

	public Unit(int ySpriteIndex, int team) {
		this.ySpriteIndex = ySpriteIndex;
		init(team);
	}

	public void init(int team) {
		this.team = team;
	}

	public void tick() {
		super.tick();

		if (shootDelay == 0) {
			shootDelay = 2 + random.nextInt(100);
		} else {
			shootDelay--;
			if (shootDelay == 0) {
				level.add(new Bullet(this, x, y, z + 5, Math.cos(dir), Math.sin(dir), 0));
				shootTime = 20;
			}
		}

		if (shootTime > 0) {
			shootTime--;
		}

		if (random.nextInt(100) == 0) {
			for (int i = 0; i < 8; i++) {
				level.add(new MeatDebris(x, y, z));
			}
		}

		boolean onGround = z <= 1;
		if (onGround) {
			xa *= 0.5;
			ya *= 0.5;
			dira *= 0.9;
			dira += ((random.nextDouble()) - (random.nextDouble()) * random.nextDouble()) * 0.01;
			dir += dira;

			double moveSpeed = 0.2 * speed / 100;
			xa += Math.cos(dir) * moveSpeed;
			ya += Math.sin(dir) * moveSpeed;

			if (random.nextInt(100) == 0) {
				za = 2;
			}
		} else {
			xa *= 0.99;
			ya *= 0.99;
		}
		za -= 0.15;

		attemptMove();

		if (xa == 0 || ya == 0) {
			dira += ((random.nextDouble()) - (random.nextDouble()) * random.nextDouble()) * 0.4;
		}

		walkStep += speed / 100;
	}

	public void render(Bitmap b) {
		int xp = (int) x;
		int yp = (int) (y - z);

		int frame = 0;

		if (shootTime == 0) {
			int dirFrame = (int) (Math.floor(-dir * 4 / (Math.PI * 2) - 2.5)) & 3;
			if (dirFrame == 0) frame = 0;
			if (dirFrame == 1) frame = 3;
			if (dirFrame == 2) frame = 6;
			if (dirFrame == 3) {
				frame = 3;
				b.xFlip = true;
			}

			int walkFrame = ((int) walkStep / 4) & 3;
			if (frame == 3) {
				if (walkFrame == 1) frame += 1;
				if (walkFrame == 2) frame += 2;
				if (walkFrame == 3) frame += 1;
			} else {
				if (walkFrame == 1) frame += 1;
				if (walkFrame == 3) frame += 2;
			}
		} else {
			int dirFrame = (int) (-Math.floor(dir * 8 / (Math.PI * 2) - 1.5)) & 7;
			frame = dirFrame + 9;
			if (dirFrame > 4) {
				frame = 9 + 3 - (dirFrame - 5);
				b.xFlip = true;
			}
		}

		Bitmap[][] sheet = Art.i.poland;
		b.draw(sheet[frame][ySpriteIndex], xp - 8, yp - 15);
		b.xFlip = false;
	}

	public static Unit create(int unitClass, int team) {
		Unit unit = null;
		if (unitClass == UNIT_POLAND) unit = new Poland(team);
		return unit;
	}
}
