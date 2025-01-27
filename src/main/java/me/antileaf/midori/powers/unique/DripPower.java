package me.antileaf.midori.powers.unique;

import com.evacipated.cardcrawl.mod.stslib.actions.common.AutoplayCardAction;
import com.evacipated.cardcrawl.mod.stslib.powers.interfaces.NonStackablePower;
import com.evacipated.cardcrawl.mod.stslib.powers.interfaces.OnLoseBlockPower;
import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
import com.megacrit.cardcrawl.actions.utility.NewQueueCardAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.NoBlockPower;
import me.antileaf.midori.actions.utils.AnonymousAction;
import me.antileaf.midori.cards.midori.Drip;
import me.antileaf.midori.powers.AbstractMidoriPower;
import me.antileaf.midori.utils.MidoriHelper;

import java.util.Arrays;

public class DripPower extends AbstractMidoriPower implements NonStackablePower {
	public static final String SIMPLE_NAME = DripPower.class.getSimpleName();
	public static final String POWER_ID = MidoriHelper.makeID(SIMPLE_NAME);
	private static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);

	private final String cardName;
	private final AbstractMonster target;

	public DripPower(String cardName, int amount, AbstractMonster target) {
		this.name = powerStrings.NAME;
		this.ID = POWER_ID;
		this.owner = AbstractDungeon.player;
		this.cardName = cardName;
		this.amount = amount;
		this.target = target;
		this.isTurnBased = true;

		this.type = PowerType.BUFF;
		this.updateDescription();
		this.initializeImage(null);
	}

	private String yellow(String s) {
		return Arrays.stream(s.split(" "))
				.map(word -> "#y" + word)
				.reduce((a, b) -> a + " " + b)
				.orElse("");
	}

	@Override
	public void updateDescription() {
		this.description = String.format(powerStrings.DESCRIPTIONS[0],
				this.yellow(this.cardName),
				this.amount,
				this.yellow(this.target.name));
	}

	@Override
	public void atStartOfTurn() {
		this.addToBot(new RemoveSpecificPowerAction(this.owner, this.owner, this));

		this.addToBot(new AnonymousAction(() -> {
			Drip drip = new Drip();
			drip.magicNumber = drip.baseMagicNumber = this.amount;
			drip.purgeOnUse = true;

			drip.current_y = -200.0F * Settings.scale;
			AbstractDungeon.player.limbo.group.add(drip);

			drip.target_x = Settings.WIDTH / 2.0F;
			drip.target_y = Settings.HEIGHT / 2.0F;
			drip.targetAngle = 0.0F;
			drip.lighten(false);
			drip.drawScale = 0.12F;
			drip.targetDrawScale = 0.75F;

			this.addToTop(new NewQueueCardAction(drip,
					this.target.isDeadOrEscaped() ?
							AbstractDungeon.getRandomMonster() :
							this.target,
					false, true));
		}));
	}
}
