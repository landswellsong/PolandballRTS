package de.cirrus.polandball.weapons;

import de.cirrus.polandball.units.Mob;

/**
 * User: Cirrus
 * Date: 25.05.13
 * Time: 19:43
 */
public class HealingWand extends Weapon {

	public HealingWand(Mob owner) {
		super(owner);
		ammoLoaded = maxAmmoLoaded = 0;
		ammoCarried = maxAmmoCarried = 0;
		shootDelayTime = 1.0 / 25.0f;
		startReloadDelayTime = 0;
		reloadDelayTime = 0.0;
		maxRange = 100;
		aimLead = 2;

		highRamp = 100;
		lowRamp = 100;
	}

	public void shoot(double xa, double ya, double za) {
	}
}
