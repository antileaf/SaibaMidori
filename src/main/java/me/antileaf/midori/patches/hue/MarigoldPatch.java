package me.antileaf.midori.patches.hue;

import basemod.ReflectionHacks;
import com.evacipated.cardcrawl.modthespire.lib.SpireInstrumentPatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePrefixPatch;
import com.evacipated.cardcrawl.modthespire.lib.SpireReturn;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ExhaustAction;
import com.megacrit.cardcrawl.actions.common.ExhaustSpecificCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import javassist.CannotCompileException;
import javassist.expr.ExprEditor;
import javassist.expr.MethodCall;
import me.antileaf.midori.hue.Hue;
import me.antileaf.midori.hue.HueManager;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@SuppressWarnings("unused")
public class MarigoldPatch {
	@SpirePatch(clz = ExhaustSpecificCardAction.class, method = "update", paramtypez = {})
	public static class ExhaustSpecificCardActionPatch {
		@SpirePrefixPatch
		public static SpireReturn<Void> Prefix(ExhaustSpecificCardAction _inst) {
			float duration = ReflectionHacks.getPrivate(_inst,
					AbstractGameAction.class, "duration");
			float startingDuration = ReflectionHacks.getPrivate(_inst,
					ExhaustSpecificCardAction.class, "startingDuration");

			if (duration == startingDuration) {
				AbstractCard card = ReflectionHacks.getPrivate(_inst,
						ExhaustSpecificCardAction.class, "targetCard");

				if (HueManager.getHue(card) == Hue.MARIGOLD) {
					card.flash();
					HueManager.remove(card);

					ReflectionHacks.privateMethod(AbstractGameAction.class,
							"tickDuration").invoke(_inst);

					return SpireReturn.Return(null);
				}
			}

			return SpireReturn.Continue();
		}
	}

	public static boolean removeMarigold(AbstractCard card) {
		if (HueManager.getHue(card) != Hue.MARIGOLD)
			return false;

		if (AbstractDungeon.player.hand.contains(card) ||
				AbstractDungeon.player.limbo.contains(card))
			card.flash();
		HueManager.remove(card);

		return true;
	}

	@SpirePatch(clz = ExhaustAction.class, method = "update", paramtypez = {})
	public static class ExhaustActionPatch {
		@SpireInstrumentPatch
		public static ExprEditor Instrument() {
			return new ExprEditor() {
				@Override
				public void edit(MethodCall m) throws CannotCompileException {
					if (m.getMethodName().equals("moveToExhaustPile"))
						m.replace("{ if (" + MarigoldPatch.class.getName() +
								".removeMarigold($1)) { } else { $proceed($$); } }");
				}
			};
		}
	}

	@SpirePatch(clz = CardGroup.class, method = "moveToExhaustPile", paramtypez = {AbstractCard.class})
	public static class MoveToExhaustPilePatch {
		private static final Logger logger = LogManager.getLogger(MoveToExhaustPilePatch.class.getName());

		@SpirePrefixPatch
		public static SpireReturn<Void> Prefix(CardGroup _inst, AbstractCard c) {
			if (removeMarigold(c)) {
				logger.warn("Trying to move Marigold card to exhaust pile");
				return SpireReturn.Return();
			}

			return SpireReturn.Continue();
		}
	}
}
