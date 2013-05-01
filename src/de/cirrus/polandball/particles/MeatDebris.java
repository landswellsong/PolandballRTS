package de.cirrus.polandball.particles;

import de.cirrus.polandball.entities.Entity;

public class MeatDebris extends Debris {

	public MeatDebris(double x, double y, double z) {
		super(x, y, z);
	}

	public void tick() {
		super.tick();
		Debris blood = new BloodDebris(x, y, z);
		blood.xa *= 0.1;
		blood.ya *= 0.1;
		blood.za *= 0.1;
		blood.xa += xa * 0.5;
		blood.ya += ya * 0.5;
		blood.za += za * 0.5;
		level.add(blood);
	}

	// TODO: Optimize

	public void collide(Entity e, double xxa, double yya, double zza) {
		if (za < -0.3) {
			Debris blood = new BloodDebris(x, y, z);
			blood.xa *= 0.2;
			blood.ya *= 0.2;
			blood.za *= 0.2;
			blood.xa += xa * 0.5;
			blood.ya += ya * 0.5;
			blood.za += 1.0;
			level.add(blood);
		}
		super.collide(e, xxa, yya, zza);
	}
}
