package de.cirrus.polandball.units;


import de.cirrus.polandball.Art;
import de.cirrus.polandball.Bitmap;
import de.cirrus.polandball.Player;
import de.cirrus.polandball.entities.Entity;
import de.cirrus.polandball.particles.FlameDebris;
import de.cirrus.polandball.particles.MeatDebris;
import de.cirrus.polandball.units.order.IdleOrder;
import de.cirrus.polandball.units.order.Order;
import de.cirrus.polandball.weapons.Revolver;
import de.cirrus.polandball.weapons.Weapon;

import java.util.List;

//TODO: everything
public class Mob extends Unit {
	public static final int UNIT_POLAND = 0;
	public static final int UNIT_GERMANY = 1;
	public static final int UNIT_NETHERLANDS = 2;
	public static final int UNIT_FRANCE = 3;
	public static final int UNIT_RUSSIA = 4;
	public static final int UNIT_UKRAINE = 5;
	public static final int UNIT_FINLAND = 6;
	public static final int UNIT_JAPAN = 7;
	public static final int UNIT_BRAZIL = 8;

	public int unitClass;
	public double walkStep = 0;
	public Weapon weapon = new Revolver(this);
	public double speed = 100;


	public Order order = new IdleOrder();


	public Mob(int unitClass, Player player) {
		super(player);
		this.unitClass = unitClass;
		xr = yr = 4;
	}

	public boolean blocks(Entity e) {
		if (e instanceof Mob) {
			Mob u = (Mob) e;
			if (u.team == team) return false;
		}
		return super.blocks(e);
	}

	public void tick() {
		if (deadTime > 0) {
			if (visRange > deadTime / 4) visRange = deadTime / 4;
			deadTime--;
			if (deadTime == 0) {
				remove();
			}
			return;
		}
		super.tick();

		if (burnTime > 0) {
			if (++burnInterval >= 30) {
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

		if (isOnGround()) {
			xa *= 0.5;
			ya *= 0.5;
		} else {
			xa *= 0.99;
			ya *= 0.99;
		}
		za -= 0.08;


		order.tick();
		if (order.finished()) {
			setOrder(getNextOrder());
		}

		attemptMove();

		if (xa * xa + ya * ya < 0.02) {
			walkStep = 0;
		}


		if (burnTime > 0) {
			FlameDebris fd = new FlameDebris(x + (random.nextDouble() - 0.5) * 2, y + (random.nextDouble() - 0.5) * 4, z + random.nextInt(12));
			fd.xa *= 0.1;
			fd.ya *= 0.1;
			fd.za *= 0.1;
			fd.life /= 2;
			level.add(fd);
		}

		//so the balls spread a little when they have the same goal
		double r = 4;
		for (Entity e : level.getEntities(x - r, y - r, z - r, x + r, y + r, z + r)) {
			if (e instanceof Mob && e != this) {
				Mob u = (Mob) e;
				if (u.team == team && u.isAlive()) {
					double xd = u.x - x;
					double yd = u.y - y;
					if (xd * xd + yd * yd < 0.01) {
						xd = 0.01;
						yd = 0;
					}
					double dd = Math.sqrt(xd*xd+yd*yd);
					if (dd < r * r) {
						xd = xd / dd / dd * 0.5;
						yd = yd / dd / dd * 0.5;
						this.knockBack(-xd, -yd, 0);
						u.knockBack(xd, yd, 0);
					}
				}
			}
		}
	}

	public boolean isAlive() {
		return health > 0;
	}

	public Order getNextOrder() {
		return new IdleOrder();
	}

	public void die() {
		for (int i = 0; i < 8; i++) {
			level.add(new MeatDebris(x, y, z + i));
		}
		weapon.playerDied();
		deadTime = 60 * 3;
		remove();
	}

	public void updateWeapon() {
		Entity target = findTarget();
		if ((weapon.maxAmmoLoaded == 0 || weapon.ammoLoaded > 0) && target != null) {
			shootAt(target);
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
		aimDir = Math.atan2(yd, zd);
		shootTime = 20;
	}


	private Entity findTarget() {
		double r = weapon.maxRange;
		List<Entity> es = level.getEntities(x - r, y - r, z - r, x + r, y + r, z + r);
		Entity closest = null;
		for (Entity e : es) {
			if (e instanceof Mob && e != this) {
				Mob u = (Mob) e;
				if (u.isAlive() && u.distanceToSqr(this) < r * r && isLegalTarget(u)) {
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

	public void render(Bitmap b, int xp, int yp) {
		if (deadTime > 0) return;
		renderStats(b);
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
			int dirFrame = (int) (-Math.floor(aimDir * 8 / (Math.PI * 2) - 1.5)) & 7;
			frame = dirFrame + 9;
			if (dirFrame > 4) {
				frame = 9 + 3 - (dirFrame - 5);
				b.xFlip = true;
			}
		}

		Bitmap[][] sheet = Art.i.balls;
		if (hurtTime > 0 && hurtTime / 2 % 2 == 1) {
			b.blendDraw(sheet[frame][unitClass], xp - 8, yp - 15, 0xffffffff);
		} else {
			b.draw(sheet[frame][unitClass], xp - 8, yp - 15);
		}
		b.xFlip = false;

	}

	public void renderShadows(Bitmap b, int xp, int yp) {
		if (deadTime > 0) return;
		super.renderShadows(b, xp, yp);
		int frame = 0;

		if (shootTime == 0) {
			int dirFrame = (int) (Math.floor(-dir * 4 / (Math.PI * 2) - 3)) & 3;
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
			int dirFrame = (int) (-Math.floor(aimDir * 8 / (Math.PI * 2) - 0.5)) & 7;
			frame = dirFrame + 9;
			if (dirFrame > 4) {
				frame = 9 + 3 - (dirFrame - 5);
				b.xFlip = true;
			}
		}

		Bitmap[][] sheet = Art.i.balls;
		if (hurtTime > 0 && hurtTime / 2 % 2 == 1) {
			b.blendDraw(sheet[frame][unitClass], xp - 8, yp - 15, 0xffffffff);
		} else {
			b.draw(sheet[frame][unitClass], xp - 8, yp - 15);
		}
		b.xFlip = false;
	}


	public void renderStats(Bitmap screen) {
		int xp = (int) x;
		int yp = (int) (y - z);
		int dmg = (maxHealth - health) * 16 / maxHealth;
		screen.fill(xp - 8, yp - 18, xp + 8, yp - 18, 0xffff0000);
		screen.fill(xp - 8, yp - 18, xp + 8 - dmg, yp - 18, 0xff00ff00);
		if (weapon.maxAmmoLoaded > 0) {
			int ammo = (weapon.maxAmmoLoaded - weapon.ammoLoaded) * 11 / weapon.maxAmmoLoaded;
			screen.fill(xp - 8, yp - 17, xp + 8, yp - 17, 0xff000000);
			screen.fill(xp - 8, yp - 17, xp + 8 - ammo, yp - 17, 0xffffcd00);
		}

	}


	public void setOrder(Order order) {
		this.order = order;
		order.init(this);
	}


	public void moveForwards() {
		double moveSpeed = 0.2 * speed / 100;
		if (!isOnGround()) {
			moveSpeed *= 0.1;
		}
		xa += Math.cos(dir) * moveSpeed;
		ya += Math.sin(dir) * moveSpeed;

		walkStep += speed / 100;
	}

	@SuppressWarnings("unused")
	public void jump() {
		za -= 1.5; // :3
	}


	public static Mob create(int unitClass, Player player) {
		Mob unit = null;
		if (unitClass == UNIT_POLAND) unit = new Poland(player);
		if (unitClass == UNIT_GERMANY) unit = new Germany(player);
		if (unitClass == UNIT_NETHERLANDS) unit = new Netherlands(player);
		if (unitClass == UNIT_FRANCE) unit = new France(player);
		if (unitClass == UNIT_RUSSIA) unit = new Russia(player);
		if (unitClass == UNIT_UKRAINE) unit = new Ukraine(player);
		if (unitClass == UNIT_FINLAND) unit = new Finland(player);
		if (unitClass == UNIT_JAPAN) unit = new Japan(player);
		if (unitClass == UNIT_BRAZIL) unit = new Brazil(player);
		return unit;
	}
}
