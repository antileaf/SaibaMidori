package me.antileaf.midori.cards.deprecated.midori;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.unique.PoisonLoseHpAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.PoisonPower;
import me.antileaf.midori.actions.utils.AnonymousAction;
import me.antileaf.midori.cards.AbstractMidoriCard;
import me.antileaf.midori.cards.interfaces.ConditionalExhaustCard;
import me.antileaf.midori.hue.Hue;
import me.antileaf.midori.hue.HueManager;
import me.antileaf.midori.patches.enums.CardColorEnum;
import me.antileaf.midori.patches.enums.CardTagEnum;
import me.antileaf.midori.utils.MidoriHelper;

@Deprecated
public class RapidInfection extends AbstractMidoriCard implements ConditionalExhaustCard {
	public static final String SIMPLE_NAME = RapidInfection.class.getSimpleName();
	public static final String ID = MidoriHelper.makeID(SIMPLE_NAME);
	private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);

	private static final int COST = 1;
	private static final int MAGIC = 4;
	private static final int UPGRADE_PLUS_MAGIC = 2;

	public RapidInfection() {
		super(
				ID,
				cardStrings.NAME,
				null, // MidoriHelper.getCardImgFilePath(SIMPLE_NAME),
				COST,
				cardStrings.DESCRIPTION,
				CardType.SKILL,
				CardColorEnum.MIDORI_COLOR,
				CardRarity.COMMON,
				CardTarget.ENEMY
		);

		this.magicNumber = this.baseMagicNumber = MAGIC;

		this.tags.add(CardTagEnum.MIDORI_INFECTION);
	}

	@Override
	public void triggerOnGlowCheck() {
		if (HueManager.hasHue(this, Hue.LAVA) || HueManager.hasHue(this, Hue.MINT))
			this.glowColor = AbstractCard.GOLD_BORDER_GLOW_COLOR.cpy();
		else
			this.glowColor = AbstractCard.BLUE_BORDER_GLOW_COLOR.cpy();
	}

	@Override
	public void use(AbstractPlayer p, AbstractMonster m) {
		this.addToBot(new ApplyPowerAction(m, p,
				new PoisonPower(m, p, this.magicNumber)));

		this.addToBot(new AnonymousAction(() -> {
			if (m.hasPower(PoisonPower.POWER_ID))
				this.addToBot(new PoisonLoseHpAction(m, m,
						m.getPower(PoisonPower.POWER_ID).amount,
						AbstractGameAction.AttackEffect.POISON));
		}));
	}

	@Override
	public boolean shouldExhaust() {
		return !(HueManager.hasHue(this, Hue.LAVA) || HueManager.hasHue(this, Hue.MINT));
	}

	@Override
	public AbstractCard makeCopy() {
		return new RapidInfection();
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
