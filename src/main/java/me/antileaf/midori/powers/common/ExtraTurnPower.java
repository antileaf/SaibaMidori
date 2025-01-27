package me.antileaf.midori.powers.common;

import com.megacrit.cardcrawl.actions.common.ReducePowerAction;
import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
import com.megacrit.cardcrawl.actions.watcher.SkipEnemiesTurnAction;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.powers.AbstractPower;
import me.antileaf.midori.powers.AbstractMidoriPower;
import me.antileaf.midori.utils.MidoriHelper;

public class ExtraTurnPower extends AbstractMidoriPower {
	public static final String SIMPLE_NAME = ExtraTurnPower.class.getSimpleName();
	public static final String POWER_ID = MidoriHelper.makeID(SIMPLE_NAME);
	private static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
	
	public ExtraTurnPower(int amount) {
		this.name = powerStrings.NAME;
		this.ID = POWER_ID;
		this.owner = AbstractDungeon.player;
		this.amount = amount;
		
		this.type = AbstractPower.PowerType.BUFF;
		this.updateDescription();
//		this.initializeImage(SIMPLE_NAME);
		this.loadRegion("time");
	}
	
	@Override
	public void playApplyPowerSfx() {
		CardCrawlGame.sound.play("POWER_TIME_WARP", 0.05F);
	}
	
//	@Override
//	public void onInitialApplication() {
//		AbstractDungeon.overlayMenu.endTurnButton.updateText(
//				CardCrawlGame.languagePack.getUIString("ZhuangZhou:ExtraTurnButton").TEXT[0]);
//	}
	
	@Override
	public void stackPower(int stackAmount) {
		this.fontScale = 8.0F;
		this.amount += stackAmount;
	}
	
	@Override
	public void updateDescription() {
		if (this.amount <= 1)
			this.description = powerStrings.DESCRIPTIONS[0];
		else
			this.description = String.format(powerStrings.DESCRIPTIONS[1], this.amount);
	}
	
	@Override
	public void atEndOfTurn(boolean isPlayer) {
		if (isPlayer) {
			this.flash();
			this.addToBot(new SkipEnemiesTurnAction());
//			AbstractDungeon.topLevelEffectsQueue.add(new TimeWarpTurnEndEffect());
			
			if (this.amount > 1)
				this.addToBot(new ReducePowerAction(this.owner, this.owner, this, 1));
			else
				this.addToBot(new RemoveSpecificPowerAction(this.owner, this.owner, this));
		}
	}
}
