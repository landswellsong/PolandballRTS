package de.cirrus.polandball;

import java.awt.image.BufferedImage;
import java.awt.image.DataBufferInt;
import java.util.Arrays;

public class Bitmap {

	public final int[] pixels;
	public final int w, h;
	public int xOffs, yOffs;
	public boolean xFlip, yFlip;

	public Bitmap(int w, int h) {
		this.w = w;
		this.h = h;
		pixels = new int[w * h];
	}

	public Bitmap(int w, int h, int[] pixels) {
		this.w = w;
		this.h = h;
		this.pixels = pixels;
	}

	public Bitmap(BufferedImage image) {
		this.w = image.getWidth();
		this.h = image.getHeight();
		pixels = ((DataBufferInt) image.getRaster().getDataBuffer()).getData();
	}

	public void draw(Bitmap b, int xPos, int yPos) {
		xPos += xOffs;
		yPos += yOffs;
		int x0 = xPos;
		int y0 = yPos;
		int x1 = xPos + b.w;
		int y1 = yPos + b.h;
		if (x0 < 0)
			x0 = 0;
		if (y0 < 0)
			y0 = 0;
		if (x1 > w)
			x1 = w;
		if (y1 > h)
			y1 = h;

		if (xFlip) {
			for (int y = y0; y < y1; y++) {
				int sp = (y - yPos) * b.w + xPos + b.w - 1;
				int dp = y * w;
				for (int x = x0; x < x1; x++) {
					int c = b.pixels[sp - x];
					if (c < 0)
						pixels[dp + x] = b.pixels[sp - x];
				}
			}
		} else {
			for (int y = y0; y < y1; y++) {
				int sp = (y - yPos) * b.w - xPos;
				int dp = y * w;
				for (int x = x0; x < x1; x++) {
					int c = b.pixels[sp + x];
					if (c < 0)
						pixels[dp + x] = b.pixels[sp + x];
				}
			}
		}
	}


	public void setPixel(int xp, int yp, int col) {
		xp += xOffs;
		yp += yOffs;
		if (xp >= 0 && yp >= 0 && xp < w && yp < h) {
			pixels[xp + yp * w] = col;
		}
	}

	public void clear(int color) {
		Arrays.fill(pixels, color);
	}

	public void fill(int x0, int y0, int x1, int y1, int col) {
		if (x0 < 0)
			x0 = 0;
		if (y0 < 0)
			y0 = 0;
		if (x1 >= w)
			x1 = w - 1;
		if (y1 >= h)
			y1 = h - 1;

		for (int y = y0; y <= y1; y++) {
			for (int x = x0; x <= x1; x++) {
				pixels[x + y * w] = col;
			}
		}
	}

	public void shade(Bitmap shadows) {
		for (int i = 0; i < pixels.length; i++) {
			if (shadows.pixels[i] > 0) {
				int r = ((pixels[i] & 0xff0000) * 200) >> 8 & 0xff0000;
				int g = ((pixels[i] & 0xff00) * 200) >> 8 & 0xff00;
				int b = ((pixels[i] & 0xff) * 200) >> 8 & 0xff;
				pixels[i] = 0xff000000 | r | b | g;
			}
		}
	}
}
