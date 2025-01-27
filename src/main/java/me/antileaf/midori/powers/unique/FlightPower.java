package me.antileaf.midori.powers.unique;

import com.megacrit.cardcrawl.actions.common.ReducePowerAction;
import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.powers.AbstractPower;
import me.antileaf.midori.powers.AbstractMidoriPower;
import me.antileaf.midori.utils.MidoriHelper;

public class FlightPower extends AbstractMidoriPower {
	public static final String SIMPLE_NAME = FlightPower.class.getSimpleName();
	public static final String POWER_ID = MidoriHelper.makeID(SIMPLE_NAME);
	private static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);

	public FlightPower(int amount) {
		this.name = powerStrings.NAME;
		this.ID = POWER_ID;
		this.owner = AbstractDungeon.player;
		this.amount = amount;
		this.isTurnBased = true;

		this.type = AbstractPower.PowerType.BUFF;
		this.updateDescription();
		this.loadRegion("flight");
	}

	@Override
	public void updateDescription() {
		this.description = String.format(powerStrings.DESCRIPTIONS[0], this.amount);
	}

	@Override
	public float atDamageFinalReceive(float damage, DamageInfo.DamageType type) {
		return type == DamageInfo.DamageType.NORMAL ? damage / 2.0F : damage;
	}

	@Override
	public void atStartOfTurn() {
		if (this.amount > 1)
			this.addToBot(new ReducePowerAction(this.owner, this.owner, this, 1));
		else
			this.addToBot(new RemoveSpecificPowerAction(this.owner, this.owner, this));
	}
}
