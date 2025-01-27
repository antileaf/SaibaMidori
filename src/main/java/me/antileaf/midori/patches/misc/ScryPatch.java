package me.antileaf.midori.patches.misc;

import com.evacipated.cardcrawl.modthespire.lib.*;
import com.evacipated.cardcrawl.modthespire.patcher.PatchingException;
import com.megacrit.cardcrawl.actions.utility.ScryAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import javassist.CannotCompileException;
import javassist.CtBehavior;

import java.util.ArrayList;

@SuppressWarnings("unused")
public class ScryPatch {
//	private static ArrayList<AbstractCard> discarded = new ArrayList<>();
//
//	public static ArrayList<AbstractCard> getDiscarded() {
//		ArrayList<AbstractCard> ret = discarded;
//		discarded = new ArrayList<>();
//		return ret;
//	}
//
//	@SpirePatch(clz = ScryAction.class, method = "update", paramtypez = {})
//	public static class ScryUpdatePatch {
//		private static class Locator extends SpireInsertLocator {
//			@Override
//			public int[] Locate(CtBehavior ctBehavior) throws CannotCompileException, PatchingException {
//				return LineFinder.findInOrder(ctBehavior,
//						new Matcher.MethodCallMatcher(ArrayList.class, "clear"));
//			}
//		}
//
//		@SpireInsertPatch(locator = Locator.class)
//		public static void Insert(ScryAction _inst) {
//			discarded.addAll(AbstractDungeon.gridSelectScreen.selectedCards);
//		}
//	}
}
