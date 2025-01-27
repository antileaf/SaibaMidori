package me.antileaf.midori.cards.midori;

import com.megacrit.cardcrawl.actions.common.DrawCardAction;
import com.megacrit.cardcrawl.actions.common.ExhaustSpecificCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.StrengthPower;
import me.antileaf.midori.actions.common.ChooseOneCallbackAction;
import me.antileaf.midori.actions.utils.AnonymousAction;
import me.antileaf.midori.cards.AbstractMidoriCard;
import me.antileaf.midori.cards.colorless.LeftChoice;
import me.antileaf.midori.cards.colorless.RightChoice;
import me.antileaf.midori.cards.interfaces.ConditionalExhaustCard;
import me.antileaf.midori.patches.enums.CardColorEnum;
import me.antileaf.midori.utils.MidoriHelper;

import java.util.ArrayList;
import java.util.stream.Stream;

public class SurrenderToMadness extends AbstractMidoriCard implements ConditionalExhaustCard {
	public static final String SIMPLE_NAME = SurrenderToMadness.class.getSimpleName();
	public static final String ID = MidoriHelper.makeID(SIMPLE_NAME);
	private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);

	private static final int COST = 1;
	private static final int UPGRADED_COST = 0;
	private static final int MAGIC = 3;

	private boolean shouldExhaustThisTime = false;

	public SurrenderToMadness() {
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
	public void use(AbstractPlayer p, AbstractMonster m) {
		this.addToBot(new DrawCardAction(this.magicNumber, new AnonymousAction(() -> {
			ArrayList<AbstractCard> choices = new ArrayList<>();
			choices.add(new LeftChoice(true));
			choices.add(new RightChoice(DrawCardAction.drawnCards));

			MidoriHelper.addToTop(new ChooseOneCallbackAction(
					choices,
					card -> {
						if (card instanceof LeftChoice)
							this.shouldExhaustThisTime = true;
						else if (card instanceof RightChoice) {
							for (AbstractCard c : DrawCardAction.drawnCards)
								MidoriHelper.addActionToBuffer(new ExhaustSpecificCardAction(c, p.hand));

							MidoriHelper.commitBuffer();
						}
					},
					cardStrings.EXTENDED_DESCRIPTION[0],
					false
			));
		})));
	}

	@Override
	public boolean shouldExhaust() {
		boolean ret = this.shouldExhaustThisTime;
		this.shouldExhaustThisTime = false;
		return ret;
	}

	@Override
	public AbstractCard makeCopy() {
		return new SurrenderToMadness();
	}

	@Override
	public void upgrade() {
		if (!this.upgraded) {
			this.upgradeName();
			this.upgradeBaseCost(UPGRADED_COST);
			this.initializeDescription();
		}
	}
}
