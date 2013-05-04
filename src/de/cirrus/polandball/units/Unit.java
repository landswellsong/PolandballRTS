package de.cirrus.polandball.units;

import java.util.List;
import java.util.Random;

import de.cirrus.polandball.Art;
import de.cirrus.polandball.Bitmap;
import de.cirrus.polandball.entities.Bullet;
import de.cirrus.polandball.entities.Entity;
import de.cirrus.polandball.entities.Mob;
import de.cirrus.polandball.particles.Debris;
import de.cirrus.polandball.particles.FlameDebris;
import de.cirrus.polandball.particles.MeatDebris;
import de.cirrus.polandball.particles.SplatDebris;
import de.cirrus.polandball.weapons.StickyBombLauncher;
import de.cirrus.polandball.weapons.Weapon;

public class Unit extends Mob {
	public static final Random random = new Random();

	public static final int UNIT_POLAND = 0;
	public static final int UNIT_GERMANY = 1;
	public static final int UNIT_NETHERLANDS = 2;
	public static final int UNIT_FRANCE = 3;
	public static final int UNIT_RUSSIA = 4;
	public static final int UNIT_UKRAINE = 5;
	public static final int UNIT_FINLAND = 6;
	public static final int UNIT_JAPAN = 7;

	private int ySpriteIndex;

	public int team;
	public double dir;
	public double dira;
	public double walkStep;

	public int shootTime;
	public int speed = 100;
	public int maxHealth = 1250;
	public int health = 125;
	public int hurtTime = 0;
	public int aimDir = 0;

	public int burnTime, burnInterval;

	//public Weapon weapon = new RocketLauncher(this);
	//public Weapon weapon = new Shotgun(this);
	//public Weapon weapon = new SniperRifle(this);
	//public Weapon weapon = new Minigun(this);
	public Weapon weapon = new StickyBombLauncher(this);
	
	public Unit(int ySpriteIndex) {
		this.ySpriteIndex = ySpriteIndex;
		init();
	}

	public void init() {
		this.team = 0;
	}

	public void hurt(int damage) {
		this.health -= damage;
		hurtTime = 20;
	}

	public void hitBy(Bullet bullet) {
		if (bullet.owner.team == team) return; // friendly fire off
		
		bullet.applyHitEffect(this);
		
		hurt(bullet.getDamage(this));
		knockBack(bullet.xa * 0.25, bullet.ya * 0.25, bullet.za * 0.25);
		Debris sd = new SplatDebris(x, y, z + 5);
		sd.xa -= bullet.xa * 0.1;
		sd.ya -= bullet.ya * 0.1;
		sd.za -= bullet.za * 0.1;
		level.add(sd);
	}

	private void knockBack(double xxa, double yya, double zza) {
		xa += (xxa - xa) * 0.4;
		ya += (yya - ya) * 0.4;
		za += (zza - za) * 0.4;
	}

	public void tick() {
		super.tick();
		
		if (burnTime > 0) {
			if(++burnInterval >= 30) {
				burnInterval = 0;
				hurt(3); //dot
			}
			burnTime--;
		}
		
		if (hurtTime >= 0) hurtTime--;
		
		weapon.tick();

		if (weapon.canUse()) {
			updateWeapon();
		}

		if (shootTime > 0) {
			shootTime--;
		}

		if (health <= 0) {
			die();
			return;
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
		
		if (burnTime > 0) {
			FlameDebris fd = new FlameDebris(x +(random.nextDouble() - 0.5)*2, y + (random.nextDouble() - 0.5)*4, z + random.nextInt(12));
			fd.xa *= 0.1;
			fd.ya *= 0.1;
			fd.za *= 0.1;
			fd.life /= 2;
			level.add(fd);
		}
	}

	public void updateWeapon() {
		Entity target = findTarget();
		if (weapon.ammoLoaded > 0 && target != null) {
			shootAt(target);
			shootTime = 20;
		} else {
			weapon.reload();
		}
	}

	public void shootAt(Entity target) {
		double lead = Math.sqrt(target.distanceToSqr(this)) * weapon.aimLead / 5;

		double xd = (target.x + target.xa * lead) - x;
		double yd = (target.y + target.ya * lead) - y;
		double zd = (target.z + target.za * lead) - z;
		if (weapon.aimOnGround) {
			zd = (0) - (z + 5);
		}
		double dd = Math.sqrt(xd * xd + yd * yd + zd * zd);
		xd /= dd;
		yd /= dd;
		zd /= dd;
		weapon.shoot(xd, yd, zd);
	}

	private Entity findTarget() {
		double r = weapon.maxRange;
		List<Entity> es = level.getEntities(x - r, y - r, z - r, x + r, y + r, z + r);
		Entity closest = null;
		for (int i = 0; i < es.size(); i++) {
			Entity e = es.get(i);
			if (e instanceof Unit && e != this) {
				Unit u = (Unit) e;
				if (u.team != team && u.distanceToSqr(this) < r * r) {
					if (closest == null) {
						closest = e;
					} else if (e.distanceToSqr(this) < closest.distanceToSqr(this)) {
						closest = e;
					}
				}
			}
		}
		return closest;
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

		Bitmap[][] sheet = Art.i.balls;
		if (hurtTime > 0 && hurtTime / 2 % 2 == 1) {
			b.blendDraw(sheet[frame][ySpriteIndex], xp - 8, yp - 15, 0xffffffff);
		} else {
			b.draw(sheet[frame][ySpriteIndex], xp - 8, yp - 15);
		}
		b.xFlip = false;
		int dmg = (maxHealth - health) * 11 / maxHealth;
		b.fill(xp - 5, yp - 16, xp + 5, yp - 16, 0xffff0000);
		b.fill(xp - 5, yp - 16, xp + 5 - dmg, yp - 16, 0xff00ff00);
		int ammo = (weapon.maxAmmoLoaded - weapon.ammoLoaded) * 11 / weapon.maxAmmoLoaded;
		b.fill(xp - 5, yp - 15, xp + 5, yp - 15, 0xffffffff);
		b.fill(xp - 5, yp - 15, xp + 5 - ammo, yp - 15, 0xffb0b0b0);
	}

	public static Unit create(int unitClass) {
		Unit unit = null;
		if (unitClass == UNIT_POLAND) unit = new Poland();
		if (unitClass == UNIT_GERMANY) unit = new Germany();
		if (unitClass == UNIT_NETHERLANDS) unit = new Netherlands();
		if (unitClass == UNIT_FRANCE) unit = new France();
		if (unitClass == UNIT_RUSSIA) unit = new Russia();
		if (unitClass == UNIT_UKRAINE) unit = new Ukraine();
		if (unitClass == UNIT_FINLAND) unit = new Finland();
		if (unitClass == UNIT_JAPAN) unit = new Japan();
		return unit;
	}
	
	
	public void handleExplosion(Bullet source, int dmg, double xd, double yd, double zd) {
		if (this == source.owner) {
			dmg /= 2; //at least a dmg reduction if you're dumb enough to shoot with rockets at your own mate
		} else if (team == source.owner.team) {
			return;
		}
		hurt(dmg);
		knockBack(xd*2, yd*2, zd*2);
	}

	public void die() {
		for (int i = 0; i < 8; i++) {
			level.add(new MeatDebris(x, y, z + i));
		}
		weapon.playerDied();
		remove();
	}
}
