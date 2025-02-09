package me.antileaf.midori.powers.common;

import com.megacrit.cardcrawl.actions.common.ReducePowerAction;
import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.powers.AbstractPower.PowerType;
import me.antileaf.midori.utils.MidoriHelper;

public class LessDrawPower extends AbstractPower {
	public static final String POWER_ID = MidoriHelper.makeID(LessDrawPower.class.getSimpleName());
	private static final PowerStrings powerStrings;
	public static final String NAME;
	public static final String[] DESCRIPTIONS;

	public LessDrawPower(AbstractCreature owner, int amount) {
		this.name = NAME;
		this.ID = "Draw Reduction";
		this.owner = owner;
		this.amount = Math.min(amount, AbstractDungeon.player.gameHandSize);
		this.updateDescription();
		this.loadRegion("lessdraw");
		this.type = PowerType.DEBUFF;
	}

	public void onInitialApplication() {
		AbstractDungeon.player.gameHandSize -= this.amount;
	}

	@Override
	public void stackPower(int amount) {
		amount = Math.min(amount, AbstractDungeon.player.gameHandSize);
		if (amount > 0) {
			super.stackPower(amount);
			AbstractDungeon.player.gameHandSize -= amount;
		}
	}

	public void onRemove() {
		AbstractDungeon.player.gameHandSize += this.amount;
	}

	@Override
	public void atStartOfTurnPostDraw() {
		this.addToBot(new RemoveSpecificPowerAction(this.owner, this.owner, this));
	}

	public void updateDescription() {
		this.description = String.format(DESCRIPTIONS[0], this.amount);
	}

	static {
		powerStrings = CardCrawlGame.languagePack.getPowerStrings(LessDrawPower.POWER_ID);
		NAME = powerStrings.NAME;
		DESCRIPTIONS = powerStrings.DESCRIPTIONS;
	}
}
