package de.cirrus.polandball;

import de.cirrus.polandball.units.Mob;
import de.cirrus.polandball.units.Unit;

import java.util.TreeSet;

/**
 * User: Cirrus Date: 10.05.13 Time: 20:14
 */


public class PlayerView {
	private static final int SELECT_REGION_DISTANCE = 4;
	public Game game;
	public Player player;
	public Input input;

	public boolean dragging;
	public int xStartDrag;
	public int yStartDrag;

	public double xScroll = 442;
	public double yScroll = 286;
	public double xScrollA = 0;
	public double yScrollA = 0;
	public double xScrollT = 0;
	public double yScrollT = 0;
	public double scrollSpeed = 2;
	public int scrollSteps = 0;
	public int time = 0;
	

	public PlayerView(Game game, Player player, Input input) {
		this.game = game;
		this.player = player;
		this.input = input;
		
	}



	@SuppressWarnings("ConstantConditions")
	public void tick() {
		time++;

		xScrollA *= 0.2;
		yScrollA *= 0.2;
		if (input.up.down | input.down.down | input.left.down | input.right.down) {
			scrollSpeed += (6 - scrollSpeed) * 0.05;
		} else {
			scrollSpeed = 2;
		}
		if (input.up.down) yScrollA -= scrollSpeed;
		if (input.down.down) yScrollA += scrollSpeed;
		if (input.left.down) xScrollA -= scrollSpeed;
		if (input.right.down) xScrollA += scrollSpeed;

		xScroll += xScrollA;
		yScroll += yScrollA;
		if (scrollSteps > 0) {
			xScroll += (xScrollT - xScroll) / scrollSteps;
			yScroll += (yScrollT - yScroll) / scrollSteps;
			scrollSteps--;
		}
		
		if (input.b0Clicked) {
			dragging = true;
			xStartDrag = input.x;
			yStartDrag = input.y;
		}

		if (input.b1Clicked) {
			player.sendAllSelectedTo(input.x, input.y, xScroll, yScroll);

		}
		if (input.b0Released) {
			if (hasDraggedBox()) {
				selectAll(xStartDrag, yStartDrag, input.x, input.y);
			} else {
				selectNearest(xStartDrag, yStartDrag);
			}
			dragging = false;
		}
		
		if (input.b2Clicked) {
			//dragging = true;
			xStartDrag = input.x;
			yStartDrag = input.y;
		}
		
		if (input.b2) {
			xScroll -= input.x - xStartDrag;
			yScroll -= input.y - yStartDrag;
			xStartDrag = input.x;
			yStartDrag = input.y;
			xScrollA = yScrollA = 0;
			scrollSteps = 0;
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
		x0 += xScroll;
		y0 += yScroll;
		player.selected.clear();
		int r = 8;
		Unit nearest = null;
		for (Unit u : game.level.getUnitScreenSpace(x0 - r, y0 - r, x0 + r, y0 + r)) {
			if (u.team == player.team) {
				if (nearest == null || u.distanceToScreenSpaceSqr(x0, y0) < nearest.distanceToScreenSpaceSqr(x0, y0)) {
					nearest = u;
				}
			}
		}
		if (nearest != null && nearest instanceof Mob) {
			player.selected.add((Mob)nearest);
		}

	}

	public void selectAll(int x0, int y0, int x1, int y1) {
		x0 += xScroll;
		y0 += yScroll;
		x1 += xScroll;
		y1 += yScroll;
		
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
		screen.fill(0, 0, screen.w, screen.h, 0xffff00ff);
		game.renderRest(screen, (int) Math.floor(xScroll), (int) Math.floor(yScroll));
		TreeSet<Sprite> sortedSprites = new TreeSet<Sprite>(game.level.spriteComparator);
		sortedSprites.addAll(player.selected);
		for (Sprite s : sortedSprites) {
			if (!s.removed) {
				((Unit) s).renderSelected(screen);
			}
		}
		game.renderSprites(screen, (int) Math.floor(xScroll), (int) Math.floor(yScroll));
		screen.xOffs = 0;
		screen.yOffs = 0;
		
		if (dragging && hasDraggedBox()) {
			drawSelectBox(screen, xStartDrag, yStartDrag, input.x, input.y);
		} 
		
	}

	private boolean hasDraggedBox() {
		int xd = xStartDrag - input.x;
		int yd = yStartDrag - input.y;
		if (xd < 0) xd = -xd;
		if (yd < 0) yd = -yd;
		int d = xd > yd ? xd : yd;
		return d > SELECT_REGION_DISTANCE;
	}
	
	
}
