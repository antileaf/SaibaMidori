package me.antileaf.midori.powers.unique;

import com.evacipated.cardcrawl.mod.stslib.patches.NeutralPowertypePatch;
import com.megacrit.cardcrawl.actions.common.GainEnergyAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.PowerStrings;
import me.antileaf.midori.actions.utils.AnonymousAction;
import me.antileaf.midori.fatigue.FatigueManager;
import me.antileaf.midori.hue.Hue;
import me.antileaf.midori.hue.HueManager;
import me.antileaf.midori.powers.AbstractMidoriPower;
import me.antileaf.midori.utils.MidoriHelper;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.HashSet;
import java.util.Set;

public class PaintingsPower extends AbstractMidoriPower {
	private static final Logger logger = LogManager.getLogger(PaintingsPower.class.getName());

	public static final String SIMPLE_NAME = PaintingsPower.class.getSimpleName();
	public static final String POWER_ID = MidoriHelper.makeID(SIMPLE_NAME);
	private static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);

	public PaintingsPower(int amount) {
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
		if (this.amount == 1)
			this.description = powerStrings.DESCRIPTIONS[0];
		else
			this.description = String.format(powerStrings.DESCRIPTIONS[1], this.amount);
	}

	@Override
	public void atStartOfTurnPostDraw() {
		this.addToBot(new AnonymousAction(() -> {
			Set<Hue> set = new HashSet<>();

			for (AbstractCard c : AbstractDungeon.player.hand.group)
				for (Hue hue : Hue.values())
					if (HueManager.hasHue(c, hue))
						set.add(hue);

			int count = set.size();
			if (count * this.amount > 0)
				this.addToBot(new GainEnergyAction(count * this.amount));

			logger.info("count = {}, amount = {}", count, this.amount);
		}));
	}
}
