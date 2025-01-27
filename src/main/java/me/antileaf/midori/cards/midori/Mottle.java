package me.antileaf.midori.cards.midori;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.ReducePowerAction;
import com.megacrit.cardcrawl.actions.unique.PoisonLoseHpAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
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

public class Mottle extends AbstractMidoriCard {
	public static final String SIMPLE_NAME = Mottle.class.getSimpleName();
	public static final String ID = MidoriHelper.makeID(SIMPLE_NAME);
	private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);

	private static final int COST = 1;
	private static final int UPGRADED_COST = 0;
	private static final int MAGIC = 4;

	public Mottle() {
		super(
				ID,
				cardStrings.NAME,
				null, // MidoriHelper.getCardImgFilePath(SIMPLE_NAME),
				COST,
				cardStrings.DESCRIPTION,
				CardType.SKILL,
				CardColorEnum.MIDORI_COLOR,
				CardRarity.UNCOMMON,
				CardTarget.ENEMY
		);

		this.magicNumber = this.baseMagicNumber = MAGIC;

		this.exhaust = true;
	}

	@Override
	public void use(AbstractPlayer p, AbstractMonster m) {
		this.addToBot(new ApplyPowerAction(m, p,
				new PoisonPower(m, p, this.magicNumber), this.magicNumber,
				AbstractGameAction.AttackEffect.POISON));

		this.addToBot(new AnonymousAction(() -> {
			int poison = m.hasPower(PoisonPower.POWER_ID) ? m.getPower(PoisonPower.POWER_ID).amount : 0;

			for (AbstractMonster other : AbstractDungeon.getMonsters().monsters)
				if (other != m) {
					int otherPoison = other.hasPower(PoisonPower.POWER_ID) ?
							other.getPower(PoisonPower.POWER_ID).amount : 0;

					if (otherPoison < poison)
						MidoriHelper.addActionToBuffer(new ApplyPowerAction(other, p,
								new PoisonPower(other, p, poison - otherPoison),
								poison - otherPoison,
								AbstractGameAction.AttackEffect.POISON));
					else
						MidoriHelper.addActionToBuffer(new ReducePowerAction(other, p, PoisonPower.POWER_ID,
								otherPoison - poison));
				}

			MidoriHelper.commitBuffer();
		}));
	}

	@Override
	public AbstractCard makeCopy() {
		return new Mottle();
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
