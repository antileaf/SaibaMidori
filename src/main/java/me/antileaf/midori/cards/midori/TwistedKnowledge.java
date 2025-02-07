package me.antileaf.midori.cards.midori;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.StrengthPower;
import com.megacrit.cardcrawl.powers.VulnerablePower;
import me.antileaf.midori.cards.AbstractMidoriCard;
import me.antileaf.midori.hue.Hue;
import me.antileaf.midori.hue.HueManager;
import me.antileaf.midori.patches.enums.CardColorEnum;
import me.antileaf.midori.patches.hue.HueThisCombatPatch;
import me.antileaf.midori.powers.unique.TwistedKnowledgePower;
import me.antileaf.midori.utils.MidoriHelper;

import java.util.ArrayList;

public class TwistedKnowledge extends AbstractMidoriCard {
	public static final String SIMPLE_NAME = TwistedKnowledge.class.getSimpleName();
	public static final String ID = MidoriHelper.makeID(SIMPLE_NAME);
	private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);

	private static final int COST = 0;
	private static final int MAGIC = 3;
	private static final int MAGIC2 = 12;
	private static final int UPGRADE_PLUS_MAGIC2 = 6;

	public TwistedKnowledge() {
		super(
				ID,
				cardStrings.NAME,
				null, // MidoriHelper.getCardImgFilePath(SIMPLE_NAME),
				COST,
				cardStrings.DESCRIPTION,
				CardType.POWER,
				CardColorEnum.MIDORI_COLOR,
				CardRarity.UNCOMMON,
				CardTarget.SELF
		);

		this.magicNumber = this.baseMagicNumber = MAGIC;
		this.secondaryMagicNumber = this.baseSecondaryMagicNumber = MAGIC2;
	}

	@Override
	public void use(AbstractPlayer p, AbstractMonster m) {
		this.addToBot(new ApplyPowerAction(p, p,
				new StrengthPower(p, this.magicNumber)));

		this.addToBot(new ApplyPowerAction(p, p,
				new TwistedKnowledgePower(this.secondaryMagicNumber, this.magicNumber)));
	}

	@Override
	public AbstractCard makeCopy() {
		return new TwistedKnowledge();
	}

	@Override
	public void upgrade() {
		if (!this.upgraded) {
			this.upgradeName();
			this.upgradeSecondaryMagicNumber(UPGRADE_PLUS_MAGIC2);
			this.initializeDescription();
		}
	}
}
