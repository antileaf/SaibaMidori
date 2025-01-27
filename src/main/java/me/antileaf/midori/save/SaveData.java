package me.antileaf.midori.save;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import me.antileaf.midori.hue.Hue;
import me.antileaf.midori.hue.HueManager;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class SaveData {
	private static final Logger logger = LogManager.getLogger(SaveData.class.getName());

	public static SaveData saveData = null;

	public int hueRngCounter = 0;

//	@Deprecated
//	public Hue[] masterHues = null;

	public static SaveData save() {
		if (saveData == null)
			saveData = new SaveData();

//		saveData.masterHues = HueManager.save();
		return saveData;
	}

	public static void load(SaveData saveData) {
		SaveData.saveData = saveData;
	}
}
