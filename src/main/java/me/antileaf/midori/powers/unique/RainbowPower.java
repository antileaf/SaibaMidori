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
import me.antileaf.midori.powers.AbstractMidoriPower;
import me.antileaf.midori.utils.MidoriHelper;

public class RainbowPower extends AbstractMidoriPower {
	public static final String SIMPLE_NAME = RainbowPower.class.getSimpleName();
	public static final String POWER_ID = MidoriHelper.makeID(SIMPLE_NAME);
	private static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);

	public RainbowPower(int amount) {
		this.name = powerStrings.NAME;
		this.ID = POWER_ID;
		this.owner = AbstractDungeon.player;
		this.amount = Math.min(amount, 1);

		this.type = PowerType.BUFF;
		this.updateDescription();
		this.initializeImage(null);
	}

	@Override
	public void stackPower(int stackAmount) {
		this.amount += stackAmount;
		if (this.amount > 1)
			this.amount = 1;
	}

	@Override
	public void updateDescription() {
		this.name = powerStrings.NAME;
		if (this.amount > 0)
			this.name += "+";

		this.description = powerStrings.DESCRIPTIONS[this.amount == 0 ? 0 : 1];
	}
}
