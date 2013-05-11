package de.cirrus.polandball;

import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;



public class Art {

	public static Art i;

	public static void init() {
		i = new Art();
	}


	public Bitmap[][] balls = loadAndCut("/balls/balls.png", 16, 16);
	public Bitmap[][] mouseCursor = loadAndCut("/cursors/mousecursor.png", 16, 16);
	public Bitmap[][] particles = loadAndCut("/particles/particles.png", 8, 8);
	public Bitmap[][] projectiles = loadAndCut("/projectiles/projectiles.png", 8, 8);
	public Bitmap[][] tiles = loadAndCut("/tiles/tiles.png", 8, 8);


	public static Bitmap[][] loadAndCut(String name, int sw, int sh) {
		BufferedImage image;
		try {
			image = ImageIO.read(Art.class.getResource(name));
		} catch (IOException e) {
			throw new RuntimeException("failed to load: " + name);
		}
		
		int xSlices = image.getWidth() / sw;
		int ySlices = image.getHeight() / sh;


		Bitmap[][] result = new Bitmap[xSlices][ySlices];

		for (int x = 0; x < xSlices; x++) {
			for (int y = 0; y < ySlices; y++) {
				result[x][y] = new Bitmap(sw, sh);
				image.getRGB(x * sw, y * sh, sw, sh, result[x][y].pixels, 0, sw);
			}
		}
		return result;
	}
}
