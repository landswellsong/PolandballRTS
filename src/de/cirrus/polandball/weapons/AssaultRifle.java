package de.cirrus.polandball.weapons;

import de.cirrus.polandball.entities.Bullet;
import de.cirrus.polandball.units.Mob;

public class AssaultRifle extends Weapon {

	public AssaultRifle(Mob owner) {
		super(owner);
		ammoLoaded = maxAmmoLoaded = 30;
		ammoCarried = maxAmmoCarried = 240;
		maxRange = 220.0;
		shootDelayTime = 0.1 / 1;
		startReloadDelayTime = 1.16;
		reloadDelayTime = 1.16;
		
		highRamp = 150;
		lowRamp = 52;
	}

	public void shoot(double xa, double ya, double za) {
		super.shoot(xa, ya, za);
		double spread = 0.1;

		for (int i = 0; i < 3; i++) {
			double xxa = xa + (random.nextDouble() - 0.5) * spread;
			double yya = ya + (random.nextDouble() - 0.5) * spread;
			double zza = za + (random.nextDouble() - 0.5) * spread * 0.5;
			owner.level.add(new Bullet(owner, this, xxa, yya, zza, 6));
		}
		shootDelay = shootDelayTime;	
	}
	
}

