package me.antileaf.midori.utils;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.rooms.AbstractRoom;
import me.antileaf.midori.MidoriCore;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.lang.reflect.Array;
import java.util.ArrayList;

public abstract class MidoriHelper {
	public static String getModPrefix() {
		return MidoriCore.MOD_ID + ":";
	}

	public static String makeID(String id) {
		return getModPrefix() + id;
	}

	public static String getImgFilePath(String type, String name) {
		return "SaibaMidori/img/" + type + "/" + name + ".png";
	}

	public static String getCardImgFilePath(String name) {
		return getImgFilePath("cards", name);
	}

	public static String getRelicImgFilePath(String name) {
		return getImgFilePath("relics", name);
	}

	public static String getRelicOutlineImgFilePath(String name) {
		return getImgFilePath("relics/outline", name);
	}

	public static String getRelicLargeImgFilePath(String name) {
		return getImgFilePath("relics/large", name);
	}

	public static String getLangShort() {
		return "zhs";
	}

	public static void addToBot(AbstractGameAction action) {
		AbstractDungeon.actionManager.addToBottom(action);
	}

	public static void addToTop(AbstractGameAction action) {
		AbstractDungeon.actionManager.addToTop(action);
	}

	public static void addActionsToTop(AbstractGameAction... actions) {
		for (int i = actions.length - 1; i >= 0; i--)
			addToTop(actions[i]);
	}

	private static ArrayList<AbstractGameAction> buffer = new ArrayList<>();

	public static void addActionToBuffer(AbstractGameAction action) {
		buffer.add(action);
	}

	public static void commitBuffer() {
		addActionsToTop(buffer.toArray(new AbstractGameAction[0]));
		buffer.clear();
	}

	public static boolean isInBattle() {
		return CardCrawlGame.dungeon != null &&
				AbstractDungeon.isPlayerInDungeon() &&
				AbstractDungeon.currMapNode != null &&
				AbstractDungeon.getCurrRoom().phase == AbstractRoom.RoomPhase.COMBAT;
	}
}
