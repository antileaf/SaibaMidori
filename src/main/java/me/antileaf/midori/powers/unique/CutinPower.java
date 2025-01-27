package me.antileaf.midori.powers.unique;

import com.megacrit.cardcrawl.actions.common.ReducePowerAction;
import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.PowerStrings;
import me.antileaf.midori.hue.Hue;
import me.antileaf.midori.hue.HueManager;
import me.antileaf.midori.powers.AbstractMidoriPower;
import me.antileaf.midori.utils.MidoriHelper;

public class CutinPower extends AbstractMidoriPower {
	public static final String SIMPLE_NAME = CutinPower.class.getSimpleName();
	public static final String POWER_ID = MidoriHelper.makeID(SIMPLE_NAME);
	private static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);

	private final Hue hue;
	private final boolean same;

	public CutinPower(Hue hue, boolean same) {
		this.name = powerStrings.NAME;
		this.hue = hue;
		this.same = same;
		this.amount = 1;
		this.ID = POWER_ID + "_" + (hue == null ? "null" : hue.name()) + (same ? "_same" : "_diff");
		this.owner = AbstractDungeon.player;

		this.type = PowerType.DEBUFF;
		this.updateDescription();
		this.initializeImage(null);
	}

	@Override
	public void updateDescription() {
		this.description = String.format(
				powerStrings.DESCRIPTIONS[this.same ? 0 : 1],
				this.hue == null ? Hue.getNoneName() : this.hue.getName());
	}

	@Override
	public boolean canPlayCard(AbstractCard card) {
		boolean ret;
		if (this.hue != null)
			ret = this.same == HueManager.hasHue(card, this.hue);
		else
			ret = this.same == !HueManager.hasAnyHue(card);

		if (!ret)
			card.cantUseMessage = String.format(powerStrings.DESCRIPTIONS[this.same ? 2 : 3],
					this.hue == null ? Hue.getNoneName() : this.hue.getName());

		return ret;
	}

	@Override
	public void atEndOfTurn(boolean isPlayer) {
		if (isPlayer)
			this.addToBot(new RemoveSpecificPowerAction(this.owner, this.owner, this));
	}
}
