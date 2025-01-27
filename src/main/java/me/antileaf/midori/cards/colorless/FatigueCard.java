package me.antileaf.midori.cards.colorless;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.vfx.ThoughtBubble;
import me.antileaf.midori.cards.AbstractMidoriCard;
import me.antileaf.midori.utils.MidoriHelper;

public class FatigueCard extends AbstractMidoriCard {
	public static final String SIMPLE_NAME = FatigueCard.class.getSimpleName();
	public static final String ID = MidoriHelper.makeID(SIMPLE_NAME);
	private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);

	private static final int COST = -2;

	private boolean aoe = false;

	public FatigueCard(int damage, boolean aoe) {
		super(
				ID,
				cardStrings.NAME,
				MidoriHelper.getCardImgFilePath(SIMPLE_NAME),
				COST,
				cardStrings.DESCRIPTION,
				CardType.SKILL,
				CardColor.COLORLESS,
				CardRarity.SPECIAL,
				CardTarget.ALL_ENEMY
		);

		this.magicNumber = this.baseMagicNumber = damage;
		this.aoe = aoe;

		this.initializeDescription();
	}

	public FatigueCard() {
		this(-1, false);
	}

	@Override
	public void initializeDescription() {
		if (this.magicNumber == -1)
			this.rawDescription = cardStrings.DESCRIPTION;
		else
			this.rawDescription = cardStrings.EXTENDED_DESCRIPTION[this.aoe ? 1 : 0];

		super.initializeDescription();
	}

	@Override
	public void use(AbstractPlayer p, AbstractMonster m) {
		AbstractDungeon.effectsQueue.add(new ThoughtBubble(p.dialogX, p.dialogY, 3.0F,
				cardStrings.EXTENDED_DESCRIPTION[2], true));
	}

	@Override
	public AbstractCard makeCopy() {
		return new FatigueCard();
	}

	@Override
	public boolean canUpgrade() {
		return false;
	}

	@Override
	public void upgrade() {
	}
}
