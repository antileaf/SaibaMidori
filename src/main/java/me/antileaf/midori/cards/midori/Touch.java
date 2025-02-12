package me.antileaf.midori.cards.midori;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import me.antileaf.midori.cards.AbstractMidoriCard;
import me.antileaf.midori.hue.Hue;
import me.antileaf.midori.hue.HueManager;
import me.antileaf.midori.patches.enums.CardColorEnum;
import me.antileaf.midori.utils.MidoriHelper;

import java.util.HashSet;
import java.util.Set;

public class Touch extends AbstractMidoriCard {
	public static final String SIMPLE_NAME = Touch.class.getSimpleName();
	public static final String ID = MidoriHelper.makeID(SIMPLE_NAME);
	private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);

	private static final int COST = 1;
	private static final int DAMAGE = 9;
	private static final int MAGIC = 1;
	private static final int UPGRADE_PLUS_DMG = 2;
	private static final int UPGRADE_PLUS_MAGIC = 1;

	public Touch() {
		super(
				ID,
				cardStrings.NAME,
				null, // MidoriHelper.getCardImgFilePath(SIMPLE_NAME),
				COST,
				cardStrings.DESCRIPTION,
				CardType.ATTACK,
				CardColorEnum.MIDORI_COLOR,
				CardRarity.BASIC,
				CardTarget.ENEMY
		);

		this.damage = this.baseDamage = DAMAGE;
		this.magicNumber = this.baseMagicNumber = MAGIC;
	}

	private int countHuesInHand() {
		Set<Hue> hues = new HashSet<>();

		for (AbstractCard c : AbstractDungeon.player.hand.group)
			for (Hue hue : Hue.values())
				if (HueManager.hasHue(c, hue))
					hues.add(hue);

		return hues.size();
	}

	@Override
	public void calculateCardDamage(AbstractMonster mo) {
		int realBaseDamage = this.baseDamage;
		this.baseDamage += this.magicNumber * this.countHuesInHand();
		super.calculateCardDamage(mo);
		this.baseDamage = realBaseDamage;
		this.isDamageModified = this.damage != this.baseDamage;
	}

	@Override
	public void applyPowers() {
		int realBaseDamage = this.baseDamage;
		this.baseDamage += this.magicNumber * this.countHuesInHand();
		super.applyPowers();
		this.baseDamage = realBaseDamage;
		this.isDamageModified = this.damage != this.baseDamage;
	}

	@Override
	public void use(AbstractPlayer p, AbstractMonster m) {
		this.addToBot(new DamageAction(m,
				new DamageInfo(p, this.damage, this.damageTypeForTurn),
				AbstractGameAction.AttackEffect.BLUNT_LIGHT));
	}

	@Override
	public AbstractCard makeCopy() {
		return new Touch();
	}

	@Override
	public void upgrade() {
		if (!this.upgraded) {
			this.upgradeName();
			this.upgradeDamage(UPGRADE_PLUS_DMG);
			this.upgradeMagicNumber(UPGRADE_PLUS_MAGIC);
			this.initializeDescription();
		}
	}
}
