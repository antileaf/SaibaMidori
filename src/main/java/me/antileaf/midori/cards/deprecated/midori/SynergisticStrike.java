package me.antileaf.midori.cards.deprecated.midori;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.actions.utility.NewQueueCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import me.antileaf.midori.actions.utils.AnonymousAction;
import me.antileaf.midori.cards.AbstractMidoriCard;
import me.antileaf.midori.hue.Hue;
import me.antileaf.midori.hue.HueManager;
import me.antileaf.midori.patches.enums.CardColorEnum;
import me.antileaf.midori.utils.MidoriHelper;

@Deprecated
public class SynergisticStrike extends AbstractMidoriCard {
	public static final String SIMPLE_NAME = SynergisticStrike.class.getSimpleName();
	public static final String ID = MidoriHelper.makeID(SIMPLE_NAME);
	private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);

	private static final int COST = 1;
	private static final int DAMAGE = 10;
	private static final int UPGRADE_PLUS_DMG = 3;

	public SynergisticStrike() {
		super(
				ID,
				cardStrings.NAME,
				null, // MidoriHelper.getCardImgFilePath(SIMPLE_NAME),
				COST,
				cardStrings.DESCRIPTION,
				CardType.ATTACK,
				CardColorEnum.MIDORI_COLOR,
				CardRarity.COMMON,
				CardTarget.ENEMY
		);

		this.damage = this.baseDamage = DAMAGE;

		this.tags.add(CardTags.STRIKE);
	}

	@Override
	public void use(AbstractPlayer p, AbstractMonster m) {
		this.addToBot(new DamageAction(m,
				new DamageInfo(p, this.damage, this.damageTypeForTurn),
				AbstractGameAction.AttackEffect.BLUNT_LIGHT));

		for (AbstractCard c : p.hand.group)
			if (c.hasTag(CardTags.STARTER_STRIKE)) {
				for (Hue hue : Hue.values())
					if (HueManager.hasHue(this, hue) && HueManager.hasHue(c, hue)) {
						this.addToBot(new AnonymousAction(() -> {
							if (!m.isDeadOrEscaped())
								this.addToBot(new NewQueueCardAction(c, m, false, true));
							else
								this.addToBot(new NewQueueCardAction(c, true, false, true));
						}));

						break;
					}
			}
	}

	@Override
	public AbstractCard makeCopy() {
		return new SynergisticStrike();
	}

	@Override
	public void upgrade() {
		if (!this.upgraded) {
			this.upgradeName();
			this.upgradeDamage(UPGRADE_PLUS_DMG);
			this.initializeDescription();
		}
	}
}
