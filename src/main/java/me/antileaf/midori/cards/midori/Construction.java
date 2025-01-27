package me.antileaf.midori.cards.midori;

import com.megacrit.cardcrawl.actions.common.DrawCardAction;
import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.actions.common.GainEnergyAction;
import com.megacrit.cardcrawl.actions.defect.ChannelAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.orbs.Frost;
import me.antileaf.midori.actions.common.ChooseOneCallbackAction;
import me.antileaf.midori.actions.common.FilteredDrawCardAction;
import me.antileaf.midori.actions.utils.AnonymousAction;
import me.antileaf.midori.cards.AbstractMidoriCard;
import me.antileaf.midori.cards.colorless.BreakChoice;
import me.antileaf.midori.cards.colorless.ContinueChoice;
import me.antileaf.midori.hue.Hue;
import me.antileaf.midori.hue.HueManager;
import me.antileaf.midori.patches.enums.CardColorEnum;
import me.antileaf.midori.utils.MidoriHelper;

import java.util.ArrayList;

public class Construction extends AbstractMidoriCard {
	public static final String SIMPLE_NAME = Construction.class.getSimpleName();
	public static final String ID = MidoriHelper.makeID(SIMPLE_NAME);
	private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);

	private static final int COST = 1;
	private static final int MAGIC = 1;
	private static final int UPGRADE_PLUS_MAGIC = 1;

	public Construction() {
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

		this.magicNumber = this.baseMagicNumber = MAGIC;
	}

	@Override
	public void use(AbstractPlayer p, AbstractMonster m) {
		this.addToBot(new DrawCardAction(this.magicNumber));

//		if (!this.upgraded)
			this.addToBot(new FilteredDrawCardAction(this.magicNumber,
					c -> HueManager.hasHue(c, Hue.SKY), true, null));
//		else {
//			ArrayList<AbstractCard> choices = new ArrayList<>();
//			choices.add(new ContinueChoice());
//			choices.add(new BreakChoice());
//
//			this.addToBot(new ChooseOneCallbackAction(choices,
//					card -> {
//						if (card instanceof ContinueChoice) {
//							this.addToTop(new FilteredDrawCardAction(this.magicNumber,
//									c -> HueManager.hasHue(c, Hue.SKY),
//									true, null));
//						} else if (card instanceof BreakChoice) {
//							this.addToTop(new GainEnergyAction(1));
//						}
//					},
//					cardStrings.EXTENDED_DESCRIPTION[0],
//					false));
//		}
	}

	@Override
	public AbstractCard makeCopy() {
		return new Construction();
	}

	@Override
	public void upgrade() {
		if (!this.upgraded) {
			this.upgradeName();

//			this.rawDescription = cardStrings.UPGRADE_DESCRIPTION;
			this.upgradeMagicNumber(UPGRADE_PLUS_MAGIC);

			this.initializeDescription();
		}
	}
}
