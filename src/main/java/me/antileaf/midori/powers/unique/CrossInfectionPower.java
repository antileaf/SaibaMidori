package me.antileaf.midori.powers.unique;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.PoisonPower;
import me.antileaf.midori.hue.Hue;
import me.antileaf.midori.hue.HueManager;
import me.antileaf.midori.powers.AbstractMidoriPower;
import me.antileaf.midori.powers.interfaces.BetterAtDamageFinalGivePower;
import me.antileaf.midori.utils.MidoriHelper;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class CrossInfectionPower extends AbstractMidoriPower implements BetterAtDamageFinalGivePower {
	private static final Logger logger = LogManager.getLogger(CrossInfectionPower.class.getName());

	public static final String SIMPLE_NAME = CrossInfectionPower.class.getSimpleName();
	public static final String POWER_ID = MidoriHelper.makeID(SIMPLE_NAME);
	private static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);

	public CrossInfectionPower(int amount) {
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
	public float betterAtDamageFinalGive(float damage, DamageInfo.DamageType type,
										 AbstractCard card, AbstractMonster m) {
		logger.info("m = {}", m);

		if (type != DamageInfo.DamageType.NORMAL || m == null)
			return damage;

		// 卡牌为红色或绿色，且敌人拥有中毒
		if (HueManager.hasHue(card, Hue.LAVA) || HueManager.hasHue(card, Hue.MINT)) {
			if (m.hasPower(PoisonPower.POWER_ID) && m.getPower(PoisonPower.POWER_ID).amount > 0) {
				return damage * (1 + this.amount / 100.0F);
			}
		}

		return damage;
	}
}
