package me.antileaf.midori.cards.midori;

import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.DrawCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.StrengthPower;
import com.megacrit.cardcrawl.powers.VulnerablePower;
import com.megacrit.cardcrawl.powers.WeakPower;
import me.antileaf.midori.cards.AbstractMidoriCard;
import me.antileaf.midori.hue.Hue;
import me.antileaf.midori.hue.HueManager;
import me.antileaf.midori.patches.enums.CardColorEnum;
import me.antileaf.midori.utils.MidoriHelper;

public class Drifting extends AbstractMidoriCard {
	public static final String SIMPLE_NAME = Drifting.class.getSimpleName();
	public static final String ID = MidoriHelper.makeID(SIMPLE_NAME);
	private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);

	private static final int COST = 1;
	private static final int MAGIC = 2;
	private static final int UPGRADE_PLUS_MAGIC = 1;

	public Drifting() {
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
	}

	private int count() {
		int total = 0;

		if (AbstractDungeon.player.hasPower(StrengthPower.POWER_ID) &&
				AbstractDungeon.player.getPower(StrengthPower.POWER_ID).amount > 0)
			total++;

		for (AbstractMonster mo : AbstractDungeon.getMonsters().monsters)
			if (!mo.isDeadOrEscaped() && mo.hasPower(StrengthPower.POWER_ID) &&
					mo.getPower(StrengthPower.POWER_ID).amount > 0)
				total++;

		return total;
	}

	@Override
	public void applyPowers() {
		int realBaseMagicNumber = this.baseMagicNumber;
		this.baseMagicNumber += this.count();
		this.magicNumber = this.baseMagicNumber;

		super.applyPowers();

		this.baseMagicNumber = realBaseMagicNumber;
		this.isMagicNumberModified = this.magicNumber != this.baseMagicNumber;
	}

	@Override
	public void use(AbstractPlayer p, AbstractMonster m) {
		this.addToBot(new DrawCardAction(this.magicNumber));
	}

	@Override
	public AbstractCard makeCopy() {
		return new Drifting();
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
