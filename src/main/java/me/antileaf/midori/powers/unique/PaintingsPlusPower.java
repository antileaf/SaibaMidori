package me.antileaf.midori.powers.unique;

import com.megacrit.cardcrawl.actions.common.GainEnergyAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.PowerStrings;
import me.antileaf.midori.actions.utils.AnonymousAction;
import me.antileaf.midori.hue.Hue;
import me.antileaf.midori.hue.HueManager;
import me.antileaf.midori.powers.AbstractMidoriPower;
import me.antileaf.midori.utils.MidoriHelper;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.HashSet;
import java.util.Set;

public class PaintingsPlusPower extends AbstractMidoriPower {
	private static final Logger logger = LogManager.getLogger(PaintingsPlusPower.class.getName());

	public static final String SIMPLE_NAME = PaintingsPlusPower.class.getSimpleName();
	public static final String POWER_ID = MidoriHelper.makeID(SIMPLE_NAME);
	private static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);

	public PaintingsPlusPower() {
		this.name = powerStrings.NAME;
		this.ID = POWER_ID;
		this.owner = AbstractDungeon.player;
		this.amount = -1;

		this.type = PowerType.BUFF;
		this.updateDescription();
		this.initializeImage(null);
	}

	@Override
	public void updateDescription() {
		this.description = powerStrings.DESCRIPTIONS[0];
	}

	@Override
	public void atEndOfTurn(boolean isPlayer) {
		if (isPlayer) {
			boolean[] tbl = new boolean[Hue.values().length];

			for (AbstractCard c : AbstractDungeon.player.hand.group) {
				Hue hue = HueManager.getHue(c);
				if (hue != null)
					tbl[hue.ordinal()] = true;
			}

			int count = 0;
			for (boolean b : tbl)
				count += b ? 1 : 0;

			if (count >= 7) {
				this.flash();
				this.addToBot(new AnonymousAction(() -> {
					AbstractDungeon.getCurrRoom().endBattle();
				}));
			}
		}
	}
}
