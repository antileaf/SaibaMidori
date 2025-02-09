package me.antileaf.midori.cards.deprecated.midori;

import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import me.antileaf.midori.cards.AbstractMidoriCard;
import me.antileaf.midori.hue.Hue;
import me.antileaf.midori.patches.enums.CardColorEnum;
import me.antileaf.midori.powers.unique.MechanicalMindPower;
import me.antileaf.midori.utils.MidoriHelper;

@Deprecated
public class MechanicalMind extends AbstractMidoriCard {
	public static final String SIMPLE_NAME = MechanicalMind.class.getSimpleName();
	public static final String ID = MidoriHelper.makeID(SIMPLE_NAME);
	private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);

	private static final int COST = 3;

	public MechanicalMind() {
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

		this.isEthereal = true;

		this.fixedHue = Hue.SKY;
	}

	@Override
	public void use(AbstractPlayer p, AbstractMonster m) {
		this.addToBot(new ApplyPowerAction(p, p, new MechanicalMindPower(1)));
	}

	@Override
	public AbstractCard makeCopy() {
		return new MechanicalMind();
	}

	@Override
	public void upgrade() {
		if (!this.upgraded) {
			this.upgradeName();

			this.isEthereal = false;
			this.rawDescription = cardStrings.UPGRADE_DESCRIPTION;

			this.initializeDescription();
		}
	}
}
