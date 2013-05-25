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

	public Player(Team team, Level level) {
		this.level = level;
		this.team = team;
	}

	public void sendAllSelectedTo(int x, int y) {
		for (Mob unit : selected) {
			unit.setOrder(new MoveOrder(x, y));
		}
	}

}
