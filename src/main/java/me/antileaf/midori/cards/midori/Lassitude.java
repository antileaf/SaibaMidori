package me.antileaf.midori.cards.midori;

import basemod.devcommands.draw.Draw;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.DrawCardAction;
import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.WeakPower;
import me.antileaf.midori.actions.fatigue.TriggerFatigueAction;
import me.antileaf.midori.cards.AbstractMidoriCard;
import me.antileaf.midori.fatigue.FatigueManager;
import me.antileaf.midori.patches.enums.CardColorEnum;
import me.antileaf.midori.utils.MidoriHelper;

public class Lassitude extends AbstractMidoriCard {
	public static final String SIMPLE_NAME = Lassitude.class.getSimpleName();
	public static final String ID = MidoriHelper.makeID(SIMPLE_NAME);
	private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);

	private static final int COST = 0;
	private static final int MAGIC = 1;
	private static final int MAGIC2 = 1;
	private static final int UPGRADE_PLUS_MAGIC = 1;

	public Lassitude() {
		super(
				ID,
				cardStrings.NAME,
				null, // MidoriHelper.getCardImgFilePath(SIMPLE_NAME),
				COST,
				cardStrings.DESCRIPTION,
				CardType.SKILL,
				CardColorEnum.MIDORI_COLOR,
				CardRarity.BASIC,
				CardTarget.ENEMY
		);

		this.magicNumber = this.baseMagicNumber = MAGIC;
		this.secondaryMagicNumber = this.baseSecondaryMagicNumber = MAGIC2;
	}

	@Override
	public void use(AbstractPlayer p, AbstractMonster m) {
		for (int i = 0; i < this.magicNumber; i++)
			this.addToBot(new TriggerFatigueAction());

//		this.addToBot(new DrawCardAction(1));
		this.addToBot(new ApplyPowerAction(m, p,
				new WeakPower(m, this.secondaryMagicNumber, false)));
	}

	@Override
	public AbstractCard makeCopy() {
		return new Lassitude();
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
