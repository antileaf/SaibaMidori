package me.antileaf.midori.cards.midori;

import com.megacrit.cardcrawl.actions.common.DrawCardAction;
import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import me.antileaf.midori.cards.AbstractMidoriCard;
import me.antileaf.midori.patches.enums.CardColorEnum;
import me.antileaf.midori.utils.MidoriHelper;

public class Moisturize extends AbstractMidoriCard {
	public static final String SIMPLE_NAME = Moisturize.class.getSimpleName();
	public static final String ID = MidoriHelper.makeID(SIMPLE_NAME);
	private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);

	private static final int COST = 2;
	private static final int MAGIC = 4;
	private static final int MAGIC2 = 2;
	private static final int UPGRADE_PLUS_MAGIC = 2;
	private static final int UPGRADE_PLUS_MAGIC2 = 1;

	public Moisturize() {
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

		this.magicNumber = this.baseMagicNumber = MAGIC;
		this.secondaryMagicNumber = this.baseSecondaryMagicNumber = MAGIC2;
	}

	@Override
	public void triggerOnGlowCheck() {
		if (AbstractDungeon.player.drawPile.size() < this.magicNumber)
			this.glowColor = AbstractCard.GOLD_BORDER_GLOW_COLOR.cpy();
		else
			this.glowColor = AbstractCard.BLUE_BORDER_GLOW_COLOR.cpy();
	}

	@Override
	public void use(AbstractPlayer p, AbstractMonster m) {
		this.addToBot(new DrawCardAction(this.magicNumber));

		if (p.drawPile.size() < this.magicNumber)
			this.addToBot(new DrawCardAction(this.secondaryMagicNumber));
	}

	@Override
	public AbstractCard makeCopy() {
		return new Moisturize();
	}

	@Override
	public void upgrade() {
		if (!this.upgraded) {
			this.upgradeName();
			this.upgradeMagicNumber(UPGRADE_PLUS_MAGIC);
			this.upgradeSecondaryMagicNumber(UPGRADE_PLUS_MAGIC2);
			this.initializeDescription();
		}
	}
}
