package me.antileaf.midori.cards.midori;

import basemod.BaseMod;
import com.evacipated.cardcrawl.mod.stslib.actions.common.SelectCardsAction;
import com.evacipated.cardcrawl.mod.stslib.actions.common.SelectCardsInHandAction;
import com.megacrit.cardcrawl.actions.common.MakeTempCardInDrawPileAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.curses.Pain;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.watcher.MasterRealityPower;
import me.antileaf.midori.actions.common.ChooseOneCallbackAction;
import me.antileaf.midori.actions.common.FilteredDrawCardAction;
import me.antileaf.midori.actions.hue.PaintAction;
import me.antileaf.midori.cards.AbstractMidoriCard;
import me.antileaf.midori.cards.colorless.Paint;
import me.antileaf.midori.hue.Hue;
import me.antileaf.midori.hue.HueManager;
import me.antileaf.midori.patches.enums.CardColorEnum;
import me.antileaf.midori.utils.MidoriHelper;

import java.util.ArrayList;

public class CrowningTouch extends AbstractMidoriCard {
	public static final String SIMPLE_NAME = CrowningTouch.class.getSimpleName();
	public static final String ID = MidoriHelper.makeID(SIMPLE_NAME);
	private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);

	private static final int COST = 1;
	private static final int MAGIC = 1;
	private static final int UPGRADE_PLUS_MAGIC = 1;

	public CrowningTouch() {
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

		this.cardsToPreview = new Paint();
	}

	@Override
	public void use(AbstractPlayer p, AbstractMonster m) {
		this.addToBot(new SelectCardsAction(p.drawPile.group, this.magicNumber,
				cardStrings.EXTENDED_DESCRIPTION[0], false, c -> true,
				(cards) -> {
			for (AbstractCard c : cards) {
				c.unhover();

				int index = p.drawPile.group.indexOf(c);

				if (p.hand.size() >= BaseMod.MAX_HAND_SIZE) {
					p.drawPile.moveToDiscardPile(c);
					p.createHandIsFullDialog();
				} else
					p.drawPile.moveToHand(c, p.drawPile);

				Paint paint = new Paint();
				if (p.hasPower(MasterRealityPower.POWER_ID))
					paint.upgrade();
				p.drawPile.group.add(index, paint);
			}
		}));
	}

	@Override
	public AbstractCard makeCopy() {
		return new CrowningTouch();
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
