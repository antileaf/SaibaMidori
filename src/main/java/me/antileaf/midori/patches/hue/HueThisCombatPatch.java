package me.antileaf.midori.patches.hue;

import com.evacipated.cardcrawl.modthespire.lib.*;
import com.evacipated.cardcrawl.modthespire.patcher.PatchingException;
import com.megacrit.cardcrawl.actions.GameActionManager;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import javassist.CannotCompileException;
import javassist.CtBehavior;
import me.antileaf.midori.hue.Hue;
import me.antileaf.midori.hue.HueManager;

@SuppressWarnings("unused")
public class LastHueThisCombatPatch {
	public static Hue get() {
		return Fields.lastHueThisCombat.get(AbstractDungeon.actionManager);
	}

	@SpirePatch(clz = GameActionManager.class, method = SpirePatch.CLASS)
	public static class Fields {
		public static SpireField<Hue> lastHueThisCombat = new SpireField<>(() -> null);
	}

	@SpirePatch(clz = GameActionManager.class, method = "getNextAction",
			paramtypez = {})
	public static class UpdatePatch {
		private static class Locator extends SpireInsertLocator {
			@Override
			public int[] Locate(CtBehavior ctBehavior) throws CannotCompileException, PatchingException {
				return LineFinder.findInOrder(ctBehavior,
						new Matcher.FieldAccessMatcher(GameActionManager.class, "cardsPlayedThisCombat"));
			}
		}

		@SpireInsertPatch(locator = Locator.class)
		public static void Insert(GameActionManager _inst) {
			Fields.lastHueThisCombat.set(_inst,
					HueManager.getHue(_inst.cardQueue.get(0).card));
		}
	}

	@SpirePatch(clz = GameActionManager.class, method = "clear")
	public static class ClearPatch {
		public static void Postfix(GameActionManager _inst) {
			Fields.lastHueThisCombat.set(_inst, null);
		}
	}
}
