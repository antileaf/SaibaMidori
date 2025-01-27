package me.antileaf.midori.cards.colorless;

import com.evacipated.cardcrawl.mod.stslib.actions.common.SelectCardsInHandAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.vfx.ThoughtBubble;
import me.antileaf.midori.actions.common.ChooseOneCallbackAction;
import me.antileaf.midori.actions.hue.PaintAction;
import me.antileaf.midori.cards.AbstractMidoriCard;
import me.antileaf.midori.hue.Hue;
import me.antileaf.midori.hue.HueManager;
import me.antileaf.midori.utils.MidoriHelper;

import java.util.ArrayList;

public class Paint extends AbstractMidoriCard {
	public static final String SIMPLE_NAME = Paint.class.getSimpleName();
	public static final String ID = MidoriHelper.makeID(SIMPLE_NAME);
	private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);

	private static final int COST = 0;
	private static final int MAGIC = 1;
	private static final int UPGRADE_PLUS_MAGIC = 1;

	public Paint() {
		super(
				ID,
				cardStrings.NAME,
				null, // MidoriHelper.getCardImgFilePath(SIMPLE_NAME),
				COST,
				cardStrings.DESCRIPTION,
				CardType.SKILL,
				CardColor.COLORLESS,
				CardRarity.SPECIAL,
				CardTarget.NONE
		);

		this.magicNumber = this.baseMagicNumber = MAGIC;

		this.retain = this.selfRetain = true;
		this.exhaust = true;
	}

	@Override
	public void use(AbstractPlayer p, AbstractMonster m) {
		ArrayList<AbstractCard> choices = new ArrayList<>();
		choices.add(new PaintChoiceCard(Hue.LAVA));
		choices.add(new PaintChoiceCard(Hue.MINT));
		choices.add(new PaintChoiceCard(Hue.SKY));
		if (this.upgraded)
			choices.add(new PaintChoiceCard(Hue.INK));

		this.addToBot(new ChooseOneCallbackAction(
				choices,
				(card) -> {
					if (card instanceof PaintChoiceCard) {
						Hue hue = ((PaintChoiceCard) card).hue;

						this.addToTop(new SelectCardsInHandAction(
								this.magicNumber,
								cardStrings.EXTENDED_DESCRIPTION[0],
								false,
								false,
								HueManager::canBePainted,
								(cards) -> {
									MidoriHelper.addToTop(new PaintAction(hue, cards));
								}
						));
					}
				},
				cardStrings.EXTENDED_DESCRIPTION[1],
				false
		));
	}

	@Override
	public AbstractCard makeCopy() {
		return new Paint();
	}

	@Override
	public void upgrade() {
		if (!this.upgraded) {
			this.upgradeName();
//			this.upgradeMagicNumber(UPGRADE_PLUS_MAGIC);
			this.rawDescription = cardStrings.UPGRADE_DESCRIPTION;
			this.initializeDescription();
		}
	}
}
