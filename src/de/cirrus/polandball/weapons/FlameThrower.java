package de.cirrus.polandball.weapons;

import de.cirrus.polandball.entities.FlameBullet;
import de.cirrus.polandball.units.Mob;

public class FlameThrower extends Weapon {

	public FlameThrower(Mob owner) {
		super(owner);
		ammoLoaded = maxAmmoLoaded = 200;
		ammoCarried = maxAmmoCarried = 0;
		shootDelayTime = 0.04;
		startReloadDelayTime = 0.0;
		reloadDelayTime = 0.0;
		maxRange = 120;
		aimLead = 2;
		
		highRamp = 100;
		lowRamp = 60;
		
		midDistance = 200;
		farDistance = 400;
	}

	
	public void shoot(double xa, double ya, double za) {
		super.shoot(xa, ya, za);
		owner.level.add(new FlameBullet(owner, this, xa, ya, za, 9));
		shootDelay = shootDelayTime;
	}

}
