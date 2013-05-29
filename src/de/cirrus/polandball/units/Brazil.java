package de.cirrus.polandball.units;

import de.cirrus.polandball.Player;
import de.cirrus.polandball.entities.Entity;
import de.cirrus.polandball.particles.Healwind;
import de.cirrus.polandball.weapons.HealingWand;

/**
 * User: Cirrus
 * Date: 25.05.13
 * Time: 19:35
 */
public class Brazil extends Mob {

	public Brazil (Player player) {
		super(8, player);

		maxHealth = health = 150;
		weapon = new HealingWand(this); //I feel like brazil could heal with his forest magics
	}

	public boolean isLegalTarget(Mob u) {
		return u.team == this.player.team && u.health < u.maxHealth* 100 / 100;
	}

	public void shootAt(Entity target) {
		if (target instanceof Mob) {
			Mob u = (Mob) target;
			if (isLegalTarget(u)) {
				u.health++;
				level.add(new Healwind(this, u));
				aimDir = dir;
				shootTime = 10;
			}
		}
	}

}
