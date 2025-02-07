package me.antileaf.midori.cards.deprecated;

import com.megacrit.cardcrawl.actions.common.DrawCardAction;
import com.megacrit.cardcrawl.actions.common.HealAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import me.antileaf.midori.cards.AbstractMidoriCard;
import me.antileaf.midori.patches.enums.CardColorEnum;
import me.antileaf.midori.utils.MidoriHelper;

@Deprecated
public class Overflow extends AbstractMidoriCard {
	public static final String SIMPLE_NAME = Overflow.class.getSimpleName();
	public static final String ID = MidoriHelper.makeID(SIMPLE_NAME);
	private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);

	private static final int COST = 2;
	private static final int MAGIC = 5;
	private static final int MAGIC2 = 5;

	public Overflow() {
		super(
				ID,
				cardStrings.NAME,
				null, // MidoriHelper.getCardImgFilePath(SIMPLE_NAME),
				COST,
				cardStrings.DESCRIPTION,
				CardType.SKILL,
				CardColorEnum.MIDORI_COLOR,
				CardRarity.UNCOMMON,
				CardTarget.ALL
		);

		this.magicNumber = this.baseMagicNumber = MAGIC;
		this.secondaryMagicNumber = this.baseSecondaryMagicNumber = MAGIC2;

		this.exhaust = true;

		this.tags.add(CardTags.HEALING);
	}

	@Override
	public void use(AbstractPlayer p, AbstractMonster m) {
		this.addToBot(new HealAction(p, p, this.magicNumber));

		for (AbstractMonster mon : AbstractDungeon.getMonsters().monsters)
			if (!mon.isDeadOrEscaped())
				this.addToBot(new HealAction(mon, p, this.magicNumber));

		this.addToBot(new DrawCardAction(this.secondaryMagicNumber));
	}

	@Override
	public AbstractCard makeCopy() {
		return new Overflow();
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
