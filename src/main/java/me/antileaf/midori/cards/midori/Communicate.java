package me.antileaf.midori.cards.midori;

import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.actions.common.GainEnergyAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import me.antileaf.midori.cards.AbstractMidoriCard;
import me.antileaf.midori.hue.Hue;
import me.antileaf.midori.hue.HueManager;
import me.antileaf.midori.patches.enums.CardColorEnum;
import me.antileaf.midori.utils.MidoriHelper;

public class Communicate extends AbstractMidoriCard {
	public static final String SIMPLE_NAME = Communicate.class.getSimpleName();
	public static final String ID = MidoriHelper.makeID(SIMPLE_NAME);
	private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);

	private static final int COST = 1;
	private static final int MAGIC = 3;

	public Communicate() {
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
	}

	@Override
	public boolean canUse(AbstractPlayer p, AbstractMonster m) {
		if (!super.canUse(p, m))
			return false;

		boolean mint = false, sky = false, other = false;
		for (AbstractCard c : p.hand.group) {
			if (HueManager.hasHue(c, Hue.MINT))
				mint = true;
			if (HueManager.hasHue(c, Hue.SKY))
				sky = true;

			for (Hue h : Hue.values())
				if (h != Hue.MINT && h != Hue.SKY && HueManager.hasHue(c, h)) {
					other = true;
					break;
				}

			other |= !HueManager.hasAnyHue(c);
		}

		if (!(mint && sky)) {
			this.cantUseMessage = cardStrings.EXTENDED_DESCRIPTION[0];
			return false;
		}
		else if (!this.upgraded && other) {
			this.cantUseMessage = cardStrings.EXTENDED_DESCRIPTION[1];
			return false;
		}

		return true;
	}

	@Override
	public void use(AbstractPlayer p, AbstractMonster m) {
		this.addToBot(new GainEnergyAction(this.magicNumber));
	}

	@Override
	public AbstractCard makeCopy() {
		return new Communicate();
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
