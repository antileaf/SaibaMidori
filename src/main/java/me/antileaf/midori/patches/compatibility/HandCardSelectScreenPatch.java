package me.antileaf.midori.patches.compatibility;

import basemod.ReflectionHacks;
import com.evacipated.cardcrawl.modthespire.lib.*;
import com.evacipated.cardcrawl.modthespire.patcher.PatchingException;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.screens.select.HandCardSelectScreen;
import javassist.CannotCompileException;
import javassist.CtBehavior;
import javassist.expr.ExprEditor;
import javassist.expr.MethodCall;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.*;

@SuppressWarnings("unused")
public class HandCardSelectScreenPatch {
	private static boolean enabled = false;
	private static Map<AbstractCard, Integer> rank;

	public static void enable() {
		enabled = true;
	}

	public static void disable() {
		enabled = false;
		rank = null;
	}

	@SpirePatch(clz = HandCardSelectScreen.class, method = "prep", paramtypez = {})
	public static class PrepPatch {
		@SpirePostfixPatch
		public static void Postfix(HandCardSelectScreen _inst) {
			if (enabled) {
				rank = new HashMap<>();

				for (int i = 0; i < AbstractDungeon.player.hand.size(); i++)
					rank.put(AbstractDungeon.player.hand.group.get(i), AbstractDungeon.player.hand.size() - i - 1);
			}
		}
	}

//	private static class HandLocator extends SpireInsertLocator {
//		@Override
//		public int[] Locate(CtBehavior ctBehavior) throws CannotCompileException, PatchingException {
//			int[] top = LineFinder.findAllInOrder(ctBehavior,
//					new Matcher.MethodCallMatcher(CardGroup.class, "addToTop"));
//			int[] hand = LineFinder.findAllInOrder(ctBehavior,
//					new Matcher.FieldAccessMatcher(AbstractPlayer.class, "hand"));
//
//			ArrayList<Integer> ret = new ArrayList<>();
//			for (int i : top)
//				for (int j : hand)
//					if (i == j)
//						ret.add(i);
//
//			return ret.stream().mapToInt(i -> i + 1).toArray();
//		}
//	}

	public static void sortHand() {
		if (rank == null)
			return;

		CardGroup clone = new CardGroup(CardGroup.CardGroupType.UNSPECIFIED);
		for (int i = 0; i < rank.size(); i++)
			for (AbstractCard c : AbstractDungeon.player.hand.group)
				if (rank.get(c) == i) {
					clone.addToBottom(c);
					AbstractDungeon.player.hand.removeCard(c);
					break;
				}

		for (AbstractCard c : clone.group)
			AbstractDungeon.player.hand.addToTop(c);

		ReflectionHacks.privateMethod(HandCardSelectScreen.class, "refreshSelectedCards")
				.invoke(AbstractDungeon.handCardSelectScreen);

		AbstractDungeon.player.hand.refreshHandLayout();
	}

	private static boolean mark = false;

	private static class Locator extends SpireInsertLocator {
		@Override
		public int[] Locate(CtBehavior ctBehavior) throws CannotCompileException, PatchingException {
			return LineFinder.findAllInOrder(ctBehavior,
					new Matcher.MethodCallMatcher(CardGroup.class, "refreshHandLayout"));
		}
	}

	@SpirePatch(clz = HandCardSelectScreen.class, method = "updateControllerInput")
	public static class UpdateControllerInputPatch {
		@SpireInsertPatch(locator = Locator.class)
		public static void Insert(HandCardSelectScreen _inst) {
			if (enabled)
				mark = true;
		}

		@SpirePostfixPatch
		public static void Postfix(HandCardSelectScreen _inst) {
			if (enabled && mark) {
				sortHand();
				mark = false;
			}
		}
	}

	@SpirePatch(clz = HandCardSelectScreen.class, method = "selectHoveredCard")
	public static class SelectHoveredCardPatch {
		@SpireInsertPatch(locator = Locator.class)
		public static void Insert(HandCardSelectScreen _inst) {
			if (enabled)
				mark = true;
		}

		@SpirePostfixPatch
		public static void Postfix(HandCardSelectScreen _inst) {
			if (enabled && mark) {
				sortHand();
				mark = false;
			}
		}
	}

	@SpirePatch(clz = HandCardSelectScreen.class, method = "updateSelectedCards")
	public static class UpdateSelectedCardsPatch {
		@SpireInsertPatch(locator = Locator.class)
		public static void Insert(HandCardSelectScreen _inst) {
			if (enabled)
				mark = true;
		}

		@SpirePostfixPatch
		public static void Postfix(HandCardSelectScreen _inst) {
			if (enabled && mark) {
				sortHand();
				mark = false;
			}
		}
	}
}
