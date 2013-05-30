package de.cirrus.polandball.units;

import de.cirrus.polandball.Player;
import de.cirrus.polandball.entities.Entity;
import de.cirrus.polandball.particles.HealWind;
import de.cirrus.polandball.weapons.HealingWand;


public class Brazil extends Mob {
	
	public Brazil (Player player) {
		super(8, player);
		
		maxHealth = health = 150;
		weapon = new HealingWand(this);
	}

	public boolean isLegalTarget(Mob u) {

		boolean toHeal = u.team ==this.player.team && u.health < u.maxHealth * 100 / 100;
		return toHeal;
	}

	public void shootAt(Entity target) {
		if (target instanceof Mob) {
			Mob u = (Mob) target;
			if (isLegalTarget(u)) {
				u.health++;
				level.add(new HealWind(this, u));
				
				aimDir = dir;
				shootTime = 10;
			}
		}
	}
}