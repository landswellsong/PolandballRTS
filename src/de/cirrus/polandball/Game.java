package de.cirrus.polandball;

import de.cirrus.polandball.level.Level;

public class Game {
	public Player[] players = new Player[2];
	public Level level;

	private Bitmap shadows;

	public Game() {
		players[0] = new Player(Team.allied, level);
		players[1] = new Player(Team.soviet, level);
		level = new Level(512, 512, players);

	}

	public void tick() {
		level.tick();
	}

	public void renderRest (Bitmap screen) {
		if (shadows == null) shadows = new Bitmap(screen.w, screen.h);
		shadows.clear(0);

		level.renderBg(screen);
		level.renderShadows(shadows);
		screen.shade(shadows);
	}

	public void renderSprites(Bitmap screen) {
		level.renderSprites(screen); //we'll separate it for now
	}


}
