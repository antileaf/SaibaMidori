package me.antileaf.midori.powers.unique;

import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.PoisonPower;
import me.antileaf.midori.actions.utils.AnonymousAction;
import me.antileaf.midori.cards.AbstractMidoriCard;
import me.antileaf.midori.hue.Hue;
import me.antileaf.midori.hue.HueManager;
import me.antileaf.midori.powers.AbstractMidoriPower;
import me.antileaf.midori.powers.interfaces.BetterAtDamageFinalGivePower;
import me.antileaf.midori.powers.interfaces.OnCardPaintedPower;
import me.antileaf.midori.utils.MidoriHelper;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class PoisonousPaintPower extends AbstractMidoriPower implements OnCardPaintedPower {
	private static final Logger logger = LogManager.getLogger(PoisonousPaintPower.class.getName());

	public static final String SIMPLE_NAME = PoisonousPaintPower.class.getSimpleName();
	public static final String POWER_ID = MidoriHelper.makeID(SIMPLE_NAME);
	private static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);

	public PoisonousPaintPower(int amount) {
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
	public void onCardPainted(AbstractCard card, Hue hue) {
		if (hue != null && !HueManager.hasAllHues(card)) {
			this.flash();

			this.addToBot(new AnonymousAction(() -> {
				AbstractMonster m = AbstractDungeon.getMonsters().getRandomMonster(true);
				if (m != null)
					this.addToTop(new ApplyPowerAction(m, AbstractDungeon.player,
							new PoisonPower(m, AbstractDungeon.player, this.amount),
							this.amount, true));
			}));
		}
	}
}
