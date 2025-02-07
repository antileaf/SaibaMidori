package me.antileaf.midori.cards.midori;

import com.megacrit.cardcrawl.actions.common.DrawCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import me.antileaf.midori.actions.common.FilteredDrawCardAction;
import me.antileaf.midori.actions.utils.AnonymousAction;
import me.antileaf.midori.actions.utils.ReplaceCardAction;
import me.antileaf.midori.cards.AbstractMidoriCard;
import me.antileaf.midori.cards.colorless.Paint;
import me.antileaf.midori.hue.Hue;
import me.antileaf.midori.hue.HueManager;
import me.antileaf.midori.patches.enums.CardColorEnum;
import me.antileaf.midori.utils.MidoriHelper;

public class ArtPolish extends AbstractMidoriCard {
	public static final String SIMPLE_NAME = ArtPolish.class.getSimpleName();
	public static final String ID = MidoriHelper.makeID(SIMPLE_NAME);
	private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);

	private static final int COST = 1;

	public ArtPolish() {
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

		this.cardsToPreview = new Paint();
	}

	@Override
	public void use(AbstractPlayer p, AbstractMonster m) {
//		this.addToBot(new FilteredDrawCardAction(1,
//				c -> c.type == CardType.ATTACK && HueManager.hasHue(c, Hue.LAVA),
//				true, null));
//
//		this.addToBot(new FilteredDrawCardAction(1,
//				c -> c.type == CardType.SKILL && HueManager.hasHue(c, Hue.MINT),
//				true, null));
//
//		this.addToBot(new FilteredDrawCardAction(1,
//				c -> c.type == CardType.POWER && HueManager.hasHue(c, Hue.SKY),
//				true, null));

		this.addToBot(new FilteredDrawCardAction(Integer.MAX_VALUE,
				c -> c.type == CardType.STATUS || c.type == CardType.CURSE,
				true, new AnonymousAction(() -> {
					for (AbstractCard c : FilteredDrawCardAction.drawnCards)
						this.addToTop(new ReplaceCardAction(c,
								this.cardsToPreview.makeStatEquivalentCopy()));
		})));
	}

	@Override
	public AbstractCard makeCopy() {
		return new ArtPolish();
	}

	@Override
	public void upgrade() {
		if (!this.upgraded) {
			this.upgradeName();

//			this.isInnate = true;
			this.rawDescription = cardStrings.UPGRADE_DESCRIPTION;

			this.cardsToPreview.upgrade();

			this.initializeDescription();
		}
	}
}
