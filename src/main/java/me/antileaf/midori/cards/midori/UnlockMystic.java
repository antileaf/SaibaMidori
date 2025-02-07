package me.antileaf.midori.cards.midori;

import com.megacrit.cardcrawl.actions.common.DrawCardAction;
import com.megacrit.cardcrawl.actions.common.GainEnergyAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.relics.ChemicalX;
import me.antileaf.midori.actions.utils.AnonymousAction;
import me.antileaf.midori.cards.AbstractMidoriCard;
import me.antileaf.midori.patches.enums.CardColorEnum;
import me.antileaf.midori.utils.MidoriHelper;

public class UnlockMystic extends AbstractMidoriCard {
	public static final String SIMPLE_NAME = UnlockMystic.class.getSimpleName();
	public static final String ID = MidoriHelper.makeID(SIMPLE_NAME);
	private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);

	private static final int COST = -1;

	public UnlockMystic() {
		super(
				ID,
				cardStrings.NAME,
				null, // MidoriHelper.getCardImgFilePath(SIMPLE_NAME),
				COST,
				cardStrings.DESCRIPTION,
				CardType.SKILL,
				CardColorEnum.MIDORI_COLOR,
				CardRarity.RARE,
				CardTarget.NONE
		);

		this.exhaust = true;
	}

	@Override
	public void use(AbstractPlayer p, AbstractMonster m) {
		this.addToBot(new AnonymousAction(() -> {
			int amount = this.energyOnUse + (this.upgraded ? 1 : 0);

			if (p.hasRelic(ChemicalX.ID)) {
				amount += ChemicalX.BOOST;
				p.getRelic(ChemicalX.ID).flash();
			}

			if (amount > 0) {
				if (!this.freeToPlayOnce)
					p.energy.use(this.energyOnUse);

				this.addToBot(new DrawCardAction(amount, new AnonymousAction(() -> {
					this.addToTop(new GainEnergyAction(DrawCardAction.drawnCards.stream()
							.mapToInt(card -> card.costForTurn)
							.filter(cost -> cost > 0)
							.sum()));
				})));
			}
		}));
	}

	@Override
	public AbstractCard makeCopy() {
		return new UnlockMystic();
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
