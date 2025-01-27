package me.antileaf.midori.cards.midori;

import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.ExhaustSpecificCardAction;
import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import me.antileaf.midori.cards.AbstractMidoriCard;
import me.antileaf.midori.cards.interfaces.OnPaintedCard;
import me.antileaf.midori.hue.Hue;
import me.antileaf.midori.patches.enums.CardColorEnum;
import me.antileaf.midori.powers.unique.FlightPower;
import me.antileaf.midori.utils.MidoriHelper;

public class PaperPlane extends AbstractMidoriCard implements OnPaintedCard {
	public static final String SIMPLE_NAME = PaperPlane.class.getSimpleName();
	public static final String ID = MidoriHelper.makeID(SIMPLE_NAME);
	private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);

	private static final int COST = -2;
	private static final int MAGIC = 3;

	private int counter;

	public PaperPlane() {
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

		this.magicNumber = this.baseMagicNumber = MAGIC;
		this.counter = MAGIC;
	}

	@Override
	public boolean canUse(AbstractPlayer p, AbstractMonster m) {
		return false;
	}

	@Override
	public void applyPowers() {
		super.applyPowers();

		this.rawDescription = this.upgraded ? cardStrings.UPGRADE_DESCRIPTION : cardStrings.DESCRIPTION;
		if (this.counter < this.magicNumber)
			this.rawDescription += " NL " + String.format(cardStrings.EXTENDED_DESCRIPTION[0], this.counter);
		this.initializeDescription();
	}

	@Override
	public void onMoveToDiscard() {
		this.rawDescription = this.upgraded ? cardStrings.UPGRADE_DESCRIPTION : cardStrings.DESCRIPTION;
		this.initializeDescription();
	}

	@Override
	public void triggerOnExhaust() {
		this.counter = this.magicNumber;
	}

	@Override
	public void onPainted(Hue hue) {
		this.counter--;

		if (this.counter <= 0) {
			if (AbstractDungeon.player.hand.contains(this))
				this.addToTop(new ExhaustSpecificCardAction(this, AbstractDungeon.player.hand));
			else if (AbstractDungeon.player.drawPile.contains(this))
				this.addToTop(new ExhaustSpecificCardAction(this, AbstractDungeon.player.drawPile));
			else if (AbstractDungeon.player.discardPile.contains(this))
				this.addToTop(new ExhaustSpecificCardAction(this, AbstractDungeon.player.discardPile));

			this.addToBot(new ApplyPowerAction(AbstractDungeon.player, AbstractDungeon.player,
					new FlightPower(1)));
		}
		else
			this.applyPowers();
	}

	@Override
	public void use(AbstractPlayer p, AbstractMonster m) {}

	@Override
	public AbstractCard makeCopy() {
		return new PaperPlane();
	}

	@Override
	public AbstractCard makeStatEquivalentCopy() {
		PaperPlane copy = (PaperPlane) super.makeStatEquivalentCopy();
		copy.counter = this.counter;
		return copy;
	}

	@Override
	public void upgrade() {
		if (!this.upgraded) {
			this.upgradeName();

			this.retain = this.selfRetain = true;
			this.rawDescription = cardStrings.UPGRADE_DESCRIPTION;

			this.initializeDescription();
		}
	}
}
