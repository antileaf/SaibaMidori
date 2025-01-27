package me.antileaf.midori.fatigue;

import com.megacrit.cardcrawl.actions.common.GainEnergyAction;
import com.megacrit.cardcrawl.actions.common.ReducePowerAction;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.UIStrings;
import me.antileaf.midori.actions.fatigue.FatigueDamageAction;
import me.antileaf.midori.actions.utils.AnonymousAction;
import me.antileaf.midori.powers.unique.FutureSightPower;
import me.antileaf.midori.utils.MidoriHelper;

public class FatigueManager {
	private static final UIStrings uiStrings = CardCrawlGame.languagePack.getUIString(
			MidoriHelper.makeID("Fatigue"));

	public static int fatigue = 1;
	public static int limit = 10;
	public static boolean unlimited = false;

	public static void initPreBattle() {
		fatigue = 1;
		limit = 10;
		unlimited = false;
	}

	public static void clearPostBattle() {
		fatigue = 1;
		limit = 10;
		unlimited = false;
	}

	public static int getFatigue() {
		return fatigue;
	}

	public static void trigger() {
		if (AbstractDungeon.player.hasPower(FutureSightPower.POWER_ID)) {
			FutureSightPower p = (FutureSightPower) AbstractDungeon.player.getPower(FutureSightPower.POWER_ID);

			p.flash();
			MidoriHelper.addActionsToTop(new GainEnergyAction(1),
					new ReducePowerAction(AbstractDungeon.player, AbstractDungeon.player,
							p, 1));
		}
		else {
			MidoriHelper.addToTop(new FatigueDamageAction(fatigue));
			increment();
		}
	}

	public static void increment() {
		if (fatigue < limit || unlimited)
			fatigue++;
	}

	public static void decrease(int amount) {
		fatigue -= amount;
		if (fatigue < 1)
			fatigue = 1;
	}

	public static void increaseLimit(int amount) {
		limit += amount;
	}

	public static void decreaseLimit(int amount) {
		limit -= amount;
		if (limit < 1)
			limit = 1;

		if (fatigue > limit)
			fatigue = limit;
	}

	public static void unlimit() {
		unlimited = true;
	}

	public static void limit() {
		unlimited = false;
		if (fatigue > limit)
			fatigue = limit;
	}

	public static String getTip() {
		return String.format(uiStrings.TEXT[0], fatigue);
	}
}
