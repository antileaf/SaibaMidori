package me.antileaf.midori.powers.unique;

import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.PoisonPower;
import me.antileaf.midori.actions.utils.AnonymousAction;
import me.antileaf.midori.hue.Hue;
import me.antileaf.midori.hue.HueManager;
import me.antileaf.midori.powers.AbstractMidoriPower;
import me.antileaf.midori.powers.interfaces.OnCardPaintedPower;
import me.antileaf.midori.utils.MidoriHelper;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class FragmentShieldPower extends AbstractMidoriPower {
	private static final Logger logger = LogManager.getLogger(FragmentShieldPower.class.getName());

	public static final String SIMPLE_NAME = FragmentShieldPower.class.getSimpleName();
	public static final String POWER_ID = MidoriHelper.makeID(SIMPLE_NAME);
	private static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);

	public FragmentShieldPower(int amount) {
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
		this.description = String.format(powerStrings.DESCRIPTIONS[0], this.amount);
	}

	@Override
	public void atEndOfTurnPreEndTurnCards(boolean isPlayer) {
		if (isPlayer) {
			int block = this.amount * (int) AbstractDungeon.player.hand.group.stream()
					.filter(c -> HueManager.hasHue(c, Hue.SKY))
					.count();

			this.addToBot(new GainBlockAction(this.owner, this.owner, block));
		}
	}
}
