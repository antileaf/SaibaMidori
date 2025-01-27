package me.antileaf.midori.cards.midori;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.DrawCardAction;
import com.megacrit.cardcrawl.actions.unique.PoisonLoseHpAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.PoisonPower;
import me.antileaf.midori.actions.hue.RemoveHueAction;
import me.antileaf.midori.actions.utils.AnonymousAction;
import me.antileaf.midori.cards.AbstractMidoriCard;
import me.antileaf.midori.cards.interfaces.ConditionalExhaustCard;
import me.antileaf.midori.hue.Hue;
import me.antileaf.midori.hue.HueManager;
import me.antileaf.midori.patches.enums.CardColorEnum;
import me.antileaf.midori.patches.enums.CardTagEnum;
import me.antileaf.midori.utils.MidoriHelper;

public class ColorIntensivism extends AbstractMidoriCard implements ConditionalExhaustCard {
	public static final String SIMPLE_NAME = ColorIntensivism.class.getSimpleName();
	public static final String ID = MidoriHelper.makeID(SIMPLE_NAME);
	private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);

	private static final int COST = 0;
	private static final int MAGIC = 2;
	private static final int UPGRADE_PLUS_MAGIC = 1;

	private boolean shouldExhaustThisTime = false;

	public ColorIntensivism() {
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
	public void triggerOnGlowCheck() {
		if (HueManager.hasAnyHue(this))
			this.glowColor = AbstractCard.GOLD_BORDER_GLOW_COLOR.cpy();
		else
			this.glowColor = AbstractCard.BLUE_BORDER_GLOW_COLOR.cpy();
	}

	@Override
	public void use(AbstractPlayer p, AbstractMonster m) {
		this.addToBot(new DrawCardAction(this.magicNumber));

		if (!HueManager.hasAnyHue(this))
			this.shouldExhaustThisTime = true;
		else
			this.addToBot(new RemoveHueAction(this));
	}

	@Override
	public boolean shouldExhaust() {
		boolean ret = this.shouldExhaustThisTime;
		this.shouldExhaustThisTime = false;
		return ret;
	}

	@Override
	public AbstractCard makeCopy() {
		return new ColorIntensivism();
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
