package de.cirrus.polandball;

import de.cirrus.polandball.units.Mob;
import de.cirrus.polandball.units.Unit;

import java.util.TreeSet;

/**
 * User: Cirrus
 * Date: 10.05.13
 * Time: 20:14
 */
public class PlayerView {
	private static final int SELECT_REGION_DISTANCE = 4;
	public Game game;
	public Player player;
	public Input input;

	public boolean selecting;
	public int xStartSelect;
	public int yStartSelect;

	public PlayerView(Game game, Player player, Input input) {
		this.game = game;
		this.player = player;
		this.input = input;
	}

	public void tick() {
		if (input.b0Clicked) {
			selecting = true;
			xStartSelect = input.x;
			yStartSelect = input.y;
		}

		if (input.b1Clicked) {
			player.sendAllSelectedTo(input.x, input.y);

		}
		if (input.b0Released) {
			if (hasDraggedBox()) {
				selectAll(xStartSelect, yStartSelect, input.x, input.y);
			} else {
				selectNearest(xStartSelect, yStartSelect);
			}
			selecting = false;
		}
	}

	public void drawSelectBox(Bitmap screen, int x0, int y0, int x1, int y1) {
		if (x0 > x1) {
			int tmp = x0;
			x0 = x1;
			x1 = tmp;
		}
		if (y0 > y1) {
			int tmp = y0;
			y0 = y1;
			y1 = tmp;
		}
		screen.box(x0, y0, x1, y1, 0xff00ff00);
	}

	public void selectNearest(int x0, int y0) {
		player.selected.clear();
		int r = 8;
		Unit nearest = null;
		for (Unit u : game.level.getUnitScreenSpace(x0 - r, y0 - r, x0 + r, y0 + r)) {
			if (u.team == player.team) {
				if (nearest == null || u.distanceToScreenSpaceSqr(x0, y0) < nearest.distanceToScreenSpaceSqr(x0, y0));
					nearest = u;
			}
		}
		if (nearest != null && nearest instanceof Mob) {
			player.selected.add((Mob)nearest);
		}

	}

	public void selectAll(int x0, int y0, int x1, int y1) {
		if (x0 > x1) {
			int tmp = x0;
			x0 = x1;
			x1 = tmp;
		}
		if (y0 > y1) {
			int tmp = y0;
			y0 = y1;
			y1 = tmp;
		}

		int r = 8;

		player.selected.clear();

		for (Unit u : game.level.getUnitScreenSpace(x0 - r, y0 - r, x1 + r, y1 + r)) {
			if (u.team == player.team) {
				if (u instanceof Mob)
				player.selected.add((Mob)u);
			}
		}
	}

	public void render(Bitmap screen) {
		game.renderRest(screen);
		TreeSet<Sprite> sortedSprites = new TreeSet<Sprite>(game.level.spriteComparator);
		sortedSprites.addAll(player.selected);
		for (Sprite s : sortedSprites) {
			if (!s.removed) {
				((Unit) s).renderSelected(screen);
			}

		}
		game.renderSprites(screen);

		if (selecting && hasDraggedBox()) {
			drawSelectBox(screen, xStartSelect, yStartSelect, input.x, input.y);
		}
	}

	private boolean hasDraggedBox() {
		int xd = xStartSelect - input.x;
		int yd = yStartSelect - input.y;
		if (xd < 0) xd = -xd;
		if (yd < 0) yd = -yd;
		int d = xd > yd ? xd : yd;
		return d > SELECT_REGION_DISTANCE;
	}
}
