package de.cirrus.polandball.weapons;

import de.cirrus.polandball.entities.Rocket;
import de.cirrus.polandball.units.Mob;
import de.cirrus.polandball.units.Unit;

public class RocketLauncher extends Weapon {

	public RocketLauncher(Mob owner) {
		super(owner);
		ammoLoaded = maxAmmoLoaded = 4;
		ammoCarried = maxAmmoCarried = 20;
		shootDelayTime = 0.8;
		startReloadDelayTime = 0.8;
		reloadDelayTime = 0.92;
		aimOnGround = true;
		aimLead = 2;
		highRamp = 125;
		lowRamp = 53;
	}

	public void shoot(double xa, double ya, double za) {
		super.shoot(xa, ya, za);
		owner.level.add(new Rocket(owner, this, xa, ya, za, 90));
		shootDelay = shootDelayTime;
	}

}
