package me.antileaf.midori.powers.unique;

import com.evacipated.cardcrawl.mod.stslib.powers.interfaces.OnLoseBlockPower;
import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.actions.common.ReducePowerAction;
import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.powers.NoBlockPower;
import me.antileaf.midori.actions.utils.AnonymousAction;
import me.antileaf.midori.powers.AbstractMidoriPower;
import me.antileaf.midori.utils.MidoriHelper;

public class TimeOutPower extends AbstractMidoriPower implements OnLoseBlockPower {
	public static final String SIMPLE_NAME = TimeOutPower.class.getSimpleName();
	public static final String POWER_ID = MidoriHelper.makeID(SIMPLE_NAME);
	private static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);

	public TimeOutPower(int amount) {
		this.name = powerStrings.NAME;
		this.ID = POWER_ID;
		this.owner = AbstractDungeon.player;
		this.amount = amount;
		this.isTurnBased = true;

		this.type = PowerType.BUFF;
		this.updateDescription();
		this.initializeImage(null);
	}

	@Override
	public void stackPower(int stackAmount) {
		if (stackAmount > this.amount) {
			this.amount = stackAmount;
			this.fontScale = 8.0F;
		}
	}

	@Override
	public void updateDescription() {
		this.description = String.format(powerStrings.DESCRIPTIONS[0], this.amount);
	}

	@Override
	public int onLoseBlock(DamageInfo info, int damageAmount) {
		this.addToBot(new AnonymousAction(() -> {
			if (this.owner.hasPower(NoBlockPower.POWER_ID))
				return;

			if (this.owner.currentBlock < this.amount)
				this.addToTop(new GainBlockAction(this.owner, this.owner,
						this.amount - this.owner.currentBlock));
		}));

		return damageAmount;
	}

	@Override
	public void atStartOfTurn() {
		this.addToTop(new RemoveSpecificPowerAction(this.owner, this.owner, this));
	}
}
