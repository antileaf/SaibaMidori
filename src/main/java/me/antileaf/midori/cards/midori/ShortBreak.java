package me.antileaf.midori.cards.midori;

import com.megacrit.cardcrawl.actions.common.DrawCardAction;
import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import me.antileaf.midori.actions.utils.AnonymousAction;
import me.antileaf.midori.cards.AbstractMidoriCard;
import me.antileaf.midori.fatigue.FatigueManager;
import me.antileaf.midori.patches.enums.CardColorEnum;
import me.antileaf.midori.utils.MidoriHelper;

public class ShortBreak extends AbstractMidoriCard {
	public static final String SIMPLE_NAME = ShortBreak.class.getSimpleName();
	public static final String ID = MidoriHelper.makeID(SIMPLE_NAME);
	private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);

	private static final int COST = 1;
	private static final int BLOCK = 12;
	private static final int MAGIC = 1;
	private static final int MAGIC2 = 3;
	private static final int UPGRADE_PLUS_BLOCK = 2;
	private static final int UPGRADE_PLUS_MAGIC2 = -1;

	public ShortBreak() {
		super(
				ID,
				cardStrings.NAME,
				null, // MidoriHelper.getCardImgFilePath(SIMPLE_NAME),
				COST,
				cardStrings.DESCRIPTION,
				CardType.SKILL,
				CardColorEnum.MIDORI_COLOR,
				CardRarity.COMMON,
				CardTarget.SELF
		);

		this.block = this.baseBlock = BLOCK;
		this.magicNumber = this.baseMagicNumber = MAGIC;
		this.secondaryMagicNumber = this.baseSecondaryMagicNumber = MAGIC2;
	}

	@Override
	public void use(AbstractPlayer p, AbstractMonster m) {
		this.addToBot(new GainBlockAction(p, p, this.block));

		this.addToBot(new DrawCardAction(this.magicNumber));

		this.addToBot(new AnonymousAction(() -> {
			FatigueManager.decrease(this.secondaryMagicNumber);
		}));
	}

	@Override
	public AbstractCard makeCopy() {
		return new ShortBreak();
	}

	@Override
	public void upgrade() {
		if (!this.upgraded) {
			this.upgradeName();

			this.upgradeBlock(UPGRADE_PLUS_BLOCK);
			this.upgradeSecondaryMagicNumber(UPGRADE_PLUS_MAGIC2);

			this.initializeDescription();
		}
	}
}
