package me.antileaf.midori.powers.unique;

import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.powers.DexterityPower;
import me.antileaf.midori.powers.AbstractMidoriPower;
import me.antileaf.midori.utils.MidoriHelper;

public class GainDexterityPower extends AbstractMidoriPower {
	public static final String SIMPLE_NAME = GainDexterityPower.class.getSimpleName();
	public static final String POWER_ID = MidoriHelper.makeID(SIMPLE_NAME);
	private static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);

	public GainDexterityPower(int amount) {
		this.name = powerStrings.NAME;
		this.ID = POWER_ID;
		this.owner = AbstractDungeon.player;
		this.amount = amount;

		this.type = PowerType.BUFF;
		this.updateDescription();
		this.initializeImage(null);
	}

	@Override
	public void updateDescription() {
		this.description = String.format(powerStrings.DESCRIPTIONS[0], this.amount, this.amount);
	}

	@Override
	public void atEndOfTurn(boolean isPlayer) {
		this.flash();
		this.addToBot(new ApplyPowerAction(this.owner, this.owner, new DexterityPower(this.owner, this.amount)));
		this.addToBot(new RemoveSpecificPowerAction(this.owner, this.owner, this));
	}
}
