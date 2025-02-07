package me.antileaf.midori.cards.midori;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import me.antileaf.midori.cards.AbstractMidoriCard;
import me.antileaf.midori.hue.Hue;
import me.antileaf.midori.hue.HueManager;
import me.antileaf.midori.patches.enums.CardColorEnum;
import me.antileaf.midori.powers.unique.CutinNextTurnPower;
import me.antileaf.midori.utils.MidoriHelper;

public class Cutin extends AbstractMidoriCard {
	public static final String SIMPLE_NAME = Cutin.class.getSimpleName();
	public static final String ID = MidoriHelper.makeID(SIMPLE_NAME);
	private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);

	private static final int COST = 2;
	private static final int DAMAGE = 32;

	public Cutin() {
		super(
				ID,
				cardStrings.NAME,
				null, // MidoriHelper.getCardImgFilePath(SIMPLE_NAME),
				COST,
				cardStrings.DESCRIPTION,
				CardType.ATTACK,
				CardColorEnum.MIDORI_COLOR,
				CardRarity.UNCOMMON,
				CardTarget.ENEMY
		);

		this.damage = this.baseDamage = DAMAGE;
	}

	@Override
	public void use(AbstractPlayer p, AbstractMonster m) {
		this.addToBot(new DamageAction(m,
				new DamageInfo(p, this.damage, this.damageTypeForTurn),
				AbstractGameAction.AttackEffect.BLUNT_HEAVY));

		this.addToBot(new ApplyPowerAction(p, p,
				new CutinNextTurnPower(HueManager.getHue(this), !this.upgraded)));
	}

	@Override
	public AbstractCard makeCopy() {
		return new Cutin();
	}

	@Override
	public void upgrade() {
		if (!this.upgraded) {
			this.upgradeName();

			this.rawDescription = cardStrings.UPGRADE_DESCRIPTION;

			this.initializeDescription();
		}
	}
}
