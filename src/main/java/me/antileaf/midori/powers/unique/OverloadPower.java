package me.antileaf.midori.powers.unique;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.actions.common.DamageRandomEnemyAction;
import com.megacrit.cardcrawl.actions.utility.UseCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import me.antileaf.midori.actions.utils.AnonymousAction;
import me.antileaf.midori.fatigue.FatigueManager;
import me.antileaf.midori.powers.AbstractMidoriPower;
import me.antileaf.midori.utils.MidoriHelper;

public class OverloadPower extends AbstractMidoriPower {
	public static final String SIMPLE_NAME = OverloadPower.class.getSimpleName();
	public static final String POWER_ID = MidoriHelper.makeID(SIMPLE_NAME);
	private static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);

	public OverloadPower(int amount) {
		this.name = powerStrings.NAME;
		this.ID = POWER_ID;
		this.owner = AbstractDungeon.player;
		this.amount = amount;

		this.type = PowerType.BUFF;
		this.updateDescription();
		this.initializeImage(null);
	}

	@Override
	public void stackPower(int stackAmount) {
		super.stackPower(stackAmount);

		this.addToTop(new AnonymousAction(() -> FatigueManager.increaseLimit(stackAmount)));
	}

	@Override
	public void updateDescription() {
		this.description = String.format(powerStrings.DESCRIPTIONS[0], this.amount);
	}

	@Override
	public void onInitialApplication() {
		this.addToTop(new AnonymousAction(() -> FatigueManager.increaseLimit(this.amount)));
	}

	@Override
	public void onRemove() {
		this.addToTop(new AnonymousAction(() -> FatigueManager.decreaseLimit(this.amount)));
	}
}
