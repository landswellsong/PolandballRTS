package de.cirrus.polandball.level;

import java.util.ArrayList;
import java.util.List;

import de.cirrus.polandball.entities.Entity;

public class EntityListCache {
	private static List<ArrayList<Entity>> cache = new ArrayList<ArrayList<Entity>>(); // so I heard you like lists, so I put a list into a list so you can compare while you compare
	private static int cc = 0;
	
	public static List<Entity>get() {
		if (cc == cache.size()) {
			cache.add(new ArrayList<Entity>());			
		}
		
		List<Entity> el = cache.get(cc++);
		el.clear();
		return el;
	}
	
	public static void reset() {
		cc = 0;
	}
}
