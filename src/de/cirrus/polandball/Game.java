package de.cirrus.polandball;

import java.awt.BorderLayout;
import java.awt.Canvas;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;

import javax.swing.JFrame;

import de.cirrus.polandball.level.EntityListCache;
import de.cirrus.polandball.level.Level;
import de.cirrus.polandball.units.Unit;

//now I need ideas !!!!!11
//well you can't see much yet, but at least...wait

public class Game extends Canvas implements Runnable {
	private static final long serialVersionUID = 1L;

	public static final int WIDTH = 320;
	public static final int HEIGHT = 240;
	public static final int SCALE = 2;
	public static final String TITLE = "Polandball";

	public Thread gameThread;
	
	public Bitmap screenBitmap;
	public Bitmap shadows;
	
	public static JFrame frame;
	public volatile boolean running = false;

	private Level level;
	private BufferedImage image;

	public Game() {
		setPreferredSize(new Dimension(WIDTH * SCALE, HEIGHT * SCALE));
		setMaximumSize(new Dimension(WIDTH * SCALE, HEIGHT * SCALE));
		setMinimumSize(new Dimension(WIDTH * SCALE, HEIGHT * SCALE));

	}

	public void init() {
		Art.init();
		image = new BufferedImage(WIDTH, HEIGHT, BufferedImage.TYPE_INT_RGB);
		screenBitmap = new Bitmap(image);
		level = new Level(WIDTH, HEIGHT);
		shadows = new Bitmap(WIDTH, HEIGHT);
		for (int y = 0; y < HEIGHT/32; y++) {
			for (int x = 0; x < WIDTH/32; x++) {
				Unit u;
				u = Unit.create(0, 0);
				u.x = x*32;
				u.y = y*32;
				level.add(u);
			}
		}
	}

	//brb cigarette
	
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

	
	// NEED PERFORMANCE TUNING NAOW!
	// this is my method of doing this, it's probably not what they'd teach you in uni
	public void run() {
		init();
		double nsPerFrame = 1000000000D/60D; //O SHIT
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
				lastFrameTime+=1000; //so it updates every second
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
		level.tick();
	}

	private void swap() {
		BufferStrategy bs = getBufferStrategy();
		if (bs == null) {
			createBufferStrategy(3); //not much of a difference anyway
			requestFocus();
			return;
		}

		Graphics g = bs.getDrawGraphics();
		g.setColor(Color.BLACK);
		g.fillRect(0, 0, getWidth(), getHeight());
		int w = getWidth() - WIDTH*SCALE;
		int h = getHeight() - HEIGHT*SCALE;

		g.drawImage(image, w/2, h/2, WIDTH*SCALE, HEIGHT*SCALE, null);
		g.dispose();
		bs.show();
	}

	private void render(Bitmap screen) {
		shadows.clear(0);
		
		level.renderBg(screen);
		level.renderShadows(shadows);
		screen.shade(shadows);
		level.renderSprites(screen);
	}

	public static void main(String[] args) {
		// just writing down code here, no idea about the game yet
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
