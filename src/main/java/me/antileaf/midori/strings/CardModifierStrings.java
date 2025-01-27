package me.antileaf.midori.strings;

import me.antileaf.midori.utils.MidoriHelper;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Map;

public class CardModifierStrings {
	private static final Logger logger = LogManager.getLogger(CardModifierStrings.class.getName());

	public String NAME;
	public String DESCRIPTION;
	public String[] EXTENDED_DESCRIPTION = null;
	
	static Map<String, CardModifierStrings> strings = null;
	
	public static void init(Map<String, CardModifierStrings> strings) {
		CardModifierStrings.strings = strings;
	}
	
	public static CardModifierStrings get(String id) {
		if (!strings.containsKey(id))
			logger.warn("No such string: {}", id);
		
		return strings.get(id);
	}
}
