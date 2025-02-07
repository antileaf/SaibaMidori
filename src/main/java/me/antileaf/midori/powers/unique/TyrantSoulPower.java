package me.antileaf.midori.powers.unique;

import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.defect.ChannelAction;
import com.megacrit.cardcrawl.actions.utility.UseCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.orbs.AbstractOrb;
import com.megacrit.cardcrawl.powers.ArtifactPower;
import com.megacrit.cardcrawl.powers.DexterityPower;
import com.megacrit.cardcrawl.powers.LoseDexterityPower;
import com.megacrit.cardcrawl.powers.StrengthPower;
import me.antileaf.midori.actions.utils.AnonymousAction;
import me.antileaf.midori.hue.Hue;
import me.antileaf.midori.hue.HueManager;
import me.antileaf.midori.powers.AbstractMidoriPower;
import me.antileaf.midori.utils.MidoriHelper;

public class TyrantSoulPower extends AbstractMidoriPower {
	public static final String SIMPLE_NAME = TyrantSoulPower.class.getSimpleName();
	public static final String POWER_ID = MidoriHelper.makeID(SIMPLE_NAME);
	private static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);

	public TyrantSoulPower(int amount) {
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
	public void onUseCard(AbstractCard card, UseCardAction action) {
		if (HueManager.hasHue(card, Hue.LAVA)) {
			this.flash();
			final int finalAmount = this.amount;

			this.addToBot(new ApplyPowerAction(this.owner, this.owner,
					new StrengthPower(this.owner, finalAmount)));

			this.addToBot(new AnonymousAction(() -> {
				if (!this.owner.hasPower(ArtifactPower.POWER_ID))
					this.addToTop(new ApplyPowerAction(this.owner, this.owner,
							new GainDexterityPower(finalAmount)));

				this.addToTop(new ApplyPowerAction(this.owner, this.owner,
						new DexterityPower(this.owner, -finalAmount)));
			}));
		}
	}
}
