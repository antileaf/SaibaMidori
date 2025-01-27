package me.antileaf.midori.powers.unique;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.DamageAllEnemiesAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.ui.panels.EnergyPanel;
import me.antileaf.midori.actions.utils.AnonymousAction;
import me.antileaf.midori.hue.Hue;
import me.antileaf.midori.hue.HueManager;
import me.antileaf.midori.powers.AbstractMidoriPower;
import me.antileaf.midori.utils.MidoriHelper;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class HiddenMeaningPower extends AbstractMidoriPower {
	private static final Logger logger = LogManager.getLogger(HiddenMeaningPower.class.getName());

	public static final String SIMPLE_NAME = HiddenMeaningPower.class.getSimpleName();
	public static final String POWER_ID = MidoriHelper.makeID(SIMPLE_NAME);
	private static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);

	public HiddenMeaningPower(int amount) {
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
	public void atEndOfTurn(boolean isPlayer) {
		if (isPlayer) {
			this.addToBot(new AnonymousAction(() -> {
				if (EnergyPanel.getCurrentEnergy() > 0) {
					this.flash();

					this.addToBot(new DamageAllEnemiesAction(
							AbstractDungeon.player,
							this.amount,
							DamageInfo.DamageType.THORNS,
							AbstractGameAction.AttackEffect.BLUNT_LIGHT
					));
				}
			}));
		}
	}
}
