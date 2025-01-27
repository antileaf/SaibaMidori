package me.antileaf.midori.powers.unique;

import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.PowerStrings;
import me.antileaf.midori.hue.Hue;
import me.antileaf.midori.hue.HueManager;
import me.antileaf.midori.powers.AbstractMidoriPower;
import me.antileaf.midori.utils.MidoriHelper;

public class CutinNextTurnPower extends AbstractMidoriPower {
	public static final String SIMPLE_NAME = CutinNextTurnPower.class.getSimpleName();
	public static final String POWER_ID = MidoriHelper.makeID(SIMPLE_NAME);
	private static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);

	private final Hue hue;
	private final boolean same;

	public CutinNextTurnPower(Hue hue, boolean same) {
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
	public void atStartOfTurn() {
		this.addToBot(new RemoveSpecificPowerAction(this.owner, this.owner, this));
		this.addToBot(new ApplyPowerAction(this.owner, this.owner,
				new CutinPower(this.hue, this.same)));
	}
}
