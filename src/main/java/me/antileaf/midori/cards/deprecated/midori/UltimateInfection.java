package me.antileaf.midori.cards.deprecated.midori;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import me.antileaf.midori.actions.common.MoveToHandAction;
import me.antileaf.midori.cards.AbstractMidoriCard;
import me.antileaf.midori.patches.enums.CardColorEnum;
import me.antileaf.midori.patches.enums.CardTagEnum;
import me.antileaf.midori.utils.MidoriHelper;

@Deprecated
public class UltimateInfection extends AbstractMidoriCard {
	public static final String SIMPLE_NAME = UltimateInfection.class.getSimpleName();
	public static final String ID = MidoriHelper.makeID(SIMPLE_NAME);
	private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);

	private static final int COST = 1;
	private static final int UPGRADED_COST = 0;

	public UltimateInfection() {
		super(
				ID,
				cardStrings.NAME,
				null, // MidoriHelper.getCardImgFilePath(SIMPLE_NAME),
				COST,
				cardStrings.DESCRIPTION,
				CardType.SKILL,
				CardColorEnum.MIDORI_COLOR,
				CardRarity.UNCOMMON,
				CardTarget.NONE
		);

		this.exhaust = true;

		this.tags.add(CardTagEnum.MIDORI_INFECTION);
	}

	@Override
	public void use(AbstractPlayer p, AbstractMonster m) {
		this.addToBot(new MoveToHandAction(p.drawPile, p.drawPile.size(),
				c -> c.hasTag(CardTagEnum.MIDORI_INFECTION)));
		this.addToBot(new MoveToHandAction(p.discardPile, p.discardPile.size(),
				c -> c.hasTag(CardTagEnum.MIDORI_INFECTION)));
	}

	@Override
	public AbstractCard makeCopy() {
		return new UltimateInfection();
	}

	@Override
	public void upgrade() {
		if (!this.upgraded) {
			this.upgradeName();
			this.upgradeBaseCost(UPGRADED_COST);
			this.initializeDescription();
		}
	}
}
