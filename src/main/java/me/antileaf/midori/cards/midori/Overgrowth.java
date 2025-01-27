package me.antileaf.midori.cards.midori;

import com.megacrit.cardcrawl.actions.common.DrawCardAction;
import com.megacrit.cardcrawl.actions.common.GainEnergyAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.StrengthPower;
import me.antileaf.midori.actions.hue.RemoveHueAction;
import me.antileaf.midori.cards.AbstractMidoriCard;
import me.antileaf.midori.hue.Hue;
import me.antileaf.midori.hue.HueManager;
import me.antileaf.midori.patches.enums.CardColorEnum;
import me.antileaf.midori.utils.MidoriHelper;

public class Overgrowth extends AbstractMidoriCard {
	public static final String SIMPLE_NAME = Overgrowth.class.getSimpleName();
	public static final String ID = MidoriHelper.makeID(SIMPLE_NAME);
	private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);

	private static final int COST = 0;

	public Overgrowth() {
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
	}

	private int count() {
		return (int) AbstractDungeon.player.hand.group.stream()
				.filter(c -> HueManager.hasHue(c, Hue.MINT))
				.count();
	}

	@Override
	public void applyPowers() {
		this.magicNumber = this.baseMagicNumber = this.count();

		super.applyPowers();

		this.initializeDescription();
	}

	@Override
	public void initializeDescription() {
		this.rawDescription = !this.upgraded ? cardStrings.DESCRIPTION : cardStrings.UPGRADE_DESCRIPTION;

		if (MidoriHelper.isInBattle() && AbstractDungeon.player.hand.contains(this))
			this.rawDescription += " NL " + cardStrings.EXTENDED_DESCRIPTION[0];

		super.initializeDescription();
	}

	@Override
	public void use(AbstractPlayer p, AbstractMonster m) {
		this.addToBot(new RemoveHueAction(p.hand.group.stream()
				.filter(c -> HueManager.hasHue(c, Hue.MINT))
				.toArray(AbstractCard[]::new)));

		this.addToBot(new GainEnergyAction(this.count()));
	}

	@Override
	public AbstractCard makeCopy() {
		return new Overgrowth();
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
