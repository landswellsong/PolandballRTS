package de.cirrus.polandball;

import de.cirrus.polandball.units.Unit;
import de.cirrus.polandball.units.order.MoveOrder;

import java.util.ArrayList;
import java.util.List;

public class Player {
	public Team team;
	public List<Unit> selected = new ArrayList<Unit>();

	public Player(Team team) {
		this.team = team;
	}

	public void sendAllSelectedTo(int x, int y) {
		for (Unit unit : selected) {
			unit.setOrder(new MoveOrder(x, y));
		}
	}

	//TODO Synchronize input actions

}
