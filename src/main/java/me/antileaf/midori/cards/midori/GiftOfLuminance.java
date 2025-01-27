package me.antileaf.midori.cards.midori;

import com.evacipated.cardcrawl.mod.stslib.actions.common.SelectCardsInHandAction;
import com.megacrit.cardcrawl.actions.common.MakeTempCardInHandAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import me.antileaf.midori.actions.hue.RemoveHueAction;
import me.antileaf.midori.cards.AbstractMidoriCard;
import me.antileaf.midori.cards.colorless.Paint;
import me.antileaf.midori.hue.Hue;
import me.antileaf.midori.hue.HueManager;
import me.antileaf.midori.patches.enums.CardColorEnum;
import me.antileaf.midori.utils.MidoriHelper;

public class GiftOfLuminance extends AbstractMidoriCard {
	public static final String SIMPLE_NAME = GiftOfLuminance.class.getSimpleName();
	public static final String ID = MidoriHelper.makeID(SIMPLE_NAME);
	private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);

	private static final int COST = 1;
	private static final int UPGRADED_COST = 0;

	public GiftOfLuminance() {
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

		this.exhaust = true;
	}

	@Override
	public void use(AbstractPlayer p, AbstractMonster m) {
		this.addToBot(new SelectCardsInHandAction(
				1,
				cardStrings.EXTENDED_DESCRIPTION[0],
				false,
				false,
				c -> !(c instanceof GiftOfLuminance),
				(cards) -> {
					cards.forEach(c -> {
						AbstractCard copy = c.makeStatEquivalentCopy();
						HueManager.paintOutsideCombat(copy, Hue.MARIGOLD);
						this.addToTop(new MakeTempCardInHandAction(copy));
					});

					if (!cards.isEmpty())
						this.purgeOnUse = true;
				}
		));
	}

	@Override
	public AbstractCard makeCopy() {
		return new GiftOfLuminance();
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
