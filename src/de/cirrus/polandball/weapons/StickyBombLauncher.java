package de.cirrus.polandball.weapons;

import de.cirrus.polandball.entities.StickyBomb;
import de.cirrus.polandball.units.Mob;

import java.util.ArrayList;
import java.util.List;

public class StickyBombLauncher extends Weapon {

	private List<StickyBomb> bombs = new ArrayList<StickyBomb>();
	
	public StickyBombLauncher(Mob owner) {
		super(owner);
		
		ammoLoaded = maxAmmoLoaded = 8;
		ammoCarried = maxAmmoCarried = 24;
		
		shootDelayTime = 0.6;
		startReloadDelayTime = 1.09;
		reloadDelayTime = 0.67;
		aimLead = 0;
		
		highRamp = 125;
		lowRamp = 100;
	}
	
	public void shoot(double xa, double ya, double za) {
		super.shoot(xa, ya, za);
		for (int i = 0; i < bombs.size(); i++) {
			if (bombs.get(i).removed) bombs.remove(i--);
		}
		
		if (bombs.size() == 8) {
			bombs.remove(0).detonate();
		}
		StickyBomb bomb = new StickyBomb(owner, this, xa, ya, za, 90);
		bombs.add(bomb);
		owner.level.add(bomb);
		shootDelay = shootDelayTime;
	}
	
	
	public void playerDied() {
		for (int i = 0; i < bombs.size(); i++) {
			if (!bombs.get(i).removed) bombs.remove(i--).fizzle();
		}
	}
}
