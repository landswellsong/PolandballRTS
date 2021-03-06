package de.cirrus.polandball;

import java.awt.event.*;
import java.util.ArrayList;
import java.util.List;


public class Input {
	public static class Key {
		public int[] bindings = new int[0];
		public boolean wasDown;
		public boolean down;
		public boolean typed;
		
		public Key(Input input) {
			input.keys.add(this);
		}
		
		
		public Key bind(int key) {
			int[] newBindings = new int[bindings.length + 1];
			System.arraycopy(bindings, 0, newBindings, 0, bindings.length);
			newBindings[bindings.length] = key;
			bindings = newBindings;
			return this;
		}
		
		public void tick(boolean[] keysDown) {
			wasDown = down;
			down = false;
			for (int binding : bindings) {
				if (keysDown[binding]) down = true;
			}
			typed = !wasDown && down;
		}
	}
	
	public int x, y;
	
	public boolean onScreen;
	
	public boolean b0, b1, b2;
	public boolean b0Clicked;
	public boolean b1Clicked;
	public boolean b2Clicked;
	public boolean b0Released;
	public boolean b1Released;
	public boolean b2Released;
	
	public String typed = "";
	
	
	public List<Key> keys = new ArrayList<Key>();

	public Key up = new Key(this).bind(KeyEvent.VK_UP).bind(KeyEvent.VK_W);
	public Key down = new Key(this).bind(KeyEvent.VK_DOWN).bind(KeyEvent.VK_S);
	public Key left = new Key(this).bind(KeyEvent.VK_LEFT).bind(KeyEvent.VK_A);
	public Key right = new Key(this).bind(KeyEvent.VK_RIGHT).bind(KeyEvent.VK_D);

	
	//TODO: bindings here
	
	public Input() {
	}
	
	public void update(int x, int y, boolean b0, boolean b1, boolean b2, boolean onScreen, boolean[] keysDown, String typed) {
		b0Clicked = !this.b0 && b0;
		b1Clicked = !this.b1 && b1;
		b2Clicked = !this.b2 && b2;
		
		b0Released = this.b0 && !b0;
		b1Released = this.b1 && !b1;
		b2Released = this.b2 && !b2;
		
		this.x = x;
		this.y = y;
		this.b0 = b0;
		this.b1 = b1;
		this.b2 = b2;
		
		this.onScreen = onScreen;
		this.typed = "";
		
		for (Key key : keys) {
			key.tick(keysDown);
		}
		
	}
}
