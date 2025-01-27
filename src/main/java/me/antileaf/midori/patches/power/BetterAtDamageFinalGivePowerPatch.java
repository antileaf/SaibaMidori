package me.antileaf.midori.patches.power;

import basemod.ReflectionHacks;
import com.evacipated.cardcrawl.modthespire.lib.SpireInsertPatch;
import com.evacipated.cardcrawl.modthespire.lib.SpireInstrumentPatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import javassist.CannotCompileException;
import javassist.expr.ExprEditor;
import javassist.expr.MethodCall;
import me.antileaf.midori.powers.interfaces.BetterAtDamageFinalGivePower;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@SuppressWarnings("unused")
public class BetterAtDamageFinalGivePowerPatch {
	private static final Logger logger = LogManager.getLogger(BetterAtDamageFinalGivePowerPatch.class.getName());

	public static int cur = -1;

	public static float apply(BetterAtDamageFinalGivePower power, float damage,
							  DamageInfo.DamageType type, AbstractCard card, AbstractMonster m) {
//		logger.info("qwq");

		boolean isMultiDamage = ReflectionHacks.getPrivate(card, AbstractCard.class, "isMultiDamage");

		if (!isMultiDamage)
			return power.betterAtDamageFinalGive(damage, type, card, m);
		else {
			if (cur == -1) {
				logger.warn("Multi-damage card with no current index!");
				return power.betterAtDamageFinalGive(damage, type, card, null);
			}

			AbstractMonster target = AbstractDungeon.getCurrRoom().monsters.monsters.get(cur);
			cur = -1;
			return power.betterAtDamageFinalGive(damage, type, card, target);
		}
	}

	@SpirePatch(clz = AbstractCard.class, method = "applyPowers", paramtypez = {})
	public static class ApplyPowersPatch {
		@SpireInsertPatch(rloc = 78, localvars = {"i"})
		public static void Insert(AbstractCard _inst, int i) { // 记录当前的 i，方便后面判断是哪个怪
			cur = i;
		}

		@SpireInstrumentPatch
		public static ExprEditor Instrument() {
			return new ExprEditor() {
				@Override
				public void edit(MethodCall m) throws CannotCompileException {
					if (m.getMethodName().equals("atDamageFinalGive"))
						m.replace("{ if ($0 instanceof " +
								BetterAtDamageFinalGivePower.class.getName() +
								") { $_ = " + BetterAtDamageFinalGivePowerPatch.class.getName() +
								".apply((" + BetterAtDamageFinalGivePower.class.getName() +
								")$0, $$, null); } else { $_ = $proceed($$); } }");
				}
			};
		}
	}

	@SpirePatch(clz = AbstractCard.class, method = "calculateCardDamage",
			paramtypez = {AbstractMonster.class})
	public static class CalculateCardDamagePatch {
		@SpireInsertPatch(rloc = 99, localvars = {"i"})
		public static void Insert(AbstractCard _inst, AbstractMonster mo, int i) {
			cur = i; // 同上
		}

		@SpireInstrumentPatch
		public static ExprEditor Instrument() {
			return new ExprEditor() {
				@Override
				public void edit(MethodCall m) throws CannotCompileException {
					if (m.getMethodName().equals("atDamageFinalGive"))
						m.replace("{ if ($0 instanceof " +
								BetterAtDamageFinalGivePower.class.getName() +
								") { $_ = " + BetterAtDamageFinalGivePowerPatch.class.getName() +
								".apply((" + BetterAtDamageFinalGivePower.class.getName() +
								")$0, $$, mo); } else { $_ = $proceed($$); } }");
				}
			};
		}
	}
}
