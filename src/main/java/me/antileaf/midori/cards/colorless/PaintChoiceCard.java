package me.antileaf.midori.cards.colorless;

import basemod.patches.com.megacrit.cardcrawl.screens.compendium.CardLibraryScreen.NoCompendium;
import com.badlogic.gdx.Gdx;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import me.antileaf.midori.cards.AbstractMidoriCard;
import me.antileaf.midori.hue.Hue;
import me.antileaf.midori.utils.MidoriHelper;

public class PaintChoiceCard extends AbstractMidoriCard {
	public static final String SIMPLE_NAME = PaintChoiceCard.class.getSimpleName();
	public static final String ID = MidoriHelper.makeID(SIMPLE_NAME);
	private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);

	private static final int COST = -2;

	public Hue hue;

	public PaintChoiceCard() {
		super(
				ID,
				cardStrings.NAME,
				null, // MidoriHelper.getCardImgFilePath(SIMPLE_NAME),
				COST,
				cardStrings.DESCRIPTION,
				CardType.SKILL,
				CardColor.COLORLESS,
				CardRarity.SPECIAL,
				CardTarget.NONE
		);
	}

	public PaintChoiceCard(Hue hue) {
		this();

		this.hue = hue;
		this.initializeDescription();

		String f = hue == Hue.LAVA ? "Momoi":
				hue == Hue.MINT ? "Midori":
						hue == Hue.SKY ? "Arisu":
								hue == Hue.INK ? "Kei":
										"";

		String file = MidoriHelper.getCardImgFilePath(f + "Choice");
		if (Gdx.files.internal(file).exists())
			this.loadCardImage(file);
	}

	@Override
	public void initializeDescription() {
		if (this.hue == null) {
			this.name = cardStrings.NAME;
			this.rawDescription = cardStrings.DESCRIPTION;
		}
		else {
			int ordinal = this.hue.ordinal();
			this.name = cardStrings.EXTENDED_DESCRIPTION[ordinal * 2];
			this.rawDescription = cardStrings.EXTENDED_DESCRIPTION[ordinal * 2 + 1];
		}

		super.initializeDescription();
	}

	@Override
	public void use(AbstractPlayer p, AbstractMonster m) {}

	@Override
	public AbstractCard makeCopy() {
		return new PaintChoiceCard();
	}

	@Override
	public boolean canUpgrade() {
		return false;
	}

	@Override
	public void upgrade() {}
}
