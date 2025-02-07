package me.antileaf.midori.patches.card;

import com.evacipated.cardcrawl.modthespire.lib.*;
import com.evacipated.cardcrawl.modthespire.patcher.PatchingException;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.utility.UseCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.random.Random;
import javassist.CannotCompileException;
import javassist.CtBehavior;
import me.antileaf.midori.cards.interfaces.ConditionalExhaustCard;

import java.util.Arrays;

@SuppressWarnings("unused")
public class ConditionalExhaustCardsPatch {
//	@SpirePatch(clz = UseCardAction.class, method = SpirePatch.CONSTRUCTOR,
//			paramtypez = {AbstractCard.class, AbstractCreature.class})
//	public static class UseCardActionPatch {
//		private static class Locator extends SpireInsertLocator {
//			@Override
//			public int[] Locate(CtBehavior ctBehavior) throws CannotCompileException, PatchingException {
////				int[] a = LineFinder.findAllInOrder(ctBehavior,
////						new Matcher.MethodCallMatcher(AbstractPlayer.class, "hasRelic"));
////				int[] b = LineFinder.findAllInOrder(ctBehavior,
////						new Matcher.MethodCallMatcher(Random.class, "randomBoolean"));
////
////				int[] intersect = Arrays.stream(a)
////						.filter(x -> Arrays.stream(b).anyMatch(y -> x == y - 1))
////						.toArray();
////
////				return new int[]{intersect[0] - 2};
//
//				return LineFinder.findInOrder(ctBehavior,
//						new Matcher.MethodCallMatcher(AbstractGameAction.class, "setValues"));
//			}
//		}
//
//		@SpireInsertPatch(locator = Locator.class)
////		@SpireInsertPatch(rloc = 6)
//		public static void Insert(UseCardAction _inst, AbstractCard card, AbstractCreature target) {
//			if (!(card instanceof ConditionalExhaustCard))
//				return;
//
//			_inst.exhaustCard |= ((ConditionalExhaustCard) card).shouldExhaust();
//		}
//	}

	@SpirePatch(clz = UseCardAction.class, method = "update")
	public static class UseCardActionUpdatePatch {
		@SpirePrefixPatch
		public static void Prefix(UseCardAction _inst, AbstractCard ___targetCard) {
			if (!(___targetCard instanceof ConditionalExhaustCard))
				return;

			_inst.exhaustCard |= ((ConditionalExhaustCard) ___targetCard).shouldExhaust();
		}
	}
}
