package me.antileaf.midori.cards.midori;

import basemod.helpers.TooltipInfo;
import com.megacrit.cardcrawl.actions.GameActionManager;
import com.megacrit.cardcrawl.actions.common.DrawCardAction;
import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import me.antileaf.midori.actions.hue.PaintAction;
import me.antileaf.midori.cards.AbstractMidoriCard;
import me.antileaf.midori.hue.Hue;
import me.antileaf.midori.patches.enums.CardColorEnum;
import me.antileaf.midori.utils.MidoriHelper;

import java.util.ArrayList;
import java.util.List;

public class Induce extends AbstractMidoriCard {
	public static final String SIMPLE_NAME = Induce.class.getSimpleName();
	public static final String ID = MidoriHelper.makeID(SIMPLE_NAME);
	private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);

	private static final int COST = 1;
	private static final int MAGIC = 3;
	private static final int UPGRADE_PLUS_MAGIC = 1;

	public Induce() {
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

	@Override
	public List<TooltipInfo> getCustomTooltips() {
		ArrayList<TooltipInfo> tips = new ArrayList<>();
		tips.add(new TooltipInfo(cardStrings.EXTENDED_DESCRIPTION[0],
				cardStrings.EXTENDED_DESCRIPTION[1]));
		return tips;
	}

	@Override
	public void use(AbstractPlayer p, AbstractMonster m) {
		this.addToBot(new DrawCardAction(this.magicNumber));

		if (AbstractDungeon.actionManager.cardsPlayedThisCombat.size() > 1) {
			AbstractCard last = AbstractDungeon.actionManager.cardsPlayedThisCombat.get(
					AbstractDungeon.actionManager.cardsPlayedThisCombat.size() - 2);

			this.addToBot(new PaintAction(Hue.VIOLET, last));
		}
	}

	@Override
	public AbstractCard makeCopy() {
		return new Induce();
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
