package me.antileaf.midori.cards.deprecated.midori;

import com.megacrit.cardcrawl.actions.defect.ChannelAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.orbs.Frost;
import com.megacrit.cardcrawl.orbs.Lightning;
import me.antileaf.midori.cards.AbstractMidoriCard;
import me.antileaf.midori.patches.enums.CardColorEnum;
import me.antileaf.midori.utils.MidoriHelper;

@Deprecated
public class Balance extends AbstractMidoriCard {
	public static final String SIMPLE_NAME = Balance.class.getSimpleName();
	public static final String ID = MidoriHelper.makeID(SIMPLE_NAME);
	private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);

	private static final int COST = 1;

	public Balance() {
		super(
				ID,
				cardStrings.NAME,
				null, // MidoriHelper.getCardImgFilePath(SIMPLE_NAME),
				COST,
				cardStrings.DESCRIPTION,
				CardType.SKILL,
				CardColorEnum.MIDORI_COLOR,
				CardRarity.COMMON,
				CardTarget.NONE
		);

		this.isEthereal = true;
	}

	@Override
	public void use(AbstractPlayer p, AbstractMonster m) {
		this.addToBot(new ChannelAction(new Lightning()));
		this.addToBot(new ChannelAction(new Frost()));
	}

	@Override
	public AbstractCard makeCopy() {
		return new Balance();
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
