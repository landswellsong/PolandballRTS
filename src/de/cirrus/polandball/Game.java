package de.cirrus.polandball;

import java.awt.BorderLayout;
import java.awt.Canvas;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;

import javax.swing.JFrame;

import de.cirrus.polandball.entities.EntityListCache;
import de.cirrus.polandball.level.Level;
import de.cirrus.polandball.units.Unit;

public class Game extends Canvas implements Runnable {
	private static final long serialVersionUID = 1L;

	public static final int SCALE = 2;
	public static final int WIDTH = 960/SCALE;
	public static final int HEIGHT = 720/SCALE;
	
	public static final String TITLE = "Polandball";

	public Thread gameThread;

	public Bitmap screenBitmap;
	public Bitmap shadows;

	public static JFrame frame;
	public volatile boolean running = false;

	private Level level;
	private BufferedImage image;

	private Input mouse;
	private InputHandler input;
	
	public Game() {
		setPreferredSize(new Dimension(WIDTH * SCALE, HEIGHT * SCALE));
		setMaximumSize(new Dimension(WIDTH * SCALE, HEIGHT * SCALE));
		setMinimumSize(new Dimension(WIDTH * SCALE, HEIGHT * SCALE));

		input = new InputHandler(this);
	}

	public void init() {
		Art.init();
		image = new BufferedImage(WIDTH, HEIGHT, BufferedImage.TYPE_INT_ARGB);
		screenBitmap = new Bitmap(image);
		mouse = input.updateMouseStatus(SCALE);
		setCursor(Toolkit.getDefaultToolkit().createCustomCursor(new BufferedImage(16, 16, BufferedImage.TYPE_INT_ARGB), new Point(0,0), "invisible"));
		level = new Level(WIDTH, HEIGHT);
		shadows = new Bitmap(WIDTH, HEIGHT);
	}

	public synchronized void start() {
		running = true;
		gameThread = new Thread(this);
		gameThread.start();
	}

	public synchronized void stop() {
		running = false;
		try {
			gameThread.join();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	public void run() {
		init();
		double nsPerFrame = 1000000000D / 60D;
		double unprocessedTime = 0;
		double maxSkipFrame = 10;

		long lastTime = System.nanoTime();
		long lastFrameTime = System.currentTimeMillis();
		int frames = 0;
		int ticks = 0;
		while (running) {
			long now = System.nanoTime();
			double passedTime = (now - lastTime) / nsPerFrame;
			lastTime = now;

			if (passedTime < -maxSkipFrame) passedTime = -maxSkipFrame;
			if (passedTime > maxSkipFrame) passedTime = maxSkipFrame;

			unprocessedTime += passedTime;

			boolean render = true;

			while (unprocessedTime > 1) {
				unprocessedTime -= 1;
				mouse = input.updateMouseStatus(SCALE);
				EntityListCache.reset();
				tick();
				ticks++;
				render = true;
			}

			if (render) {
				EntityListCache.reset();
				render(screenBitmap);
				frames++;
			}

			if (System.currentTimeMillis() - lastFrameTime > 1000) {
				lastFrameTime += 1000;
				System.out.println("fps: " + frames + ", ticks: " + ticks);
				ticks = 0;
				frames = 0;
			}

			try {
				Thread.sleep(1);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			swap();
		}
	}

	private void tick() {
		if (mouse.b0Clicked){
			Unit unit = Unit.create((int)(Math.random()*8));
			unit.x = mouse.x;
			unit.y = mouse.y;
			level.add(unit);
		}
		if (mouse.b1Clicked){
			Unit unit = Unit.create(0);
			unit.x = mouse.x;
			unit.y = mouse.y;
			level.add(unit);
		}
		level.tick();
	}

	private void swap() {
		BufferStrategy bs = getBufferStrategy();
		if (bs == null) {
			createBufferStrategy(3);
			requestFocus();
			return;
		}

		Graphics g = bs.getDrawGraphics();
		g.setColor(Color.BLACK);
		g.fillRect(0, 0, getWidth(), getHeight());
		int w = getWidth() - WIDTH * SCALE;
		int h = getHeight() - HEIGHT * SCALE;

		g.drawImage(image, w / 2, h / 2, WIDTH * SCALE, HEIGHT * SCALE, null);
		g.dispose();
		bs.show();
	}

	private void render(Bitmap screen) {
		shadows.clear(0);

		level.renderBg(screen);
		level.renderShadows(shadows);
		screen.shade(shadows);
		level.renderSprites(screen);
		if (mouse.onScreen) screen.draw(Art.i.mouseCursor[0][0], mouse.x - 1, mouse.y - 1);
	}

	public static void main(String[] args) {
		Game game = new Game();

		frame = new JFrame(TITLE);
		frame.setLayout(new BorderLayout());
		frame.add(game);
		frame.pack();
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setResizable(false);
		frame.setAlwaysOnTop(true);
		frame.setLocationRelativeTo(null);
		frame.setVisible(true);

		game.start();
	}
}
