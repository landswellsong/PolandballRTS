package de.cirrus.polandball;

import java.awt.image.*;
import java.util.Arrays;


public class Bitmap {
	public final int[] pixels;
	public final int w, h;
	public int xOffs, yOffs;
	public boolean xFlip;


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

	public Bitmap(BufferedImage img) {
		this.w = img.getWidth();
		this.h = img.getHeight();

		pixels = ((DataBufferInt) img.getRaster().getDataBuffer()).getData();
	}

	public void draw(Bitmap b, int xp, int yp) {
		xp += xOffs;
		yp += yOffs;

		int x0 = xp;
		int x1 = xp + b.w;
		int y0 = yp;
		int y1 = yp + b.h;

		if (x0 < 0) x0 = 0;
		if (y0 < 0) y0 = 0;
		if (x1 > w) x1 = w;
		if (y1 > h) y1 = h;


		if (xFlip) {
			for (int y = y0; y < y1; y++) {
				int sp = (y - yp) * b.w + xp + b.w - 1;
				int dp = (y) * w;

				for (int x = x0; x < x1; x++) {
					int c = b.pixels[sp - x];
					if (c < 0) pixels[dp + x] = b.pixels[sp - x];
				}
			}
		} else {
			for (int y = y0; y < y1; y++) {
				int sp = (y - yp) * b.w - xp;
				int dp = (y) * w;

				for (int x = x0; x < x1; x++) {
					int c = b.pixels[sp + x];
					if (c < 0) pixels[dp + x] = b.pixels[sp + x];
				}
			}
		}
	}

	public void blendDraw(Bitmap b, int xp, int yp, int col) {
		xp += xOffs;
		yp += yOffs;
		int x0 = xp;
		int x1 = xp + b.w;
		int y0 = yp;
		int y1 = yp + b.h;
		if (x0 < 0) x0 = 0;
		if (y0 < 0) y0 = 0;
		if (x1 > w) x1 = w;
		if (y1 > h) y1 = h;

		if (xFlip) {
			for (int y = y0; y < y1; y++) {
				int sp = (y - yp) * b.w + xp + b.w - 1;
				int dp = (y) * w;

				for (int x = x0; x < x1; x++) {
					int c = b.pixels[sp - x];
					if (c < 0) pixels[dp + x] = ((b.pixels[sp - x] & 0xfefefefe)
							+ (col & 0xfefefefe)) >> 1;
				}
			}
		} else {
			for (int y = y0; y < y1; y++) {
				int sp = (y - yp) * b.w - xp;
				int dp = (y) * w;

				for (int x = x0; x < x1; x++) {
					int c = b.pixels[sp + x];
					if (c < 0) pixels[dp + x] = ((b.pixels[sp + x] & 0xfefefefe) + (col & 0xfefefefe)) >> 1;
				}
			}
		}
	}

	public void setPixel(int xp, int yp, int color) {
		xp += xOffs;
		yp += yOffs;
		if (xp >= 0 && yp >= 0 && xp < w && yp < h) {
			pixels[xp + yp * w] = color;
		}

	}

	public void clear(int color) {
		Arrays.fill(pixels, color);
	}

	public void shade(Bitmap shadows) {
		for (int i = 0; i < pixels.length; i++) {
			if (shadows.pixels[i] > 0) {
				int r = ((pixels[i] & 0xff0000) * 200) >> 8 & 0xff0000;
				int g = ((pixels[i] & 0xff00) * 200) >> 8 & 0xff00;
				int b = ((pixels[i] & 0xff) * 200) >> 8 & 0xff;
				pixels[i] = 0xff000000 | r | g | b;
			}
		}
	}

	public void fill(int x0, int y0, int x1, int y1, int color) {
		x0 += xOffs;
		x1 += xOffs;
		y0 += xOffs;
		y1 += xOffs;
		if (x0 < 0) x0 = 0;
		if (y0 < 0) y0 = 0;
		if (x1 >= w) x1 = w - 1;
		if (y1 >= h) y1 = h - 1;

		for (int y = y0; y <= y1; y++) {
			for (int x = x0; x <= x1; x++) {
				pixels[x + y * w] = color;
			}
		}
	}

	public void box(int x0, int y0, int x1, int y1, int color) {
		x0 += xOffs;
		x1 += xOffs;
		y0 += xOffs;
		y1 += xOffs;

		int xx0 = x0;
		int yy0 = y0;
		int xx1 = x1;
		int yy1 = y1;


		if (x0 < 0) x0 = 0;
		if (y0 < 0) y0 = 0;
		if (x1 >= w) x1 = w - 1;
		if (y1 >= h) y1 = h - 1;

		for (int y = y0; y <= y1; y++) {
			for (int x = x0; x <= x1; x++) {
				if (x == xx0 || y == yy0 || x == xx1 || y == yy1) pixels[x + y * w] = color;
				if (y > yy0 && y < yy1 && x < xx1 - 1) {
					x = xx1 - 1;
				}
			}
		}
	}
}
