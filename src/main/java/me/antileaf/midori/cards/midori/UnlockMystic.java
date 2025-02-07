package me.antileaf.midori.cards.midori;

import com.megacrit.cardcrawl.actions.common.DrawCardAction;
import com.megacrit.cardcrawl.actions.common.GainEnergyAction;
import com.megacrit.cardcrawl.actions.utility.NewQueueCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.CardLibrary;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.relics.ChemicalX;
import me.antileaf.midori.actions.common.ChooseOneCallbackAction;
import me.antileaf.midori.actions.utils.AnonymousAction;
import me.antileaf.midori.cards.AbstractMidoriCard;
import me.antileaf.midori.patches.enums.CardColorEnum;
import me.antileaf.midori.utils.MidoriHelper;

import java.util.ArrayList;

public class UnlockMystic extends AbstractMidoriCard {
	public static final String SIMPLE_NAME = UnlockMystic.class.getSimpleName();
	public static final String ID = MidoriHelper.makeID(SIMPLE_NAME);
	private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);

	private static final int COST = -1;
	private static final int MAGIC = 3;
	private static final int UPGRADE_PLUS_MAGIC = 1;

	public UnlockMystic() {
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
		this.exhaust = true;
	}

	@Override
	public void use(AbstractPlayer p, AbstractMonster m) {
		this.addToBot(new AnonymousAction(() -> {
			int amount = this.energyOnUse;

			if (p.hasRelic(ChemicalX.ID)) {
				amount += ChemicalX.BOOST;
				p.getRelic(ChemicalX.ID).flash();
			}

//			int energyCost = this.energyOnUse;
//			if (amount > 3)
//				energyCost -= amount - 3;

			amount = Math.min(amount, 3);

			if (!this.freeToPlayOnce)
				p.energy.use(this.energyOnUse);

			int finalAmount = amount;
			ArrayList<AbstractCard> cards = CardLibrary.getAllCards().stream()
					.filter(card -> card.type == CardType.POWER)
					.filter(card -> card.rarity != CardRarity.SPECIAL)
					.filter(card -> card.cost == finalAmount)
					.collect(ArrayList::new, ArrayList::add, ArrayList::addAll);

			ArrayList<AbstractCard> choices = new ArrayList<>();
			for (int i = 0; i < this.magicNumber; i++) {
				if (cards.isEmpty())
					break;

				int index = AbstractDungeon.cardRandomRng.random(cards.size() - 1);
				choices.add(cards.get(index).makeCopy());
				cards.remove(index);
			}

			this.addToTop(new ChooseOneCallbackAction(choices,
					card -> {
				this.addToTop(new NewQueueCardAction(card, true, false, true));
					},
					cardStrings.EXTENDED_DESCRIPTION[0],
					true
			));
		}));
	}

	@Override
	public AbstractCard makeCopy() {
		return new UnlockMystic();
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
