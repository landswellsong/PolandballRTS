package de.cirrus.polandball;

import de.cirrus.polandball.level.Level;
import de.cirrus.polandball.units.Mob;
import de.cirrus.polandball.units.Unit;
import de.cirrus.polandball.units.order.MoveOrder;

import java.util.ArrayList;
import java.util.List;

public class Player {
	public Team team;
	public Level level;
	public List<Mob> selected = new ArrayList<Mob>();

	
	public Player(Level level, Team team) {
		this.team = team;
		this.level = level;
	}

	public void tick() {
	}
	
	public void sendAllSelectedTo(int x, int y, double xScroll, double yScroll) {
		double xx = (x + xScroll) / Sprite.SCALE_X / 2;
		double yy = (y + yScroll) / Sprite.SCALE_X;
		double xt = xx + yy;
		double yt = yy - xx;

		for (Mob unit : selected) {
			unit.setOrder(new MoveOrder(xt, yt));
		}
	}

	public boolean canSee(int x, int y) {
		if (x < 1 || y < 1 || x >= level.w - 1 || y >= level.h - 1) {
			return false;
		}
		return true;
	}

	public Mob getUnit(int unitClass) {
		for (Unit u : level.units) {
			if (u.team == team && u instanceof Mob && ((Mob) u).unitClass == unitClass) {
				return (Mob) u;
			}
		}
		return null;
	}

}
