package de.cirrus.polandball;

import de.cirrus.polandball.entities.EntityListCache;

import javax.swing.*;
import java.awt.*;
import java.awt.image.*;


public class PolandBall extends Canvas implements Runnable {

	//Canvas specific variables
	private static final long serialVersionUID = 1L;
	public static final int SCALE = 3;
	public static final int HEIGHT = 720 / SCALE;
	public static final int WIDTH = HEIGHT * 16 / 9;
	public static final String TITLE = "Polandball";
	public static JFrame frame;
	public Thread gameThread;
	public Bitmap screenBitmap;
	public volatile boolean running = false;
	public Game game;
	public PlayerView playerView;
	private BufferedImage image;
	private Input mouse;
	private InputHandler input;

	public PolandBall() {
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
		setCursor(Toolkit.getDefaultToolkit().createCustomCursor(new BufferedImage(16, 16, BufferedImage.TYPE_INT_ARGB), new Point(0, 0), "invisible"));
		game = new Game();
		playerView = new PlayerView(game, game.players[0], mouse);
	}

	public synchronized void start() {
		running = true;
		gameThread = new Thread(this);
		gameThread.start();
	}

	@SuppressWarnings("unused")
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
				Thread.sleep(2);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			swap();
		}
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
		playerView.render(screen);
		if (mouse.onScreen) screen.draw(Art.i.mouseCursor[0][0], mouse.x - 1, mouse.y - 1);
	}

	private void tick() {
		game.tick();
		playerView.tick();
	}

	public static void main(String[] args) {
		PolandBall gameComponent = new PolandBall();


		frame = new JFrame(TITLE);
		frame.setLayout(new BorderLayout());
		frame.add(gameComponent);
		frame.pack();
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setResizable(false);
		frame.setAlwaysOnTop(true);
		frame.setLocationRelativeTo(null);
		frame.setVisible(true);

		gameComponent.start();
	}
}

