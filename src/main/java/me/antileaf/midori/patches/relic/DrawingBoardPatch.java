package me.antileaf.midori.patches.relic;

import com.evacipated.cardcrawl.modthespire.lib.*;
import com.evacipated.cardcrawl.modthespire.patcher.PatchingException;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.unique.RetainCardsAction;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.powers.DrawReductionPower;
import com.megacrit.cardcrawl.powers.RetainCardPower;
import com.megacrit.cardcrawl.screens.select.HandCardSelectScreen;
import javassist.CannotCompileException;
import javassist.CtBehavior;
import me.antileaf.midori.powers.common.LessDrawPower;

@SuppressWarnings("unused")
public class DrawingBoardPatch {
	@SpirePatch(clz = RetainCardsAction.class, method = SpirePatch.CLASS)
	public static class Fields {
		public static SpireField<Boolean> drawingBoard = new SpireField<>(() -> false);
	}

	@SpirePatch(clz = RetainCardPower.class, method = "atEndOfTurn", paramtypez = {boolean.class})
	public static class PowerPatch {
		@SpirePostfixPatch
		public static void Postfix(RetainCardPower _inst, boolean isPlayer) {
			AbstractGameAction last = AbstractDungeon.actionManager.actions.get(
					AbstractDungeon.actionManager.actions.size() - 1);

			if (last instanceof RetainCardsAction) {
				RetainCardsAction action = (RetainCardsAction) last;

				action.amount += 2;
				Fields.drawingBoard.set(action, true);
			}
		}
	}

	@SpirePatch(clz = RetainCardsAction.class, method = "update", paramtypez = {})
	public static class ActionPatch {
		private static class Locator extends SpireInsertLocator {
			@Override
			public int[] Locate(CtBehavior ctBehavior) throws CannotCompileException, PatchingException {
				return LineFinder.findInOrder(ctBehavior,
						new Matcher.FieldAccessMatcher(HandCardSelectScreen.class, "selectedCards"));
			}
		}

		@SpireInsertPatch(locator = Locator.class)
		public static void Insert(RetainCardsAction _inst) {
			if (Fields.drawingBoard.get(_inst)) {
				int count = AbstractDungeon.handCardSelectScreen.selectedCards.size();
				if (count > _inst.amount - 2)
					AbstractDungeon.actionManager.addToTop(new ApplyPowerAction(
							AbstractDungeon.player, AbstractDungeon.player,
							new LessDrawPower(AbstractDungeon.player, count - (_inst.amount - 2))));
			}
		}
	}
}
