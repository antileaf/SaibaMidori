package me.antileaf.midori.cards.midori;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.animations.VFXAction;
import com.megacrit.cardcrawl.actions.common.DamageAllEnemiesAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.vfx.combat.ScreenOnFireEffect;
import me.antileaf.midori.actions.hue.PaintAction;
import me.antileaf.midori.cards.AbstractMidoriCard;
import me.antileaf.midori.hue.Hue;
import me.antileaf.midori.patches.enums.CardColorEnum;
import me.antileaf.midori.utils.MidoriHelper;

public class Bioluminescence extends AbstractMidoriCard {
	public static final String SIMPLE_NAME = Bioluminescence.class.getSimpleName();
	public static final String ID = MidoriHelper.makeID(SIMPLE_NAME);
	private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);

	private static final int COST = 2;
	private static final int UPGRADED_COST = 1;

	public Bioluminescence() {
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
		this.addToBot(new PaintAction(Hue.MARIGOLD, p.discardPile.group));
	}

	@Override
	public AbstractCard makeCopy() {
		return new Bioluminescence();
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
