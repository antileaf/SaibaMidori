package me.antileaf.midori.hue;

import java.util.HashMap;
import java.util.Map;

public enum Hue {
	LAVA, MINT, SKY, INK,
	// 红，绿，蓝，黑

	POMELO, VIOLET, MARIGOLD;
	// 橙，紫，黄

	// 无色用 null

	private static Map<Hue, String> names;

	public static void initialize(Map<String, String> map) {
		names = new HashMap<>();

		for (Hue hue : values())
			names.put(hue, map.get(hue.name()));

		names.put(null, map.get("null"));
	}

	public String getName() {
		return names.get(this);
	}

	public static String getNoneName() {
		return names.get(null);
	}
}
