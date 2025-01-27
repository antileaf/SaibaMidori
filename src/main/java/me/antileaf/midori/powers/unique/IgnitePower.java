package me.antileaf.midori.powers.unique;

import com.evacipated.cardcrawl.mod.stslib.patches.bothInterfaces.OnCreateCardInterface;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.ArtifactPower;
import com.megacrit.cardcrawl.powers.GainStrengthPower;
import com.megacrit.cardcrawl.powers.StrengthPower;
import me.antileaf.midori.actions.unique.IgniteAction;
import me.antileaf.midori.actions.utils.AnonymousAction;
import me.antileaf.midori.cards.colorless.Ignited;
import me.antileaf.midori.hue.Hue;
import me.antileaf.midori.hue.HueManager;
import me.antileaf.midori.powers.AbstractMidoriPower;
import me.antileaf.midori.powers.interfaces.OnCardPaintedPower;
import me.antileaf.midori.utils.MidoriHelper;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class IgnitePower extends AbstractMidoriPower implements OnCardPaintedPower, OnCreateCardInterface {
	private static final Logger logger = LogManager.getLogger(IgnitePower.class.getName());

	public static final String SIMPLE_NAME = IgnitePower.class.getSimpleName();
	public static final String POWER_ID = MidoriHelper.makeID(SIMPLE_NAME);
	private static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);

	private boolean upgraded = false;

	public IgnitePower(boolean upgraded) {
		this.upgraded = upgraded;

		this.name = powerStrings.NAME + (this.upgraded ? "+" : "");
		this.ID = POWER_ID + (this.upgraded ? "_Upgraded" : "");
		this.owner = AbstractDungeon.player;
		this.amount = -1;

		this.type = PowerType.BUFF;
		this.updateDescription();
		this.initializeImage(null);
	}

	@Override
	public void updateDescription() {
		this.description = powerStrings.DESCRIPTIONS[this.upgraded ? 1 : 0];
	}

	@Override
	public void onInitialApplication() {
		if (this.upgraded && this.owner.hasPower(POWER_ID))
			this.addToTop(new RemoveSpecificPowerAction(this.owner, this.owner, POWER_ID));

		for (CardGroup group : new CardGroup[] {
				AbstractDungeon.player.drawPile,
				AbstractDungeon.player.hand,
				AbstractDungeon.player.discardPile,
				AbstractDungeon.player.exhaustPile
		}) {
			group.group.forEach(c -> {
				if (HueManager.hasHue(c, Hue.LAVA))
					this.addToBot(new IgniteAction(c, this.upgraded));
			});
		}
	}

	@Override
	public void onCardPainted(AbstractCard card, Hue hue) {
		if (HueManager.isHue(hue, Hue.LAVA)) {
			if (card instanceof Ignited && !(this.upgraded && !card.upgraded))
				return;

			this.flash();
			this.addToBot(new IgniteAction(card, this.upgraded));
		}
	}

	@Override
	public void onCreateCard(AbstractCard card) {
		if (HueManager.hasHue(card, Hue.LAVA)) {
			if (card instanceof Ignited && !(this.upgraded && !card.upgraded))
				return;

			this.flash();
			this.addToBot(new IgniteAction(card, this.upgraded));
		}
	}
}
