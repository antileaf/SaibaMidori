package me.antileaf.midori.patches.hue;

import com.evacipated.cardcrawl.mod.stslib.StSLib;
import com.evacipated.cardcrawl.mod.stslib.patches.cardInterfaces.OnObtainCardPatches;
import com.evacipated.cardcrawl.modthespire.lib.*;
import com.evacipated.cardcrawl.modthespire.patcher.PatchingException;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.CardHelper;
import com.megacrit.cardcrawl.neow.NeowReward;
import com.megacrit.cardcrawl.rewards.RewardItem;
import com.megacrit.cardcrawl.saveAndContinue.SaveFile;
import com.megacrit.cardcrawl.vfx.FastCardObtainEffect;
import com.megacrit.cardcrawl.vfx.cardManip.ShowCardAndObtainEffect;
import javassist.CannotCompileException;
import javassist.CtBehavior;
import javassist.expr.ExprEditor;
import javassist.expr.MethodCall;
import me.antileaf.midori.hue.Hue;
import me.antileaf.midori.hue.HueCardModifier;
import me.antileaf.midori.hue.HueManager;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@SuppressWarnings("unused")
public class HueMechanicsPatch {
	private static final Logger logger = LogManager.getLogger(HueMechanicsPatch.class.getName());

//	@SpirePatch(clz = AbstractCard.class, method = SpirePatch.CLASS)
//	public static class Fields {
//		@Deprecated
//		public static SpireField<Hue> hue = new SpireField<>(() -> null);
//	}

	@SpirePatch(clz = AbstractDungeon.class, method = "generateSeeds", paramtypez = {})
	public static class GenerateSeedsPatch {
		@SpirePostfixPatch
		public static void Postfix() {
			HueManager.generateSeeds();
		}
	}

	@SpirePatch(clz = AbstractDungeon.class, method = "loadSeeds", paramtypez = {SaveFile.class})
	public static class LoadSeedsPatch {
		@SpirePostfixPatch
		public static void Postfix(SaveFile saveFile) {
			HueManager.loadSeeds();
		}
	}

	@Deprecated
	public static AbstractCard configure(AbstractCard card) {
		HueManager.configureOnSpawn(card);
		logger.info("Configuring on spawn: {}, hue = {}", card.cardID,
				HueManager.getHue(card));
		return card;
	}

	@SpirePatch(clz = AbstractDungeon.class, method = "getRewardCards", paramtypez = {})
	public static class GetRewardCardsPatch {
		@SpireInstrumentPatch
		public static ExprEditor Instrument() {
			return new ExprEditor() {
				@Override
				public void edit(MethodCall m) throws CannotCompileException {
					if (m.getMethodName().equals("makeCopy"))
						m.replace("$_ = " + HueMechanicsPatch.class.getName() +
								".configure($proceed($$));");
				}
			};
		}
	}

	@SpirePatch(clz = NeowReward.class, method = "getRewardCards", paramtypez = {boolean.class})
	public static class NeowRewardPatch {
		@SpireInstrumentPatch
		public static ExprEditor Instrument() {
			return new ExprEditor() {
				@Override
				public void edit(MethodCall m) throws CannotCompileException {
					if (m.getMethodName().equals("makeCopy"))
						m.replace("$_ = " + HueMechanicsPatch.class.getName() +
								".configure($proceed($$));");
				}
			};
		}
	}

	// TODO: 改成和 onPreviewObtainCard 一样的逻辑

	@SpirePatch(clz = ShowCardAndObtainEffect.class, method = SpirePatch.CONSTRUCTOR,
			paramtypez = {AbstractCard.class, float.class, float.class, boolean.class})
	public static class ShowCardAndObtainEffectPatch {
		@SpirePostfixPatch
		public static void Postfix(ShowCardAndObtainEffect _inst,
								   AbstractCard card, float x, float y, boolean convergeCards) {
			HueManager.configureWithCheck(card);
		}
	}

	@SpirePatch(clz = FastCardObtainEffect.class, method = SpirePatch.CONSTRUCTOR,
			paramtypez = {AbstractCard.class, float.class, float.class})
	public static class FastCardObtainEffectPatch {
		@SpirePostfixPatch
		public static void Postfix(FastCardObtainEffect _inst, AbstractCard card, float x, float y) {
			HueManager.configureWithCheck(card);
		}
	}

	@SpirePatch(clz = AbstractCard.class, method = "makeStatEquivalentCopy", paramtypez = {})
	public static class MakeStatEquivalentCopyPatch {
		@SpirePostfixPatch
		public static AbstractCard Postfix(AbstractCard _ret, AbstractCard _inst) {
			HueManager.configureOnCopy(_ret, _inst);
			return _ret;
		}
	}

	@SpirePatch(clz = StSLib.class, method = "onCreateCard", paramtypez = {AbstractCard.class})
	public static class SpawnInCombatPatch {
		@SpirePrefixPatch
		public static void Prefix(AbstractCard card) {
			HueManager.configureOnSpawn(card);
		}
	}
}
