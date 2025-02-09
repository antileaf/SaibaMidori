package me.antileaf.midori.cards.deprecated.colorless;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import me.antileaf.midori.cards.AbstractMidoriCard;
import me.antileaf.midori.utils.MidoriHelper;

public class GoWithGreen extends AbstractMidoriCard {
	public static final String SIMPLE_NAME = GoWithGreen.class.getSimpleName();
	public static final String ID = MidoriHelper.makeID(SIMPLE_NAME);
	private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);

	private static final int COST = -2;
//	private static final int MAGIC = 1;
//	private static final int UPGRADE_PLUS_MAGIC = 1;

	private int count = -1;

	public GoWithGreen() {
		super(
				ID,
				cardStrings.NAME,
				MidoriHelper.getCardImgFilePath(SIMPLE_NAME),
				COST,
				cardStrings.DESCRIPTION,
				CardType.POWER,
				CardColor.COLORLESS,
				CardRarity.SPECIAL,
				CardTarget.NONE
		);

		this.initializeDescription();

//		this.magicNumber = this.baseMagicNumber = MAGIC;
	}

	public GoWithGreen(int count) {
		this();
		this.count = count;
		this.initializeDescription();
	}

	@Override
	public void initializeDescription() {
		if (this.count == -1)
			this.rawDescription = this.upgraded ? cardStrings.UPGRADE_DESCRIPTION : cardStrings.DESCRIPTION;
		else
			this.rawDescription = String.format(cardStrings.EXTENDED_DESCRIPTION[0], this.count);

		super.initializeDescription();
	}

	@Override
	public void use(AbstractPlayer p, AbstractMonster m) {}

	@Override
	public AbstractCard makeCopy() {
		return new GoWithGreen();
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
