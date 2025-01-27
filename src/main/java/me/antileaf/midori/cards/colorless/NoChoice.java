package me.antileaf.midori.cards.colorless;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import me.antileaf.midori.cards.AbstractMidoriCard;
import me.antileaf.midori.utils.MidoriHelper;

public class NoChoice extends AbstractMidoriCard {
	public static final String SIMPLE_NAME = NoChoice.class.getSimpleName();
	public static final String ID = MidoriHelper.makeID(SIMPLE_NAME);
	private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);

	private static final int COST = -2;
	private static final int MAGIC = 2;

	public NoChoice() {
		super(
				ID,
				cardStrings.NAME,
				MidoriHelper.getCardImgFilePath(SIMPLE_NAME),
				COST,
				cardStrings.DESCRIPTION,
				CardType.SKILL,
				CardColor.COLORLESS,
				CardRarity.SPECIAL,
				CardTarget.NONE
		);

		this.magicNumber = this.baseMagicNumber = MAGIC;
	}

	@Override
	public void use(AbstractPlayer p, AbstractMonster m) {}

	@Override
	public AbstractCard makeCopy() {
		return new NoChoice();
	}

	@Override
	public boolean canUpgrade() {
		return false;
	}

	@Override
	public void upgrade() {}
}
