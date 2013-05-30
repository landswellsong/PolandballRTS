package de.cirrus.polandball;

import javax.imageio.ImageIO;
import java.awt.image.*;
import java.io.IOException;



public class Art {

	public static Art i;

	public static void init() {
		i = new Art();
	}


	public Bitmap[][] balls = loadAndCut("/balls/balls.png", 16, 16);
	public Bitmap[][] mouseCursor = loadAndCut("/cursors/mousecursor.png", 16, 16);
	public Bitmap[][] particles = loadAndCut("/particles/particles.png", 8, 8);
	public Bitmap[][] projectiles = loadAndCut("/projectiles/projectiles.png", 8, 8);
	public Bitmap[][] tiles = loadAndCut("/tiles/tiles.png", 24, 24);


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
	
	public static Bitmap load(String name) {
		BufferedImage img;
		try {
			img = ImageIO.read(Art.class.getResource(name));
		} catch (IOException e) {
			throw new RuntimeException("Failed to load " + "/levels/level.png");
		}

		int sw = img.getWidth();
		int sh = img.getHeight();

		Bitmap result = new Bitmap(sw, sh);
		img.getRGB(0, 0, sw, sh, result.pixels, 0, sw);

		return result;
	}

	public static Bitmap[][] recolor(Bitmap[][] bitmaps, int a0, int b0, int a1, int b1) {
		for (Bitmap[] bitmap : bitmaps) {
			for (Bitmap bm : bitmap) {
				for (int i = 0; i < bm.pixels.length; i++) {
					if (bm.pixels[i] == a0) bm.pixels[i] = b0;
					if (bm.pixels[i] == a1) bm.pixels[i] = b1;
				}
			}
		}
		return bitmaps;
	}
}
