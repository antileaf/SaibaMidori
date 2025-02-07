package me.antileaf.midori.cards.midori;

import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.NoBlockPower;
import me.antileaf.midori.actions.utils.AnonymousAction;
import me.antileaf.midori.cards.AbstractMidoriCard;
import me.antileaf.midori.patches.enums.CardColorEnum;
import me.antileaf.midori.powers.unique.TimeOutPower;
import me.antileaf.midori.utils.MidoriHelper;

public class TimeOut extends AbstractMidoriCard {
	public static final String SIMPLE_NAME = TimeOut.class.getSimpleName();
	public static final String ID = MidoriHelper.makeID(SIMPLE_NAME);
	private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);

	private static final int COST = 1;
	private static final int BLOCK = 8;
	private static final int UPGRADE_PLUS_BLOCK = 4;

	public TimeOut() {
		super(
				ID,
				cardStrings.NAME,
				null, // MidoriHelper.getCardImgFilePath(SIMPLE_NAME),
				COST,
				cardStrings.DESCRIPTION,
				CardType.SKILL,
				CardColorEnum.MIDORI_COLOR,
				CardRarity.RARE,
				CardTarget.SELF
		);

		this.block = this.baseBlock = BLOCK;
		this.exhaust = true;
	}

	@Override
	public void use(AbstractPlayer p, AbstractMonster m) {
		int finalBlock = this.block;
		this.addToBot(new AnonymousAction(() -> {
			if (!p.hasPower(NoBlockPower.POWER_ID) && p.currentBlock < finalBlock)
				this.addToBot(new GainBlockAction(p, finalBlock - p.currentBlock));
		}));

		if (this.block > 0)
			this.addToBot(new ApplyPowerAction(p, p, new TimeOutPower(this.block)));
	}

	@Override
	public AbstractCard makeCopy() {
		return new TimeOut();
	}

	@Override
	public void upgrade() {
		if (!this.upgraded) {
			this.upgradeName();
			this.upgradeBlock(UPGRADE_PLUS_BLOCK);
			this.initializeDescription();
		}
	}
}
