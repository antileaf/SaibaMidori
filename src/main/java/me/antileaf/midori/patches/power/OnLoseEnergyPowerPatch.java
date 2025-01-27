package me.antileaf.midori.patches.power;

import com.evacipated.cardcrawl.modthespire.lib.*;
import com.evacipated.cardcrawl.modthespire.patcher.PatchingException;
import com.megacrit.cardcrawl.core.EnergyManager;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.ui.panels.EnergyPanel;
import javassist.CannotCompileException;
import javassist.CtBehavior;
import me.antileaf.midori.powers.interfaces.OnLoseEnergyPower;

@SuppressWarnings("unused")
public class OnLoseEnergyPowerPatch {
	@SpirePatch(clz = EnergyManager.class, method = "use")
	public static class UsePatch {
		@SpirePrefixPatch
		public static void Prefix(EnergyManager _inst, int e) {
			for (AbstractPower p : AbstractDungeon.player.powers)
				if (p instanceof OnLoseEnergyPower)
					((OnLoseEnergyPower) p).onLoseEnergy(e, false);
		}
	}

	@SpirePatch(clz = EnergyManager.class, method = "recharge", paramtypez = {})
	public static class RechargePatch {
		private static class Locator extends SpireInsertLocator {
			@Override
			public int[] Locate(CtBehavior ctBehavior) throws CannotCompileException, PatchingException {
				return LineFinder.findInOrder(ctBehavior,
						new Matcher.MethodCallMatcher(EnergyPanel.class, "setEnergy"));
			}
		}

		@SpireInsertPatch(locator = Locator.class)
		public static void Insert(EnergyManager _inst) {
			for (AbstractPower p : AbstractDungeon.player.powers)
				if (p instanceof OnLoseEnergyPower)
					((OnLoseEnergyPower) p).onLoseEnergy(EnergyPanel.totalCount, true);
		}
	}
}
