package de.cirrus.polandball.units;

import de.cirrus.polandball.Art;
import de.cirrus.polandball.Bitmap;
import de.cirrus.polandball.Player;
import de.cirrus.polandball.Team;
import de.cirrus.polandball.entities.Bullet;
import de.cirrus.polandball.entities.Entity;
import de.cirrus.polandball.level.Level;
import de.cirrus.polandball.particles.Debris;
import de.cirrus.polandball.particles.SplatDebris;

import java.util.Random;

public class Unit extends Entity {
	public static final Random random = new Random();
	public Team team;
	public double dir;
	public int shootTime;
	public double walkStep;
	public int maxHealth = 125;
	public int health = 125;
	public int hurtTime = 0;
	public int burnTime;
	public int burnInterval;
	public double aimDir;
	public double visRange = 8; //hypothethical
	public Player player;
	public int deadTime; //that too

	public Unit(Player player) {
		this.player = player;
		team = player.team;
	}

	public void init(Level level) {
		super.init(level);
		level.units.add(this);
	}

	public void onRemoved() {
		level.units.remove(this);
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

	public void knockBack(double xxa, double yya, double zza) {
		xa += (xxa - xa) * 0.4;
		ya += (yya - ya) * 0.4;
		za += (zza - za) * 0.4;
	}

	public boolean isOnGround() {
		return z <= 1;
	}

	public boolean isLegalTarget(Mob u) {
		return u.team != team;
	}

	public void hurt(int damage) {
		this.health -= damage;
		hurtTime = 20;
	}

	public void handleExplosion(Bullet source, int dmg, double xd, double yd, double zd) {
		if (this == source.owner) {
			dmg /= 2;
		} else if (team == source.owner.team) {
			return;
		}
		hurt(dmg);
		knockBack(xd * 2, yd * 2, zd * 2);
	}

	public void renderSelected(Bitmap screen) {
		int xp = (int) x - 8;
		int yp = (int) (y - z - 13);

		//int r = 10;
		//screen.box(xp - r, yp - r, xp + r, yp + r, 0xff00ff00);
		screen.draw(Art.i.mouseCursor[1][0], xp, yp);

	}

	public double distanceToScreenSpaceSqr(double x0, double y0) {
		double xx = x;
		double yy = y - z - 6;

		double xd = xx - x0;
		double yd = yy - y0;

		return xd * xd + yd * yd;
	}

	public boolean intersectsScreenSpace(double x0, double y0, double x1, double y1) {
		double xx = x;
		double yy = y - z - 6;
		int ww = 4;
		int hh = 6;
		if (x1 <= xx - ww || x0 > xx + ww || y1 <= yy - hh || y0 > yy + hh) return false;
		return true;
	}

	public double distanceTo(double x, double y) {
		double xd = x - this.x;
		double yd = y - this.y;

		return Math.sqrt(xd * xd + yd * yd);
	}

	public double angleTo(double x, double y) {
		return Math.atan2(y - this.y, x - this.x);
	}

	public boolean turnTowards(double angle) {
		while (dir < -Math.PI) dir += Math.PI * 2;
		while (dir >= Math.PI) dir -= Math.PI * 2;
		while (angle < -Math.PI) angle += Math.PI * 2;
		while (angle >= Math.PI) angle -= Math.PI * 2;

		double angleDiff = angle - dir;

		while (angleDiff < -Math.PI) angleDiff += Math.PI * 2;
		while (angleDiff >= Math.PI) angleDiff -= Math.PI * 2;

		double turnSpeed = 0.2;
		double near = 1;

		boolean wasAimed = angleDiff * angleDiff < near * near;
		if (angleDiff < -turnSpeed) angleDiff = -turnSpeed;
		if (angleDiff > +turnSpeed) angleDiff = +turnSpeed;

		dir += angleDiff;

		return wasAimed;

	}

	public void heal(int toHeal) {
		int maxHeal = maxHealth - health;
		if (maxHeal <= 0) {
			return;
		}
		if (toHeal > maxHeal) toHeal = maxHeal;
		health += toHeal;
	}

}
