package me.antileaf.midori.powers.unique;

import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.MakeTempCardInHandAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.CardLibrary;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.ArtifactPower;
import com.megacrit.cardcrawl.powers.GainStrengthPower;
import com.megacrit.cardcrawl.powers.StrengthPower;
import me.antileaf.midori.actions.utils.AnonymousAction;
import me.antileaf.midori.hue.Hue;
import me.antileaf.midori.hue.HueManager;
import me.antileaf.midori.powers.AbstractMidoriPower;
import me.antileaf.midori.powers.interfaces.OnCardRemovedHuePower;
import me.antileaf.midori.utils.MidoriHelper;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;

public class SealOfCrowsPower extends AbstractMidoriPower {
	private static final Logger logger = LogManager.getLogger(SealOfCrowsPower.class.getName());

	public static final String SIMPLE_NAME = SealOfCrowsPower.class.getSimpleName();
	public static final String POWER_ID = MidoriHelper.makeID(SIMPLE_NAME);
	private static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);

	private boolean upgraded = false;

	public SealOfCrowsPower(int amount, boolean upgraded) {
		this.upgraded = upgraded;

		this.name = powerStrings.NAME + (this.upgraded ? "+" : "");
		this.ID = POWER_ID + (this.upgraded ? "_Upgraded" : "");
		this.owner = AbstractDungeon.player;
		this.amount = amount;

		this.type = PowerType.BUFF;
		this.updateDescription();
		this.initializeImage(null);
	}

	@Override
	public void updateDescription() {
		this.description = String.format(powerStrings.DESCRIPTIONS[this.upgraded ? 1 : 0], this.amount);
	}

	@Override
	public void onCardDraw(AbstractCard card) {
		if (HueManager.hasHue(card, Hue.INK) || (this.upgraded && !HueManager.hasAnyHue(card))) {
			for (AbstractMonster m : AbstractDungeon.getMonsters().monsters)
				if (!m.isDeadOrEscaped())
					this.addToBot(new AnonymousAction(() -> {
						MidoriHelper.addActionToBuffer(new ApplyPowerAction(m,
								AbstractDungeon.player,
								new StrengthPower(m, -this.amount), -this.amount, true));

						if (!m.hasPower(ArtifactPower.POWER_ID))
							MidoriHelper.addActionToBuffer(new ApplyPowerAction(m,
									AbstractDungeon.player,
									new GainStrengthPower(m, this.amount), this.amount, true));

						MidoriHelper.commitBuffer();
					}));
		}
	}
}
