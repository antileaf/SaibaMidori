package me.antileaf.midori.cards.midori;

import com.megacrit.cardcrawl.actions.animations.VFXAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.vfx.RainbowCardEffect;
import me.antileaf.midori.cards.AbstractMidoriCard;
import me.antileaf.midori.hue.Hue;
import me.antileaf.midori.patches.enums.CardColorEnum;
import me.antileaf.midori.powers.unique.FirePlumesHeartPower;
import me.antileaf.midori.powers.unique.RainbowPower;
import me.antileaf.midori.utils.MidoriHelper;

public class Rainbow extends AbstractMidoriCard {
	public static final String SIMPLE_NAME = Rainbow.class.getSimpleName();
	public static final String ID = MidoriHelper.makeID(SIMPLE_NAME);
	private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);

	private static final int COST = 2;

	public Rainbow() {
		super(
				ID,
				cardStrings.NAME,
				null, // MidoriHelper.getCardImgFilePath(SIMPLE_NAME),
				COST,
				cardStrings.DESCRIPTION,
				CardType.POWER,
				CardColorEnum.MIDORI_COLOR,
				CardRarity.RARE,
				CardTarget.SELF
		);
	}

	@Override
	public void use(AbstractPlayer p, AbstractMonster m) {
		this.addToBot(new VFXAction(new RainbowCardEffect()));

		if (AbstractDungeon.player.hasPower(RainbowPower.POWER_ID)) {
			int now = AbstractDungeon.player.getPower(RainbowPower.POWER_ID).amount;
			if (!(now == 0 && this.upgraded))
				return;
		}

		this.addToBot(new ApplyPowerAction(p, p,
				new RainbowPower(this.upgraded ? 1 : 0)));
	}

	@Override
	public AbstractCard makeCopy() {
		return new Rainbow();
	}

	@Override
	public void upgrade() {
		if (!this.upgraded) {
			this.upgradeName();
			this.rawDescription = cardStrings.UPGRADE_DESCRIPTION;
			this.initializeDescription();
		}
	}
}
