package me.antileaf.midori.cards.deprecated.midori;

import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.VulnerablePower;
import com.megacrit.cardcrawl.powers.WeakPower;
import me.antileaf.midori.cards.AbstractMidoriCard;
import me.antileaf.midori.hue.Hue;
import me.antileaf.midori.hue.HueManager;
import me.antileaf.midori.patches.enums.CardColorEnum;
import me.antileaf.midori.utils.MidoriHelper;

@Deprecated
public class ImpactWarhead extends AbstractMidoriCard {
	public static final String SIMPLE_NAME = ImpactWarhead.class.getSimpleName();
	public static final String ID = MidoriHelper.makeID(SIMPLE_NAME);
	private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);

	private static final int COST = 1;
	private static final int MAGIC = 2;
	private static final int UPGRADE_PLUS_MAGIC = 1;

	public ImpactWarhead() {
		super(
				ID,
				cardStrings.NAME,
				null, // MidoriHelper.getCardImgFilePath(SIMPLE_NAME),
				COST,
				cardStrings.DESCRIPTION,
				CardType.SKILL,
				CardColorEnum.MIDORI_COLOR,
				CardRarity.COMMON,
				CardTarget.ALL_ENEMY
		);

		this.magicNumber = this.baseMagicNumber = MAGIC;
	}

	@Override
	public void triggerOnGlowCheck() {
		if (HueManager.hasHue(this, Hue.LAVA))
			this.glowColor = AbstractCard.GOLD_BORDER_GLOW_COLOR.cpy();
		else
			this.glowColor = AbstractCard.BLUE_BORDER_GLOW_COLOR.cpy();
	}

	@Override
	public void use(AbstractPlayer p, AbstractMonster m) {
		if (HueManager.hasHue(this, Hue.LAVA)) {
			for (AbstractMonster mo : AbstractDungeon.getMonsters().monsters)
				if (!mo.isDeadOrEscaped())
					this.addToBot(new ApplyPowerAction(mo, p,
							new VulnerablePower(mo, this.magicNumber, false)));
		}
		else {
			for (AbstractMonster mo : AbstractDungeon.getMonsters().monsters)
				if (!mo.isDeadOrEscaped())
					this.addToBot(new ApplyPowerAction(mo, p,
							new WeakPower(mo, this.magicNumber, false)));
		}
	}

	@Override
	public AbstractCard makeCopy() {
		return new ImpactWarhead();
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
