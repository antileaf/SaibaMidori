package me.antileaf.midori.patches.card;

import com.evacipated.cardcrawl.modthespire.lib.*;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.vfx.AbstractGameEffect;
import com.megacrit.cardcrawl.vfx.cardManip.ShowCardAndAddToDiscardEffect;
import com.megacrit.cardcrawl.vfx.cardManip.ShowCardAndAddToDrawPileEffect;
import com.megacrit.cardcrawl.vfx.cardManip.ShowCardAndAddToHandEffect;
import javassist.CannotCompileException;
import javassist.expr.ExprEditor;
import javassist.expr.MethodCall;
import me.antileaf.midori.cards.deprecated.SpreadingSpore;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Arrays;
import java.util.function.Function;
import java.util.function.Predicate;

@Deprecated
@SuppressWarnings("unused")
public class ReplaceTempCardsInCombatPatch {
	private static final Logger logger = LogManager.getLogger(ReplaceTempCardsInCombatPatch.class.getName());

	private static class Rule {
		public final Predicate<AbstractCard> predicate;
		public final Function<AbstractCard, AbstractCard> generate;

		public Rule(Predicate<AbstractCard> predicate, Function<AbstractCard, AbstractCard> generate) {
			this.predicate = predicate;
			this.generate = generate;
		}
	}

	// 优先级从高到低
	private static final Rule[] rules = {
			new Rule(card -> AbstractDungeon.player.masterDeck.group.stream()
					.anyMatch(c -> c instanceof SpreadingSpore),
					card -> {
						AbstractCard spore = new SpreadingSpore();

						if (AbstractDungeon.player.masterDeck.group.stream()
								.filter(c -> c instanceof SpreadingSpore)
								.anyMatch(c -> c.upgraded))
							spore.upgrade();

						return spore;
					})
	};

	public static AbstractCard handle(AbstractCard card) {
		return Arrays.stream(rules)
				.filter(rule -> rule.predicate.test(card))
				.findFirst()
				.map(rule -> rule.generate.apply(card))
				.orElse(card);
	}

	@SpirePatches({
			@SpirePatch(clz = ShowCardAndAddToDiscardEffect.class, method = SpirePatch.CONSTRUCTOR,
					paramtypez = {AbstractCard.class, float.class, float.class}),
			@SpirePatch(clz = ShowCardAndAddToDrawPileEffect.class, method = SpirePatch.CONSTRUCTOR,
					paramtypez = {AbstractCard.class, boolean.class, boolean.class}),
			@SpirePatch(clz = ShowCardAndAddToDiscardEffect.class, method = SpirePatch.CONSTRUCTOR,
					paramtypez = {AbstractCard.class}),
			@SpirePatch(clz = ShowCardAndAddToDrawPileEffect.class, method = SpirePatch.CONSTRUCTOR,
					paramtypez = {AbstractCard.class, float.class, float.class,
							boolean.class, boolean.class, boolean.class})
	})
	public static class MakeStatEquivalentCopyPatch {
		@SpireInstrumentPatch
		public static ExprEditor Instrument() {
			return new ExprEditor() {
				@Override
				public void edit(MethodCall m) throws CannotCompileException {
					if (m.getClassName().equals(AbstractCard.class.getName()) &&
							m.getMethodName().equals("makeStatEquivalentCopy"))
						m.replace("{ $_ = " + ReplaceTempCardsInCombatPatch.class.getName() +
								".handle($0); }");
				}
			};
		}
	}

	@SpirePatches2({
			@SpirePatch2(clz = ShowCardAndAddToHandEffect.class, method = SpirePatch.CONSTRUCTOR,
					paramtypez = {AbstractCard.class, float.class, float.class}),
			@SpirePatch2(clz = ShowCardAndAddToHandEffect.class, method = SpirePatch.CONSTRUCTOR,
					paramtypez = {AbstractCard.class}),
	})
	public static class ReplacePatch {
		@SpirePrefixPatch
		public static void Prefix(AbstractGameEffect __instance, @ByRef AbstractCard[] card) {
			card[0] = handle(card[0]);

			logger.info("Working on {}", __instance.getClass().getSimpleName());
		}
	}
}
