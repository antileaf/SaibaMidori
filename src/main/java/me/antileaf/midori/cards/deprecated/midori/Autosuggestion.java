package me.antileaf.midori.cards.deprecated.midori;

import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.DexterityPower;
import com.megacrit.cardcrawl.powers.FocusPower;
import com.megacrit.cardcrawl.powers.StrengthPower;
import com.megacrit.cardcrawl.relics.ChemicalX;
import me.antileaf.midori.actions.common.ChooseOneCallbackAction;
import me.antileaf.midori.actions.utils.AnonymousAction;
import me.antileaf.midori.cards.AbstractMidoriCard;
import me.antileaf.midori.cards.deprecated.colorless.GoWithGreen;
import me.antileaf.midori.cards.deprecated.colorless.GoWithRed;
import me.antileaf.midori.hue.Hue;
import me.antileaf.midori.patches.enums.CardColorEnum;
import me.antileaf.midori.utils.MidoriHelper;

import java.util.ArrayList;

@Deprecated
public class Autosuggestion extends AbstractMidoriCard {
	public static final String SIMPLE_NAME = Autosuggestion.class.getSimpleName();
	public static final String ID = MidoriHelper.makeID(SIMPLE_NAME);
	private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);

	private static final int COST = -1;
//	private static final int MAGIC = 1;
//	private static final int MAGIC2 = 1;
//	private static final int UPGRADE_PLUS_MAGIC2 = 1;

	public Autosuggestion() {
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

//		this.magicNumber = this.baseMagicNumber = MAGIC;
//		this.secondaryMagicNumber = this.baseSecondaryMagicNumber = MAGIC2;

		this.fixedHue = Hue.SKY;
	}

	@Override
	public void use(AbstractPlayer p, AbstractMonster m) {
		this.addToBot(new AnonymousAction(() -> {
			int amount = this.energyOnUse + (this.upgraded ? 1 : 0);

			if (p.hasRelic(ChemicalX.ID)) {
				amount += ChemicalX.BOOST;
				p.getRelic(ChemicalX.ID).flash();
			}

			if (amount > 0) {
				if (!this.freeToPlayOnce)
					p.energy.use(this.energyOnUse);

				this.addToBot(new ApplyPowerAction(p, p,
						new FocusPower(p, amount)));

				ArrayList<AbstractCard> choices = new ArrayList<>();
				choices.add(new GoWithRed(amount));
				choices.add(new GoWithGreen(amount));

				if (this.upgraded)
					choices.forEach(AbstractCard::upgrade);

				int finalAmount = amount;
				this.addToBot(new ChooseOneCallbackAction(choices,
						card -> {
							if (card instanceof GoWithRed)
								this.addToBot(new ApplyPowerAction(p, p,
										new StrengthPower(p, finalAmount)));
							else if (card instanceof GoWithGreen)
								this.addToBot(new ApplyPowerAction(p, p,
										new DexterityPower(p, finalAmount)));
						},
						cardStrings.EXTENDED_DESCRIPTION[0],
						false));
			}
		}));
	}

	@Override
	public AbstractCard makeCopy() {
		return new Autosuggestion();
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
