package me.antileaf.midori.patches.fixes;

import com.evacipated.cardcrawl.mod.stslib.actions.common.SelectCardsAction;
import com.evacipated.cardcrawl.modthespire.lib.*;
import com.evacipated.cardcrawl.modthespire.patcher.PatchingException;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.screens.select.GridCardSelectScreen;
import javassist.CannotCompileException;
import javassist.CtBehavior;
import javassist.expr.ExprEditor;
import javassist.expr.MethodCall;

@SuppressWarnings("unused")
public class GridCardSelectScreenFixPatch {
	@SpirePatch(clz = SelectCardsAction.class, method = "update", paramtypez = {})
	public static class ChooseMultipleCardsFixPatch {
		private static class Locator extends SpireInsertLocator {
			@Override
			public int[] Locate(CtBehavior ctBehavior) throws CannotCompileException, PatchingException {
				int[] tmp = LineFinder.findInOrder(ctBehavior,
						new Matcher.MethodCallMatcher(GridCardSelectScreen.class, "open"));
				return new int[] { tmp[0] + 1 };
			}
		}

		@SpireInsertPatch(locator = Locator.class)
		public static void Insert(SelectCardsAction _inst) {
			AbstractDungeon.gridSelectScreen.forClarity = false;
			AbstractDungeon.gridSelectScreen.targetGroup.group.forEach(AbstractCard::stopGlowing);
		}
	}

	// 修复了只能选一张和一开始所有卡都是亮的这两个bug
	
//	@SpirePatch(clz = GridCardSelectScreen.class, method = "callOnOpen")
//	public static class InitiallyAllGlowingFixPatch {
//		@SpirePostfixPatch
//		public static void Postfix(GridCardSelectScreen instance) {
//			for (AbstractCard card : instance.targetGroup.group)
//				card.stopGlowing();
//		}
//	}
	
//	@SpirePatch(clz = GridCardSelectScreen.class, method = "hideCards")
//	public static class InitiallyAllGlowingFixPatch {
//		@SpirePostfixPatch
//		public static void Postfix(GridCardSelectScreen instance) {
//			for (AbstractCard card : instance.targetGroup.group)
//				card.darken(true);
//		}
//	}
}
