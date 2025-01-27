package me.antileaf.midori.cards.midori;

import com.evacipated.cardcrawl.mod.stslib.actions.common.SelectCardsInHandAction;
import com.megacrit.cardcrawl.actions.common.DrawCardAction;
import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.actions.common.MakeTempCardInHandAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import me.antileaf.midori.actions.hue.PaintAction;
import me.antileaf.midori.actions.hue.RemoveHueAction;
import me.antileaf.midori.cards.AbstractMidoriCard;
import me.antileaf.midori.cards.colorless.Paint;
import me.antileaf.midori.hue.HueManager;
import me.antileaf.midori.patches.enums.CardColorEnum;
import me.antileaf.midori.utils.MidoriHelper;

public class ColorExtraction extends AbstractMidoriCard {
	public static final String SIMPLE_NAME = ColorExtraction.class.getSimpleName();
	public static final String ID = MidoriHelper.makeID(SIMPLE_NAME);
	private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);

	private static final int COST = 1;
	private static final int MAGIC = 2;
	private static final int UPGRADE_PLUS_MAGIC = 1;

	public ColorExtraction() {
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

		this.magicNumber = this.baseMagicNumber = MAGIC;

		this.cardsToPreview = new Paint();
	}

	@Override
	public void use(AbstractPlayer p, AbstractMonster m) {
		this.addToBot(new SelectCardsInHandAction(
				this.magicNumber,
				cardStrings.EXTENDED_DESCRIPTION[0],
				true,
				true,
				c -> HueManager.hasAnyHue(c) && !HueManager.hasAllHues(c),
				(cards) -> {
					for (AbstractCard card : cards)
						MidoriHelper.addActionToBuffer(new RemoveHueAction(card));

					MidoriHelper.addActionToBuffer(new MakeTempCardInHandAction(
							new Paint(), cards.size(), true));

					MidoriHelper.commitBuffer();
				}
		));
	}

	@Override
	public AbstractCard makeCopy() {
		return new ColorExtraction();
	}

	@Override
	public void upgrade() {
		if (!this.upgraded) {
			this.upgradeName();
			this.upgradeMagicNumber(UPGRADE_PLUS_MAGIC);
			this.initializeDescription();
		}
	}
}
