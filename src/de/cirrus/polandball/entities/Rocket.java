package de.cirrus.polandball.entities;

import de.cirrus.polandball.Art;
import de.cirrus.polandball.Bitmap;
import de.cirrus.polandball.particles.FlameDebris;
import de.cirrus.polandball.units.Mob;
import de.cirrus.polandball.units.Unit;
import de.cirrus.polandball.weapons.Weapon;

public class Rocket extends Bullet {
	public int speed = 1;
	
	public Rocket(Mob owner, Weapon weapon, double xa, double ya, double za, int dmg) {
		super(owner, weapon, xa, ya, za, dmg);
		this.xa = xa * speed;
		this.ya = ya * speed;
		this.za = za * speed;
	}

	
	public boolean blocks(Entity e) {
		if (e == owner) return false;
		if (e instanceof Bullet) return false;
		return true;
	}
	
	public void tick() {
		xo = x;
		yo = y;
		zo = z;
		
		FlameDebris flame = new FlameDebris(x - xa * 2, y - ya * 2, z - za * 2);
		flame.xa *= 0.1;
		flame.ya *= 0.1;
		flame.za *= 0.1;
		flame.xa += xa * 1;
		flame.ya += ya * 1;
		flame.za += za * 1;
		flame.life = flame.maxLife / 2;
		level.add(flame);
		
		super.tick();
		attemptMove();
	}
	
	public void renderShadows(Bitmap b, int xp, int yp) {
		b.fill(xp - 1, yp, xp, yp, 1);
	}
	
	public void render(Bitmap b, int xp, int yp){
		int frame = (int) Math.floor(-Math.atan2(ya, xa) * 16 / (Math.PI*2) + 4.5) & 7;
		b.draw(Art.i.projectiles[frame][0], xp - 4, yp - 4);
	}
	
	public void collide(Entity e, double xxa, double yya, double zza) {
		if (e != null) {
			e.hitBy(this);
		}
		level.explode(this, x, y, z, dmg, 32); 
		remove();
	}
}
